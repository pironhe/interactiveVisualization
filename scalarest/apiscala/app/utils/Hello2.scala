/*package hello

import scala.collection.mutable.{ListBuffer, Map}

import org.scalajs.dom
import org.scalajs.dom.UIEvent
import org.scalajs.dom.raw.HTMLInputElement
import scala.scalajs.js
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.raw.HTMLImageElement
import org.querki.jquery._
import solver.Solver.{findCluster, evaluatePairs}
import solver.Utils.{normalize,preprocess, clearMatrix}

object Hello2 {

    def main(args: Array[String]): Unit = {

        var matrixAsString:String = null
        var matrix: Array[Array[Double]] = Array[Array[Double]]()
        var procMatrix: Array[Array[Double]] = Array[Array[Double]]()
        var positions: Map[Int,Int] = Map[Int,Int]()
        var results= (List(74, 97, 99, 101, 102, 103, 105, 106, 113, 114, 117, 118, 120, 124, 127, 128, 135, 138, 149, 159, 166),List(21313, 51085, 51196, 49442, 49773, 40933, 23870, 21368, 30444, 53167, 51313, 40939, 39237, 46473, 26314, 23268, 23227, 39701, 23229, 26203, 21783, 37120, 33541, 51086, 23228, 25440),473.6077368978774,List(List(105, 107), List(106, 107), List(105, 106), List(102, 103), List(151, 152), List(101, 103), List(96, 97), List(104, 105), List(147, 157), List(117, 118), List(144, 156), List(154, 155), List(103, 114), List(146, 157), List(101, 114), List(121, 133), List(7, 19), List(103, 117), List(101, 112), List(101, 125)),List(List(21, 33, 734), List(21, 32, 651), List(21, 32, 556), List(21, 26, 473), List(21, 26, 473), List(21, 26, 473)))
        
        $("#load-button").click{() =>
            val fileSelector = dom.document.getElementById("file-selector").asInstanceOf[HTMLInputElement]
            
            val file = fileSelector.files(0)

            val reader = new dom.FileReader()
            
            reader.onload = (e: UIEvent) =>{
                matrixAsString = reader.result.toString
                val lines = matrixAsString.split("\n")
                matrix = Array.ofDim[Double](lines.length, lines(0).split("\n").length)
                var incr = 0
                for(l <- lines){
                    val splitLine = l.split(",")
                    val lineDouble: ListBuffer[Double] = ListBuffer[Double]()
                    for(str <- splitLine){
                        lineDouble += str.toDouble
                    }
                    val numberLine = lineDouble.toArray
                    matrix(incr) = numberLine
                    incr +=1
                }
                println("done")
            }
            reader.readAsText(file)

        }

        $("#matrix-button").click{() =>
            //Normalize data matrix
            //matrix = normalize(matrix)
            println(matrix.length)
            println(matrix(0).length)
            // Preprocess matrix and run findcluster
            val toRem = preprocess(matrix, 0.25)
            val res = clearMatrix(matrix, toRem)
            procMatrix = res._1.transpose
            positions = res._2
            println(procMatrix.length)
            println(procMatrix(0).length)
            //run find cluster
            var kappa = 100.0/(procMatrix.length.toDouble)
            var mu = 0.1
            //results = findCluster(procMatrix, kappa=kappa, nNeg=mu, verbose=true)
            //println(results)
            println("kappa = " + kappa.toString)
            println("done")
        }

        $("#print-button").click{() =>
            println(matrix.length)
            println(matrix(0).length)
            //println(results)
            /*
            (List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 54, 65, 76, 80, 115, 171),List(23, 21, 9, 19),266.26754343154266,List(List(0, 2), List(0, 3), List(0, 4), List(0, 5), List(0, 6), List(0, 7), List(0, 8), List(0, 9), List(0, 10), List(0, 11), List(0, 12), List(0, 13), List(0, 14), List(0, 15), List(0, 16), List(0, 17), List(0, 18), List(0, 19), List(0, 20), List(198, 199)),List(List(31, 4, 266), List(31, 4, 339), List(30, 5, 389), List(30, 5, 464), List(30, 5, 525), List(30, 5, 573), List(30, 5, 612), List(30, 5, 642), List(30, 5, 667), List(30, 5, 687), List(16, 1, 70), List(16, 1, 70), List(16, 1, 70), List(16, 1, 70), List(30, 5, 740), List(30, 5, 745), List(30, 5, 750), List(30, 5, 753), List(30, 5, 756), List(30, 5, 758), List(30, 5, 759), List(30, 5, 761), List(30, 5, 762), List(30, 5, 763), List(30, 5, 763)))
            (List(74, 97, 99, 101, 102, 103, 105, 106, 113, 114, 117, 118, 120, 124, 127, 128, 135, 138, 149, 159, 166),List(21313, 51085, 51196, 49442, 49773, 40933, 23870, 21368, 30444, 53167, 51313, 40939, 39237, 46473, 26314, 23268, 23227, 39701, 23229, 26203, 21783, 37120, 33541, 51086, 23228, 25440),473.6077368978774,List(List(105, 107), List(106, 107), List(105, 106), List(102, 103), List(151, 152), List(101, 103), List(96, 97), List(104, 105), List(147, 157), List(117, 118), List(144, 156), List(154, 155), List(103, 114), List(146, 157), List(101, 114), List(121, 133), List(7, 19), List(103, 117), List(101, 112), List(101, 125)),List(List(21, 33, 734), List(21, 32, 651), List(21, 32, 556), List(21, 26, 473), List(21, 26, 473), List(21, 26, 473)))
            */
        }

        $("#cluster-button").click{() =>
            val indexes = results._1
            val markers = results._2
            
            val lb = ListBuffer[Array[Double]]()

            for(mark <- markers){
                lb += matrix(positions(mark))
            }

            val cluster = lb.toArray
            println(cluster.length)
            println(cluster(0).length)
            println(cluster.mkString(","))
            val notIndexes: List[Int] = (0 to 167).toList.filter(!indexes.contains(_))
            //println(notIndexes.mkString(","))
            val bicluster = ListBuffer[Array[Double]]()
            val notb = ListBuffer[Array[Double]]()
            for(mark <- cluster){
                val line = ListBuffer[Double]()
                for(notIndex <- notIndexes){
                    line += mark(notIndex)
                }
                for(index <- indexes){
                    line += mark(index)
                }
                bicluster += line.toArray
            }
            
            var rightBiCluster = bicluster.toArray
            println(rightBiCluster.length)
            println(rightBiCluster(0).length)

            val csvStringRight = rightBiCluster.map{ _.mkString(", ") }.mkString("\n")

            println(csvStringRight)
        }

        $("#testing-button").click{() =>
            var mtest = Array(Array(0.0,1.0),Array(1.0,1.0),Array(2.0,1.0),Array(3.0,1.0),Array(4.0,1.0),Array(5.0,1.0),Array(6.0,1.0),Array(7.0,1.0))
            val toRem = List(2,3,5)
            
            val res = clearMatrix(mtest,toRem)

            println(res._2.toArray.mkString(","))
            res._1.map( ar => println(ar.mkString(",")))
            //cluster.map(l => l.map(_ + 1).mkString(", "))

            
        }

        /*$("#clear-button").click{() =>
            val canvas = dom.document.createElement("canvas").asInstanceOf[Canvas]
            val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
            canvas.width = (0.95 * dom.window.innerWidth).toInt
            canvas.height = (0.95 * dom.window.innerHeight).toInt
            dom.document.body.appendChild(canvas)
            /*ctx.fillRect(25, 25, 100, 100);
            ctx.clearRect(45, 45, 60, 60);
            ctx.strokeRect(50, 50, 50, 50);*/



            
        }*/

    }

}*/