package com.tecknobit.mantis.helpers

import net.suuft.libretranslate.Language
import net.suuft.libretranslate.Translator
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
        val defLanguageValue = mantisResource.defLanguageValue!!
        currentResources.keys().forEach { language ->
            var resource = mantisResource.resource
            val languageSet = currentResources.getJSONObject(language)
            val eLanguage = getLanguage(language)!!
            if(autoTranslate && eLanguage != defLanguageValue)
                resource = Translator.translate(defLanguageValue, eLanguage, resource)
            else if(eLanguage != defLanguageValue)
                resource = ""
            languageSet.put(mantisResource.key, resource)
            currentResources.put(language, languageSet)
        }
        val fileWriter = FileWriter(mantisResource.resourcesFile!!, false)
        fileWriter.write(currentResources.toString(4))
        fileWriter.flush()
        fileWriter.close()
    }

    fun currentLanguagesSet(): List<Language> {
        loadResources()
        val languages = mutableListOf<Language>()
        currentResources.keys().forEach { language ->
            languages.add(getLanguage(language)!!)
        }
        return languages
    }

    private fun getLanguage(language: String): Language? {
        var vLanguage: Language? = null
        languagesSupported.forEach { sLanguage ->
            if (sLanguage.code == Locale.forLanguageTag(language).language)
                vLanguage = sLanguage
        }
        return vLanguage
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