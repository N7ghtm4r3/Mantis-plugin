package com.tecknobit.mantis.helpers

import com.tecknobit.mantis.Mantis
import net.suuft.libretranslate.Language
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.util.*

open class MantisManager {

    companion object {

        val mantisManager = MantisManager()

        val languagesSupported = Language.values()

    }

    private var currentResources = JSONObject()

    var mantisResource = MantisResource()

    fun createNewResource() {
        loadResources()
        val autoTranslate = mantisResource.autoTranslate
        var resource = mantisResource.resource
        currentResources.keys().forEach { language ->
            val languageSet = currentResources.getJSONObject(language)
            if(autoTranslate && language != Locale.getDefault().toLanguageTag())
                resource = Mantis(Locale.getDefault()).translate(resource)
            languageSet.put(mantisResource.key, resource)
            currentResources.put(language, languageSet)
        }
        val fileWriter = FileWriter(mantisResource.resourcesFile!!, false)
        fileWriter.write(currentResources.toString(4))
        fileWriter.flush()
    }

    fun currentLanguagesSet(): List<Language> {
        loadResources()
        val languages = mutableListOf<Language>()
        currentResources.keys().forEach { language ->
            var vLanguage: Language? = null
            languagesSupported.forEach { sLanguage ->
                if (sLanguage.code == Locale.forLanguageTag(language).language) {
                    println(sLanguage.code)
                    println(Locale.forLanguageTag(language).language)
                    vLanguage = sLanguage
                }
            }
            languages.add(vLanguage!!)
        }
        return languages
    }

    fun keyExists(resourceKey: String): Boolean {
        loadResources()
        return currentResources.toString().contains("\"$resourceKey\":")
    }

    private fun loadResources() {
        if(currentResources.isEmpty)
            currentResources = JSONObject(Scanner(mantisResource.resourcesFile!!).useDelimiter("\\Z").next())
    }

    data class MantisResource(
        var resourcesFile: File? = null,
        var resource: String = "",
        var defLanguageValue: Language? = null,
        var key: String = "",
        var autoTranslate: Boolean = true
    )

}