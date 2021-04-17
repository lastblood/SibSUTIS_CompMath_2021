import org.jfree.chart.ChartFactory.createXYLineChart
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.Range
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.Color


class Plotter(plotter: Plotter? = null) {
    private val dataset: XYSeriesCollection = plotter?.dataset ?: XYSeriesCollection()
    private val renderer: XYLineAndShapeRenderer = plotter?.renderer ?: XYLineAndShapeRenderer()
    private var seriesIndex: Int = plotter?.seriesIndex ?: 0

    fun addLine(pairs: List<Pair<Double, Double>>): Plotter {
        return addCustom(pairs) { renderer, index ->
            renderer.setSeriesLinesVisible(index, true)
            renderer.setSeriesShapesVisible(index, false)
        }
    }

    fun addDots(pairs: List<Pair<Double, Double>>): Plotter {
        return addCustom(pairs) { renderer, index ->
            renderer.setSeriesLinesVisible(index, false)
            renderer.setSeriesShapesVisible(index, true)
        }
    }

    fun removeLast(): Plotter {
        dataset.removeSeries(--seriesIndex)
        return this
    }

    fun removeAll(): Plotter {
        dataset.removeAllSeries()
        return this
    }

    fun addCustom(pairs: List<Pair<Double, Double>>, operation: (XYLineAndShapeRenderer, Int) -> Unit): Plotter {
        val thisIndex = synchronized(this) { seriesIndex++ }

        operation(renderer, thisIndex)

        val series = XYSeries(thisIndex, true)
        pairs.forEach { series.add(it.x, it.y) }
        dataset.addSeries(series)

        return this
    }

    fun getChart(title: String = "", x: String = "", y: String = "", fixed: Boolean = false): JFreeChart {
        val chart = createXYLineChart(title, x, y, dataset,
                PlotOrientation.VERTICAL, true, true, false)

        val plot = chart.xyPlot

        plot.renderer = renderer
        plot.backgroundPaint = Color.white
        plot.rangeGridlinePaint = Color.BLACK
        plot.domainGridlinePaint = Color.BLACK

        if(fixed) {
            plot.rangeAxis.range = Range(-10.0, 10.0)
        }

        return chart
    }
}
