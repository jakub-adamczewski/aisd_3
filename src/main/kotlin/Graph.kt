class Graph(private val nodesNumber: Int) {

    private var list: MutableList<MutableList<Int>> = MutableList(nodesNumber) { mutableListOf<Int>() }

    private lateinit var copyList: MutableList<MutableList<Int>>

    fun addEdge(v1: Int, v2: Int) {
        list[v1].add(v2)
        list[v2].add(v1)
    }

    private fun removeEdge(v1: Int, v2: Int) {
        list[v1].remove(v2)
        list[v2].remove(v1)
    }

    fun areConnected(node1: Int, node2: Int): Boolean{
        return list[node1].contains(node2) && list[node2].contains(node1)
    }

    fun print() {
        var nodeNumber = 0
        list.forEach {
            println("Adjacency list of node $nodeNumber: $it")
            nodeNumber++
        }
    }

    fun dfs(v: Int) {
        val visited = Array(nodesNumber) { false }
        dfsUtil(v, visited)
    }

    private fun dfsUtil(v: Int, visited: Array<Boolean>) {
        visited[v] = true
        print("$v ")
        list[v].forEach { if (!visited[it]) dfsUtil(it, visited) }
    }

    fun printEulerianCycle() {
        println("Eulerian cycle: ")
        if (this.hasEulerianCycle()) {
            val startNode = 0
            print("${startNode + 1} ")
            saveListAction { printEulerUtil(startNode) }
            println()
        } else println("Graph has no Eulerian cycle.")
    }

    private fun hasEulerianCycle(): Boolean {
        val oddDegreeNodesNumber = list.filter { it.size % 2 != 0 }.size
        return oddDegreeNodesNumber == 0
    }

    private fun printEulerUtil(u: Int) {
        for (i in list[u].indices) {
            if (list[u].size <= i) break
            val v = list[u][i]
            if (isValidNextEdge(u, v)) {
//                print(" $u - $v |")
                print("${v + 1 } ")

                removeEdge(u, v)
                printEulerUtil(v)
            }
        }

    }

    private fun isValidNextEdge(u: Int, v: Int): Boolean {
        if (list[u].size == 1) return true

        var isVisited = Array(nodesNumber) { false }
        val count1 = dfsCount(u, isVisited)

        removeEdge(u, v)
        isVisited = Array(nodesNumber) { false }
        val count2 = dfsCount(u, isVisited)

        addEdge(u, v)
        return count1 <= count2
    }

    private fun dfsCount(v: Int, isVisited: Array<Boolean>): Int {
        isVisited[v] = true
        var count = 1

        for (node in list[v]) {
            if (!isVisited[node]) {
                count += dfsCount(node, isVisited)
            }
        }

        return count
    }

    @ExperimentalStdlibApi
    fun printHamiltonianCycle(allCycles: Boolean){
        println("Hamiltonian cycles: ")
        val startNode = 0

        val path = mutableListOf(startNode)
        val visited = Array(nodesNumber){false}
        visited[startNode] = true
        printHamiltonianCycleUtil(startNode, visited, path, allCycles)
    }

    var counter = 0

    @ExperimentalStdlibApi
    private fun printHamiltonianCycleUtil(v: Int, visited: Array<Boolean>, path: MutableList<Int>, allCycles: Boolean){
        if(path.size == nodesNumber && list[path.last()].contains(0)){
            print("${++counter}: ")
            path.forEach { print("${it + 1} ") }
            println()
            return
        }

        for(w in list[v]){
            if(!visited[w]){
                visited[w] = true
                path.add(w)
                printHamiltonianCycleUtil(w, visited, path, allCycles)

                if(allCycles){
                    visited[w] = false
                    path.removeLast()
                }
            }
        }
    }

    private fun MutableList<MutableList<Int>>.copy(): MutableList<MutableList<Int>> {
        val newList = MutableList(nodesNumber) { mutableListOf<Int>() }
        for (i in this.indices) {
            newList[i].addAll(list[i])
        }
        return newList
    }

    fun testListsCopy() {
        val copyList = list.copy()
        list[2].clear()
        println(list)
        println(copyList)
    }

    private fun saveListAction(action: () -> Unit) {
        copyList = list.copy()
        action()
        list = copyList
    }

    fun getEdgesNumber(): Long {
        var number: Long = 0
        list.forEach { nodeList -> number += nodeList.size }
        return number / 2
    }

    fun getNodesNumber(): Int {
        return list.size
    }

    fun getNodeDegree(node: Int): Int {
        return list[node].size
    }
}
