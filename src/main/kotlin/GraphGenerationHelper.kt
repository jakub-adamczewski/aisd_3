import kotlin.random.Random

fun generateEulerianGraph(size: Int, completionFactor: Float): Graph {
    val graph = Graph(size)
    val edgesNumber = size * (size - 1) / 2
    var maxEdgesNumber = (edgesNumber * completionFactor).toInt()

    //First all nodes will be connected in "circle" - 0 to 1, 1 to 2, .... , and last one to 0
    for (i in 0 until size - 1) {
        graph.addEdge(i, i + 1)
        maxEdgesNumber--
    }
    graph.addEdge(size - 1, 0)
    maxEdgesNumber--

    //Code will select 3 nodes without connection to each other and add edges between them (like triangles) so that every node will still have even degree
    while (maxEdgesNumber > 0) {
        val nodesTriple = pickThreeNodesWithoutConnection(graph)

        graph.addEdge(nodesTriple.first, nodesTriple.second)
        graph.addEdge(nodesTriple.second, nodesTriple.third)
        graph.addEdge(nodesTriple.third, nodesTriple.first)

        maxEdgesNumber -= 3
    }
    return graph
}

private fun pickThreeNodesWithoutConnection(graph: Graph): Triple<Int, Int, Int> {
    val maxNode = graph.getNodesNumber() - 1

    var firstNode = Random.nextInt(0, graph.getNodesNumber())
    while (graph.getNodeDegree(firstNode) == maxNode) firstNode = Random.nextInt(0, graph.getNodesNumber())

    var secondNode: Int = incrementNodePointer(firstNode, maxNode)
    while (graph.areConnected(firstNode, secondNode)) secondNode = incrementNodePointer(secondNode, maxNode)

    var thirdNode: Int = incrementNodePointer(secondNode, maxNode)
    while (graph.areConnected(secondNode, thirdNode) || graph.areConnected(thirdNode, firstNode)) thirdNode = incrementNodePointer(thirdNode, maxNode)

    return Triple(firstNode, secondNode, thirdNode)
}

private fun incrementNodePointer(nodePointer: Int, maxNodePointer: Int): Int {
    return if (nodePointer == maxNodePointer) 0
    else nodePointer + 1
}