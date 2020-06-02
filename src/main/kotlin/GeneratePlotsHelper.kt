import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.layout.Type
import scientifik.plotly.trace
import kotlin.system.measureTimeMillis

val firstTaskGraphsSizes = listOf(
    7,
    13,
    19,
    25,
    31,
    37,
    43,
    49,
    55,
    61,
    67,
    73,
    79,
    85,
    91,
    97,
    103,
    109
)

val secondTaskGraphSizes = listOf(
    2,
    3,
    4,
    5,
    6,
    7,
    8,
    9,
    10,
    11,
    12,
    13,
    14,
    15,
    16
)

@ExperimentalStdlibApi
fun generatePlots() {
//    val thirtyPercentCompleteGraphsInstances = firstTaskGraphsSizes.map { size -> generateEulerianGraph(size, 0.3F) }
//    generateFirstTaskPlot(firstTaskGraphsSizes, thirtyPercentCompleteGraphsInstances, GraphCompletionPercentage.THIRTY)
//
//    val seventyPercentCompleteGraphsInstances = firstTaskGraphsSizes.map { size -> generateEulerianGraph(size, 0.7F) }
//    generateFirstTaskPlot(firstTaskGraphsSizes, seventyPercentCompleteGraphsInstances, GraphCompletionPercentage.SEVENTY)


    val fiftyPercentCompleteGraphsInstances = secondTaskGraphSizes.map { size -> generateEulerianGraph(size, 0.5F) }
    generateSecondTaskPlot(secondTaskGraphSizes, fiftyPercentCompleteGraphsInstances, GraphCompletionPercentage.FIFTY)
}

@ExperimentalStdlibApi
private fun generateFirstTaskPlot(
    x: List<Int>,
    graphInstances: List<Graph>,
    graphCompletion: GraphCompletionPercentage
) {
    val hamiltonCycleTimesY = mutableListOf<Long>()
    val eulerianCycleTimesY = mutableListOf<Long>()

    graphInstances.forEach { graphInstance ->
        hamiltonCycleTimesY.add(measureTimeMillis { graphInstance.printHamiltonianCycle(false) })
        eulerianCycleTimesY.add(measureTimeMillis { graphInstance.printEulerianCycle() })
    }

    val plot = Plotly.plot2D {
        trace(x, hamiltonCycleTimesY) {
            name = "times of searching hamiltonian cycles"
        }
        trace(x, eulerianCycleTimesY) {
            name = "times of searching eulerian cycles"
        }

        layout {
            title =
                "Comparison of searching times of hamiltonian and eulerian cycles in ${graphCompletion.percentageNumber}% complete graph"
            xaxis {
                title = "graphs sizes"
            }
            yaxis {
                title = "times of searching cycles (ms)"
            }
        }
    }
    plot.makeFile()
}

@ExperimentalStdlibApi
private fun generateSecondTaskPlot(
    x: List<Int>,
    graphInstances: List<Graph>,
    graphCompletion: GraphCompletionPercentage
) {
    val allHamiltonCyclesTimesY = mutableListOf<Long>()

    graphInstances.forEach { graphInstance ->
        allHamiltonCyclesTimesY.add(measureTimeMillis { graphInstance.printHamiltonianCycle(true) })
    }

    val plot = Plotly.plot2D {
        trace(x, allHamiltonCyclesTimesY) {
            name = "Times of searching hamiltonian cycles (ms)"
        }

        layout {
            title =
                "Times of searching all hamiltonian cycles in ${graphCompletion.percentageNumber}% complete graph"
            xaxis {
                title = "Graphs sizes"
            }
            yaxis {
                title = "times of searching cycles (ms)"
            }
        }
    }
    plot.makeFile()
}


private enum class GraphCompletionPercentage(val percentageNumber: Int) {
    THIRTY(30), FIFTY(50), SEVENTY(70)
}