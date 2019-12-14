import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.Tooltip
import javafx.stage.Stage
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintStream
import javax.imageio.ImageIO


public class MultiThreads() : Application() {
    var THREADS_COUNT = 400;


    override fun start(stage: Stage) {
        val filename = "src/resources/vrubel1.jpg"
        System.setErr(PrintStream(File("log.txt")))
        val picture = ImageIO.read(File(filename))
        val width = picture.getWidth()
        val height = picture.getHeight()
        println("$width,$height")
        /*    for (i in 0..THREADS_COUNT){
            var itemsCount = width*height/THREADS_COUNT
            if(i==THREADS_COUNT) itemsCount += width*height%THREADS_COUNT

        }
*/
        var sync = Synchronization(null, picture, 0)
        for (i in 1..THREADS_COUNT + 1)
            sync.calculate(i)


        val outputfile = File("saved.png")
        ImageIO.write(picture, "png", outputfile)

        val xAxis = NumberAxis(0.0, 401.0, 10.0)
        xAxis.label = "# of threads"


        val yAxis = NumberAxis(120.0, 200.0, 0.2)
        yAxis.label = "Milliseconds"
        var linechart = LineChart(xAxis,yAxis)
        linechart.setPrefSize(800.toDouble(),600.toDouble())

        var series = Series<Number,Number>()

        var reader = BufferedReader(FileReader("log.txt"))
        var file = File("log.txt")
            file.forEachLine {
            val string = it.split(" ")
            series.data.add(XYChart.Data(string[0].toDouble(),string[1].toDouble()))
        }

        linechart.data.add(series)

        series.data.forEach{
        var t = Tooltip(it.yValue.toString())
            Tooltip.install(it.node,t)
        }



        var group = Group(linechart)
        var scene = Scene(group,800.toDouble(),600.toDouble())
        stage.scene = scene
        stage.show()

    }

    companion object Main {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(MultiThreads::class.java)
        }
    }

}

