import java.io.File
import java.io.InputStream

@ExperimentalStdlibApi
fun main() {
    generatePlots()
}

fun getMatrixFromFile(): MutableList<MutableList<Int>> {
    val inputStream: InputStream = File("D:\\studia\\2 semestr\\testGraph1.txt").inputStream()
    val lineList = mutableListOf<String>()
    inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it) } }

    val intMatrix = mutableListOf<MutableList<Int>>()
    lineList.forEach { line ->
        intMatrix.add(mutableListOf())
        for (char in line) {
            if (char != ' ') intMatrix.last().add(Character.getNumericValue(char))
        }
    }

    for (line in intMatrix){
        println(line)
    }
    println()

    return intMatrix
}

fun graphFromIncidenceMatrix(matrix: MutableList<MutableList<Int>>): Graph {
    val localMatrix: MutableList<MutableList<Int>> = matrix
    val size = localMatrix.size
    val graph = Graph(size)

    for (i in 0 until size){
        for(j in 0 until size){
            if(localMatrix[i][j] == 1){
                graph.addEdge(i, j)
                localMatrix[i][j] = 0
                localMatrix[j][i] = 0
            }
        }
    }

    return graph
}
