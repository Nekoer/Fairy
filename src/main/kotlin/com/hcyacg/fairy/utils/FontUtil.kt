package com.hcyacg.fairy.utils

import org.jetbrains.skia.*
import org.jetbrains.skia.paragraph.TypefaceFontProvider
import org.springframework.stereotype.Component
import java.util.*

/**
 * @Author Nekoer
 * @Date  2/12/2023 11:33
 * @Description
 **/
@Component
object FontUtil {
    private val instances: Sequence<FontMgr> = sequence {
        yield(provider)
        yield(FontMgr.default)
        yieldAll(ServiceLoader.load(FontMgr::class.java))
        yieldAll(ServiceLoader.load(TypefaceFontProvider::class.java))
    }

    private val provider: TypefaceFontProvider = TypefaceFontProvider()

    /**
     * 字体列表
     */
    fun families(): Set<String> {
        val names: MutableSet<String> = HashSet()
        for (manager in instances) {
            repeat(manager.familiesCount) { index -> names.add(manager.getFamilyName(index)) }
        }

        return names
    }

    /**
     * 加载字体
     * @see provider
     */
    fun loadTypeface(path: String, index: Int = 0) {
        provider.registerTypeface(Typeface.makeFromFile(path, index))
    }

    /**
     * 加载字体
     * @see provider
     */
    fun loadTypeface(data: Data, index: Int = 0) {
        provider.registerTypeface(Typeface.makeFromData(data, index))
    }

    /**
     * 加载字体
     * @see provider
     */
    fun loadTypeface(bytes: ByteArray, index: Int = 0) {
        Data.makeFromBytes(bytes).use { data -> loadTypeface(data, index) }
    }

    /**
     * 获取指定的 [Typeface]
     */
    fun matchFamilyStyle(familyName: String, style: FontStyle): Typeface? {
        for (provider in instances) {
            return provider.matchFamily(familyName).matchStyle(style) ?: continue
        }
        return null
    }

    /**
     * 获取指定的 [Typeface]
     */
     fun matchFamiliesStyle(families: Array<String?>, style: FontStyle): Typeface? {
        for (provider in instances) {
            for (familyName in families) {
                return provider.matchFamily(familyName).matchStyle(style) ?: continue
            }
        }
        return null
    }

    /**
     * 获取指定的 [FontStyleSet] (count != 0)
     */
     fun matchFamily(familyName: String): FontStyleSet? {
        for (provider in instances) {
            val styles = provider.matchFamily(familyName)
            if (styles.count() != 0) {
                return styles
            }
        }
        return null
    }

    /**
     * 宋体
     */
     fun matchSimSun(style: FontStyle): Typeface? = matchFamilyStyle("SimSun", style)

    /**
     * 新宋体
     */
     fun matchNSimSun(style: FontStyle): Typeface? = matchFamilyStyle("NSimSun", style)

    /**
     * 黑体
     */
     fun matchSimHei(style: FontStyle): Typeface? = matchFamilyStyle("SimHei", style)

    /**
     * 仿宋
     */
     fun matchFangSong(style: FontStyle): Typeface? = matchFamilyStyle("FangSong", style)

    /**
     * 楷体
     */
     fun matchKaiTi(style: FontStyle): Typeface? = matchFamilyStyle("KaiTi", style)

    /**
     * Arial
     */
     fun matchArial(style: FontStyle): Typeface? = matchFamilyStyle("Arial", style)

    /**
     * Calibri
     */
     fun matchCalibri(style: FontStyle): Typeface? = matchFamilyStyle("Calibri", style)

    /**
     * Consolas
     */
     fun matchConsolas(style: FontStyle): Typeface? = matchFamilyStyle("Consolas", style)

    /**
     * Times New Roman
     */
     fun matchTimesNewRoman(style: FontStyle): Typeface? = matchFamilyStyle("Times New Roman", style)

    /**
     * Helvetica
     */
     fun matchHelvetica(style: FontStyle): Typeface? = matchFamilyStyle("Helvetica", style)

    /**
     * Liberation Sans
     */
     fun matchLiberationSans(style: FontStyle): Typeface? = matchFamilyStyle("Liberation Sans", style)

    /**
     * Liberation Serif
     */
     fun matchLiberationSerif(style: FontStyle): Typeface? = matchFamilyStyle("Liberation Serif", style)
}
