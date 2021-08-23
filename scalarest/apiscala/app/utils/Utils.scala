package utils


import scala.collection.mutable.{ListBuffer, Map}
import scala.math.log10

object Utils{

    def buildMatrix(matrixAsStringArray: Array[String], skipLine: Boolean, skipFirstElem: Boolean): Array[Array[Double]] = {
        //var matrix: Array[Array[Double]] = Array[Array[Double]]()
        //val lines = matrixAsStringArray
        var firstLine = matrixAsStringArray(0).split(',')
        var lineLength = firstLine.length
        if(skipFirstElem){
            lineLength = lineLength-1
        }
        var matrix = Array.ofDim[Double](matrixAsStringArray.length, lineLength)
        var incr = 0
        println(matrixAsStringArray.length)
        var skip = skipLine
        for(l <- matrixAsStringArray){
            if(skip){
                skip = false
            }else{
                val lineDouble: ListBuffer[Double] = ListBuffer[Double]()
                for(str <- l){
                    lineDouble += str.toDouble
                }
                val numberLine = lineDouble.toArray
                matrix(incr) = numberLine
                incr +=1
            }
        }
        return matrix

    }

    def normalize(m: Array[Array[Double]]): Array[Array[Double]] = {
        return m.map(l => l.map( d => log10(d+0.1)))
    }

    def preprocess(m: Array[Array[Double]], thresh: Double = 0.25): List[Int] = {
        val nSam = m(0).length
        val mt = m.transpose
        println(m.length)
        var superCounter = 0
        var lb = ListBuffer[Int]()
        for (i <- 0 to m.length-1){
            var counter = 0
            for(d <- m(i)){
                if(d>=0) {counter+=1}
            }

            if(counter > thresh*nSam){
                superCounter +=1
                lb += i
            }
        }
        println(superCounter)
        val toRemove = lb.toList
        return toRemove
    }

    def clearMatrix(m: Array[Array[Double]], toClear: List[Int]): (Array[Array[Double]], Map[Int,Int]) = {
        
        val positions: Map[Int,Int] = Map[Int,Int]()
        val lb = ListBuffer[Array[Double]]()
        var j = 0
        //println(toClear.mkString(","))
        //println(m.length)
        for(i <- 0 to m.length-1){
            //println(positions.mkString(","))
            if(!(toClear contains i)){
                lb += m(i)
                positions += (j -> i)
                j += 1
            }
        }
        
        return (lb.toArray,positions)

    }

    def buildBicluster(matrix: Array[Array[Double]], positions: Map[Int,Int], results : (List[Int], List[Int], Double, List[List[Int]],List[List[Int]])): Array[Array[Double]] = {

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
        return bicluster.toArray
        


    }
}