package com.tecknobit.mantis.helpers

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiFile
import net.suuft.libretranslate.Language
import net.suuft.libretranslate.Translator
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.util.*


open class MantisManager {

    companion object {

        const val MANTIS_KEY_SUFFIX = "_key"

        const val MANTIS_INSTANCE_NAME = "mantis"

        val mantisManager = MantisManager()

        val languagesSupported = Language.values()

    }

    private var currentResources = JSONObject()

    var mantisResource = MantisResource()

    fun createNewResource() {
        loadResources()
        val autoTranslate = mantisResource.autoTranslate
        val defLanguageValue = mantisResource.defLanguageValue!!
        val resourceKey = formatKey(mantisResource.key)
        currentResources.keys().forEach { language ->
            var resource = mantisResource.resource
            val languageSet = currentResources.getJSONObject(language)
            val eLanguage = getLanguage(language)!!
            if(autoTranslate && eLanguage != defLanguageValue)
                resource = Translator.translate(defLanguageValue, eLanguage, resource)
            else if(eLanguage != defLanguageValue)
                resource = ""
            languageSet.put(resourceKey, resource)
            currentResources.put(language, languageSet)
        }
        val fileWriter = FileWriter(mantisResource.resourcesFile!!, false)
        fileWriter.write(currentResources.toString(4))
        fileWriter.flush()
        fileWriter.close()
        WriteCommandAction.writeCommandAction(mantisResource.project).run<Throwable> {
            val currentExpression = mantisResource.resourceElement!!
            val factory: PsiElementFactory = JavaPsiFacade.getInstance(mantisResource.project!!).elementFactory
            val semiColon = if(currentExpression.text.endsWith(";"))
                ";"
            else
                ""
            currentExpression.replace(factory.createExpressionFromText(
                "$MANTIS_INSTANCE_NAME.getResource(\"$resourceKey\")$semiColon",
                null
            ))
        }
    }

    fun currentLanguagesSet(): List<Language> {
        loadResources()
        val languages = mutableListOf<Language>()
        currentResources.keys().forEach { language ->
            languages.add(getLanguage(language)!!)
        }
        return languages
    }

    private fun getLanguage(
        language: String
    ): Language? {
        var vLanguage: Language? = null
        languagesSupported.forEach { sLanguage ->
            if (sLanguage.code == Locale.forLanguageTag(language).language)
                vLanguage = sLanguage
        }
        return vLanguage
    }

    private fun formatKey(
        key: String
    ): String {
        return key.lowercase().replace(" ", "").replace(MANTIS_KEY_SUFFIX, "")
            .replace("-key", "").replace("key", "") + MANTIS_KEY_SUFFIX
    }

    fun keyExists(
        resourceKey: String
    ): Boolean {
        loadResources()
        return currentResources.toString().contains("\"${formatKey(resourceKey)}\":")
    }

    private fun loadResources() {
        currentResources = JSONObject(Scanner(mantisResource.resourcesFile!!).useDelimiter("\\Z").next())
    }

    data class MantisResource(
        var project: Project? = null,
        var psiFile: PsiFile? = null,
        var resourcesFile: File? = null,
        var resourceElement: PsiElement? = null,
        var resource: String = "",
        var defLanguageValue: Language? = null,
        var key: String = "",
        var autoTranslate: Boolean = true
    )

}