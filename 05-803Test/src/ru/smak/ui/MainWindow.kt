package ru.smak.ui

import ru.smak.ui.components.GraphicsPanel
import ru.smak.graphics.Painter
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.GroupLayout
import javax.swing.JFrame

class MainWindow : JFrame(){

    private val mainPanel: GraphicsPanel
    var painter: Painter?
        get() = mainPanel.painter
        set(value){
            mainPanel.painter = value
        }

    init{
        defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = Dimension(600, 500)
        mainPanel = GraphicsPanel().apply {
            background = Color.WHITE
        }

        layout = GroupLayout(contentPane).also {
            it.setHorizontalGroup(
                    it.createSequentialGroup()
                            .addGap(8)
                            .addComponent(mainPanel, minimumSize.width - 60, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                            .addGap(8)
            )
            it.setVerticalGroup(
                    it.createSequentialGroup()
                            .addGap(8)
                            .addComponent(mainPanel, minimumSize.height - 60, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                            .addGap(8)
            )
        }
        mainPanel.addComponentListener(object: ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                painter?.size = mainPanel.size
            }
        })
        pack()
    }

    override fun paint(g: Graphics?) {
        super.paint(g)
    }
}