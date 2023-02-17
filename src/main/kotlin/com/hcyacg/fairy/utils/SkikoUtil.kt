package com.hcyacg.fairy.utils

import org.jetbrains.skia.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


/**
 * @Author Nekoer
 * @Date  2/12/2023 11:21
 * @Description
 **/
@Component
class SkikoUtil {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var fontUtil: FontUtil

    fun textToImage(text: String): Surface {
        log.debug("文转图 => {}",text)
        val split = text.split("\n")
        val textArray = mutableListOf<TextLine>()

        val font = Font(fontUtil.matchArial(FontStyle.BOLD), 20F)

        split.forEach {
            textArray.add(TextLine.make(it, font))
        }



        val lightYellow = Paint().setARGB(0xFF, 255, 255, 224)
        val white = Paint().setARGB(0xFF, 0xFF, 0xFF, 0xFF)
        val black = Paint().setARGB(0xFF, 0x00, 0x00, 0x00)
        val darkGrey = Paint().setARGB(0xFF, 105,105,105)
        //棕褐色
        val tan = Paint().setARGB(0xFF, 210,180,140)

        var height = 0F
        var width = 0F
        textArray.forEach {
            height += it.height

            if (it.width >= width){
                width = it.width
            }
        }


        val surface = Surface.makeRasterN32Premul((width + 16).toInt(), (height + 14).toInt())
        surface.canvas.apply {
            //背景色
            clear(lightYellow.color)
            //线
            //drawRRect(RRect.makeXYWH(6F, 6F, width + 2F, height + 2F, 3F), darkGrey)
            //输入区
            drawRRect(RRect.makeXYWH(7F, 7F, width, height, 3F), lightYellow)

            var tempY = 10F
            textArray.forEach {
                drawTextLine(it, 8F, tempY - font.metrics.ascent, tan)
                tempY += it.height
            }
//            drawRRect(RRect.makeXYWH(prefix.width + 15, 15F, suffix.width + 20, suffix.height + 10, 10F), yellow)
//            drawTextLine(suffix, prefix.width + 25, 20 - font.metrics.ascent, black)
        }

        return surface

    }


}
