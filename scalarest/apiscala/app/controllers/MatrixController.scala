package controllers

import models.{MatrixLine,MatrixParams}
import utils.{Utils, Solver, Objective}
//import basicSolver.Solver
//import basicSolver.Objective
import scala.collection.mutable.{ListBuffer, Map}
import scala.math.log10
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

@Singleton
class MatrixController @Inject()(val controllerComponents: ControllerComponents)
extends BaseController {

    private var matrixAsStringArray:Array[Array[String]] = null
    private var matrixAsString:String = null
    var matrix: Array[Array[Double]] = Array[Array[Double]]()
    var normMatrix: Array[Array[Double]] = Array[Array[Double]]()
    var numberSolve = 0
    var procMatrix: Array[Array[Double]] = Array[Array[Double]]()
    var positions: Map[Int,Int] = Map[Int,Int]()
    var basicResults : (List[Int], List[Int], Double, List[List[Int]],List[List[Int]]) = null
    var results : ListBuffer[(List[Int], List[Int], Double, List[List[Int]],List[List[Int]])] = null
    var params = (10,30)
    var transposeMatrix = false
    var cellNamesIn = false
    var geneNamesIn = false
    var genesNames = Array[String]()
    var cellsNames = Array[String]()
    implicit val matrixLineJson = Json.format[MatrixLine]
    implicit val matrixParamsJson = Json.format[MatrixParams]

    def putLine() = Action { implicit request => 
        //println("inside putline")
        val content = request.body 
        val jsonObject = content.asJson
        //println(jsonObject)
        val ml: Option[MatrixLine] = 
            jsonObject.flatMap(
                Json.fromJson[MatrixLine](_).asOpt 
            )
        //println(ml)
        ml match{
            case Some(newLine) =>
                val toBeAdded = newLine.line
                transposeMatrix = newLine.transpose
                cellNamesIn = newLine.cellNames
                geneNamesIn = newLine.geneNames
                matrixAsString = toBeAdded
                var lines = toBeAdded.split('\n')
                //println(geneNamesIn)
                //println(cellNamesIn)
                if(cellNamesIn){
                    cellsNames = lines(0).split(',')
                }

                if(geneNamesIn){
                    //println("inside cells names")
                    val matrixGettingBuild = ListBuffer[Array[String]]()
                    val names = ListBuffer[String]()
                    var incre = 0
                    for(l <- lines){
                        //println("looping")
                        if(cellNamesIn || incre > 0 ){
                            var ll = l.split(',')
                            names += ll(0)
                            //var lineWithoutGene = ll.drop(1)
                            //matrixGettingBuild += ll
                        }else{
                            incre += 1
                        }
                    }
                    //println("finished loop")
                    //matrixAsStringArray = matrixGettingBuild.toArray
                    genesNames = names.toArray
                }
                //println("before transpose")
                if(transposeMatrix){
                    var sub = cellsNames
                    cellsNames = genesNames
                    genesNames = sub
                }
                //println(genesNames.mkString(","))
                //println(cellsNames.mkString(","))
                
                matrix = Utils.buildMatrix(lines, cellNamesIn, geneNamesIn)
                //toBeAdded = null
                matrixAsString = null
                results = ListBuffer[(List[Int], List[Int], Double, List[List[Int]],List[List[Int]])]()
                Created(Json.toJson(transposeMatrix))
            case None =>
                BadRequest
        }
    }


    def get() = Action { implicit request =>
        matrix = matrix.map(l => l.map( d => log10(d+0.1)))
        
        Ok(Json.toJson("Normalized"))

    }

    def getLines() : Action[AnyContent] = Action{
        //println(matrixLines.mkString("\n"))
        if(matrixAsString.isEmpty){
            NoContent
        }else{
            Ok(Json.toJson(matrixAsString))
        }
    }

    /*def buildMatrix() = Action { implicit request => 
        results = ListBuffer[(List[Int], List[Int], Double, List[List[Int]],List[List[Int]])]()
        Ok(Json.toJson(matrix.length))
    }*/

    def normalizeM() = Action { implicit request =>
        matrix = matrix.map(l => l.map( d => log10(d+0.1)))
        
        Ok(Json.toJson("Normalized"))

    }

    def preprocess() = Action { implicit request =>
        //matrix = matrix.map(l => l.map( d => log10(d+0.1)))
        println("after normalize")
        val nSam = matrix.length
        //matrix = matrix.transpose
        println(nSam)
        var superCounter = 0
        var lb = ListBuffer[Int]()
        for (i <- 0 to matrix.length-1){
            var counter = 0
            for(d <- matrix(i)){
                if(d>=0) {
                    counter+=1
                    //println(counter)
                }
            }

            if(counter > 0.25*nSam){
                superCounter +=1
                lb += i
            }
        }
        println(superCounter)
        val toRemove = lb.toList
        
        val lbDouble = ListBuffer[Array[Double]]()
        var j = 0
        //println(toClear.mkString(","))
        //println(m.length)
        for(i <- 0 to matrix.length-1){
            //println(positions.mkString(","))
            if(!(toRemove contains i)){
                lbDouble += matrix(i)
                positions += (j -> i)
                j += 1
            }
        }
        
        procMatrix = lbDouble.toArray

        Ok(Json.toJson(procMatrix.length))
    }

    def reset() = Action { implicit request =>
        matrixAsString = null
        matrix = Array[Array[Double]]()
        normMatrix = Array[Array[Double]]()
        procMatrix = Array[Array[Double]]()
        positions = Map[Int,Int]()
        basicResults = null
        results = ListBuffer[(List[Int], List[Int], Double, List[List[Int]],List[List[Int]])]()
        params = (10,30)
        numberSolve = 0
        Ok(Json.toJson("Done with reset"))
    }
    
    def solve() = Action { implicit request =>
        numberSolve += 1
        println(procMatrix.length)
        //println(procMatrix(0).length)
        basicResults = Solver.findCluster(procMatrix.transpose, nNeg=0.1, kappa=1.02, minMarkersWanted=params._1, maxMarkersWanted=params._2, verbose=false)
        println(results)
        results += basicResults
        Ok(Json.toJson("Solver running"))
    }

    def sendResult():  Action[AnyContent] = Action {
        if(results.length == numberSolve){
            Ok(Json.toJson(results(numberSolve-1)))
        }else{
            Ok(Json.toJson("Results not ready"))
        }
    }

    def sendParams():  Action[AnyContent] = Action {
        Ok(Json.toJson(params))
    }

    def changeParams() = Action {implicit request =>
        val content = request.body
        val jsonObject = content.asJson
        val paramsItem : Option[MatrixParams] =
            jsonObject.flatMap(Json.fromJson[MatrixParams](_).asOpt
            )
        paramsItem match{
            case Some(newParams) =>
                val toBeAdded = MatrixParams(newParams.minMarkers, newParams.maxMarkers)
                //case class MatrixParams(minMarkers: Long, maxMarkers: Long, minCells: Long, maxCells: Long)
                params = (toBeAdded.minMarkers, toBeAdded.maxMarkers)
                Created(Json.toJson(params))
            case None => BadRequest
        }
    }

    def getBicluster(): Action[AnyContent] = Action {
        
        //Need to build bicluster
        //
        //val csvStringRight = rightBiCluster.map{ _.mkString(", ") }.mkString("\n")
        var bicluster  = Utils.buildBicluster(matrix, positions, results(numberSolve-1))
        val csvString = bicluster.map{ _.mkString(", ") }.mkString("\n")
        Ok(Json.toJson(csvString))
    }
    
}