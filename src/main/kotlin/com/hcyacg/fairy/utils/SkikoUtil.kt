package com.hcyacg.fairy.utils

import org.jetbrains.skia.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import kotlin.math.ceil


/**
 * @Author Nekoer
 * @Date  2/12/2023 11:21
 * @Description 文转图
 **/
@Component
class SkikoUtil {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var fontUtil: FontUtil

    private val background = ClassPathResource("static/background.png")
    private val banner = ClassPathResource("static/banner.png")

    fun textToImage(title: String?, lrc: String): Surface {
        val userFontSize = 45F
        val lrcFontSize = 30F
        val lineSpace = 30F
        val lrcLineSpace = 15F
        var shareImageWidth = 1080F
        val borderColor = Paint().setARGB(0xFF, 220, 211, 196)
        val textColor = Paint().setARGB(0xFF, 125, 101, 89)

        val outPadding = 30F
        val padding = 45F


        val font = Font(fontUtil.matchArial(FontStyle.NORMAL), userFontSize)

        val miImage = Image.makeFromEncoded(background.inputStream.readAllBytes())
        val miBanner = Image.makeFromEncoded(banner.inputStream.readAllBytes())
        val bannerSize = miBanner.width.toFloat()
//        val surfaceBanner = Surface.makeRasterN32Premul(miBanner.width,miBanner.height)
//        surfaceBanner.canvas.translate().res


        var lrcRows = 0


        val split = lrc.split("\n")
        val textArray = mutableListOf<TextLine>()


        split.forEach {
            textArray.add(TextLine.make(it, font))
            lrcRows += 1
        }
        var height = 0F
        var width = 0F
        textArray.forEach {
            height += it.height

            if (it.width > shareImageWidth) {
                width = it.width
            }
        }
        shareImageWidth += width

        val innerH = title?.let {
            (padding * 2 + userFontSize + lineSpace + lrcFontSize * lrcRows + (lrcRows - 1) * lrcLineSpace)
        } ?: (padding * 2 + lrcFontSize * lrcRows + (lrcRows - 1) * lrcLineSpace)


        //val h = outPadding * 2 + innerH
        val h = height + outPadding * 2 + padding * 2


        val surface = Surface.makeRasterN32Premul(shareImageWidth.toInt(), h.toInt())
        surface.canvas.apply {
            //画背景
            for (x in 0 until ceil(h / 100).toInt()) {
                for (y in 0 until ceil(shareImageWidth / 1080).toInt()){
                    drawImage(miImage, (y * 1080).toFloat(), (x * 100).toFloat())
                }
                drawImage(miImage, 0F, (x * 100).toFloat())
            }

            //画四周边框
            for (i in 0 until 2) {
                //画矩形
                //上边框
                drawLine(
                    outPadding + i,
                    outPadding + i,
                    shareImageWidth - outPadding - i,
                    outPadding + i,
                    borderColor
                )
                //左边框
                drawLine(
                    outPadding + i,
                    outPadding + i,
                    outPadding + i,
                    h - outPadding - i,
                    borderColor
                )
                //右边框
                drawLine(
                    shareImageWidth - outPadding - i,
                    outPadding - i,
                    shareImageWidth - outPadding - i,
                    h - outPadding - i,
                    borderColor
                )
                //下边框
                drawLine(
                    outPadding + i,
                    h - outPadding - i,
                    shareImageWidth - outPadding - i,
                    h - outPadding - i,
                    borderColor
                )
            }
            //左上角
            drawImage(miBanner, outPadding, outPadding)
            //drawImage(miBanner,outPadding,h-outPadding -bannerSize+ 1)
            //drawImage(miBanner,shareImageWidth-outPadding -bannerSize+1,outPadding)
            //drawImage(miBanner,shareImageWidth-outPadding -bannerSize+1,h-outPadding-bannerSize + 1)
            //水平翻转 右上角
            drawImage(
                rotate(miBanner, 90F).makeImageSnapshot(),
                shareImageWidth - outPadding - bannerSize + 1,
                outPadding
            )
            //水平垂直翻转 右下角
            drawImage(
                rotate(miBanner, 180F).makeImageSnapshot(),
                shareImageWidth - outPadding - bannerSize + 1,
                h - outPadding - bannerSize + 1
            )
            //垂直翻转 左下角
            drawImage(rotate(miBanner, 270F).makeImageSnapshot(), outPadding, h - outPadding - bannerSize + 1)


            title?.let {
                drawTextLine(TextLine.make(it, font), shareImageWidth - 1 / 2, outPadding + padding, textColor)
                var tempY = outPadding + padding + userFontSize + lineSpace
                textArray.forEach { line ->
                    drawTextLine(
                        line,
                        outPadding + padding,
                        tempY - font.metrics.ascent,
                        textColor
                    )
                    tempY += line.height
                }
            } ?: kotlin.run {
                var tempY = outPadding + padding
                textArray.forEach { line ->
                    run {
                        log.debug("tempY => {}", tempY)
                        drawTextLine(line, outPadding + padding, tempY - font.metrics.ascent, textColor)
                        log.debug("line.height => {}", line.height)
                        tempY += line.height
                    }
                }
            }


        }
        return surface
    }

    fun rotate(image: Image, rotate: Float): Surface {
        val surface = Surface.makeRasterN32Premul(image.width, image.height)
        surface.canvas.apply {
            save()
            translate(image.width.toFloat(), image.height.toFloat())
            rotate(rotate)
            drawImage(image, 0F, 0F)

            restore()
        }

        return surface
    }

    fun textToImage(text: String): Surface {
        log.debug("文转图 => {}", text)
        val split = text.split("\n")
        val textArray = mutableListOf<TextLine>()

        val font = Font(fontUtil.matchArial(FontStyle.BOLD), 20F)

        split.forEach {
            textArray.add(TextLine.make(it, font))
        }


        val lightYellow = Paint().setARGB(0xFF, 255, 255, 224)
        val white = Paint().setARGB(0xFF, 0xFF, 0xFF, 0xFF)
        val black = Paint().setARGB(0xFF, 0x00, 0x00, 0x00)
        val darkGrey = Paint().setARGB(0xFF, 105, 105, 105)
        //棕褐色
        val tan = Paint().setARGB(0xFF, 210, 180, 140)

        var height = 0F
        var width = 0F
        textArray.forEach {
            height += it.height

            if (it.width >= width) {
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
