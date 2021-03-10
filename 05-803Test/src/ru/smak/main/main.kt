import ru.smak.graphics.GraphPainter
import ru.smak.io.Loader
import ru.smak.ui.MainWindow

fun main() {
    val data = Loader.load("graph2.csv")
    val p = GraphPainter(data).apply {
        thickness = 3
    }

    with (MainWindow()){
        painter = p
        isVisible = true
    }

}