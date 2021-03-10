package ru.smak.graphics

import java.awt.*
import java.awt.geom.AffineTransform
import java.util.Collections.rotate
import kotlin.math.*

class GraphPainter(
        val graph: MutableList<MutableList<Double>>
) : Painter {

    private var width = 1
    private var height = 1
    var thickness = 1
        set(value) {
            if (value >=1 && value <= 30) {
                field = value
                calcVertexPositions()
            }
        }

    var vertexSize = 30
        set(value){
            if (value >=10 && value <= 100) {
                field = value
                calcVertexPositions()
            }
        }

    override var size: Dimension
    get() = Dimension(width, height)
    set(value){
        width = value.width
        height = value.height
        calcVertexPositions()
    }

    private val minSz: Int
        get() = min(width, height) - vertexSize - thickness

    private val rect: Rectangle
        get() = Rectangle((width - minSz)/2, (height-minSz)/2, minSz, minSz)

    private val radius: Int
        get() = minSz / 2


    private val center: Point
        get() = Point(rect.x + radius, rect.y + radius)

    private val phi: Double
        get() = 2 * PI / graph.size

    private var vertexPositions: MutableList<Point>? = null

    private fun calcVertexPositions(){
        vertexPositions = MutableList<Point>(graph.size) { i ->
            Point((center.x + radius * cos(i * phi)).toInt() ,
                    (center.y + radius * sin(i * phi)).toInt()
            )
        }
    }

    override fun paint(g: Graphics){
        paintEdges(g)
        paintVerticies(g)
        paintNumbers(g)
        paintP(g)
    }

    private fun paintVerticies(g: Graphics) {
        (g as Graphics2D).apply {
            rotate(-PI / 2, center.x.toDouble(), center.y.toDouble())
            vertexPositions?.forEach {
                g.color = Color.WHITE
                g.fillOval(it.x - vertexSize / 2, it.y - vertexSize / 2, vertexSize, vertexSize)
                g.color = Color.BLUE
                g.drawOval(it.x - vertexSize / 2, it.y - vertexSize / 2, vertexSize, vertexSize)
                //g.drawString(vertexPositions!!.indexOf(it).toString(),it.x ,it.y )
            }
        }
    }

    private fun paintNumbers(g:Graphics){
        (g as Graphics2D).apply {
            rotate(PI/2 , center.x.toDouble(), center.y.toDouble())
            (g as Graphics2D).apply {
                vertexPositions?.forEach {
                    g.rotate(PI/2, ((it.x - vertexSize/8).toDouble()) ,((it.y + vertexSize/4).toDouble()))
                    g.drawString(vertexPositions!!.indexOf(it).toString(),it.x - vertexSize/2 ,it.y + vertexSize/4 )
                    g.rotate(-PI/2, ((it.x - vertexSize/8).toDouble()) ,((it.y + vertexSize/4).toDouble()))
                }
            }
            rotate(-PI/2 , center.x.toDouble(), center.y.toDouble())
        }
    }

    private fun paintP(g:Graphics){
        (g as Graphics2D).apply {
            rotate( PI/2 , center.x.toDouble(), center.y.toDouble())
            (g as Graphics2D).apply {
                graph.forEachIndexed { fromInd, from ->
                    from.takeLast(graph.size - fromInd - 1)
                            .forEachIndexed { toInd, weight ->
                                if (weight > 1e-20) {
                                    vertexPositions?.let { vPos ->
                                        val toI = toInd + fromInd + 1
                                        val fi = Math.abs(Math.atan(
                                                ( vPos[toI].y - vPos[fromInd].y).toDouble()/(vPos[toI].x- vPos[fromInd].x + vPos[toI].x).toDouble()
                                        )) + PI/2
                                        g.rotate( fi, ((vPos[fromInd].x + vPos[toI].x)/2+ 2).toDouble(), ((vPos[fromInd].y + vPos[toI].y)/2 -2).toDouble())
                                        g.drawString( graph[fromInd][toI].toInt().toString() ,
                                                (vPos[fromInd].x + vPos[toI].x)/2+ 2,
                                                (vPos[fromInd].y + vPos[toI].y)/2 - 4)
                                        g.rotate( -fi, ((vPos[fromInd].x + vPos[toI].x)/2+ 2).toDouble(), ((vPos[fromInd].y + vPos[toI].y)/2 -2).toDouble())
                                    }
                                }
                            }
                }
            }
        }
    }

    private fun paintEdges(g: Graphics) {
        (g as Graphics2D).apply {
            rotate(-PI / 2, center.x.toDouble(), center.y.toDouble())
            stroke = BasicStroke(
                    thickness.toFloat(),
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND)
            setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            )
        }

        graph.forEachIndexed { fromInd, from ->
            from.takeLast(graph.size - fromInd - 1)
                    .forEachIndexed { toInd, weight ->
                if (weight > 1e-20) {
                    vertexPositions?.let { vPos ->
                        val toI = toInd + fromInd + 1
                        g.drawLine(
                                vPos[fromInd].x, vPos[fromInd].y,
                                vPos[toI].x, vPos[toI].y
                        )
                    }
                }
            }
        }

    }
}