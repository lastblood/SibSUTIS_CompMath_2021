import org.jfree.chart.ChartPanel
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.WindowConstants

class PlotterFrame(title: String, private var chartPanel: ChartPanel): JFrame(title) {
    init {
        size = Dimension(900, 700)
        add(chartPanel)
        pack()
        repaint()
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        isResizable = false
        isVisible = true
    }

    fun rerender(chartPanel: ChartPanel) {
        remove(this.chartPanel)
        add(chartPanel)
        pack()
        repaint()
    }
}