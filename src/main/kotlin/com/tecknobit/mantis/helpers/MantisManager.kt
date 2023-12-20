package com.tecknobit.mantis.helpers

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiFile
import com.tecknobit.mantis.Mantis
import net.suuft.libretranslate.Language
import net.suuft.libretranslate.Translator
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.concurrent.atomic.AtomicReference


open class MantisManager {

    companion object {

        const val MANTIS_KEY_SUFFIX = "_key"

        const val MANTIS_INSTANCE_NAME = "mantis"

        val mantisManager = MantisManager()

        val languagesSupported = Language.values().toMutableList().subList(0, Language.values().size - 1)

        private var resourcesPath: String? = null

        fun getCurrentResourcesFile(project: Project): File {
            if (resourcesPath == null) {
                val resourcesLeafPath = AtomicReference("")
                ProjectRootManager.getInstance(project).fileIndex.iterateContent { file: VirtualFile ->
                    val path = file.path
                    if (path.endsWith(Mantis.MANTIS_RESOURCES_PATH)) resourcesPath = path
                    else if (path.endsWith("/main/resources")) {
                        resourcesLeafPath.set(path)
                    }
                    true
                }
                if (resourcesPath == null) {
                    try {
                        val resourcesPath = resourcesLeafPath.get() + "/" + Mantis.MANTIS_RESOURCES_PATH
                        if (File(resourcesPath).createNewFile()) {
                            val fileWriter = FileWriter(resourcesPath, false)
                            fileWriter.write(
                                JSONObject().put(
                                    Locale.getDefault().language,
                                    JSONObject()
                                ).toString(4)
                            )
                            fileWriter.flush()
                            fileWriter.close()
                        }
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                }
            }
            return File(resourcesPath!!)
        }

    }

    private var currentResources = JSONObject()

    var mantisResource = MantisResource()
    
    fun createNewResource() {
        loadResources()
        val resourceKey = formatKey(mantisResource.key)
        currentResources.keys().forEach { language ->
            createSingleResource(mantisResource, currentResources, language)
        }
        val project = mantisResource.project!!
        saveResources(mantisResource.resourcesFile!!, currentResources, project)
        WriteCommandAction.writeCommandAction(project).run<Throwable> {
            val currentExpression = mantisResource.resourceElement!!
            if(mantisResource.isJavaExpression) {
                val factory: PsiElementFactory = JavaPsiFacade.getInstance(project).elementFactory
                val semiColon = if(currentExpression.text.endsWith(";"))
                    ";"
                else
                    ""
                currentExpression.replace(factory.createExpressionFromText(
                    "$MANTIS_INSTANCE_NAME.getResource(\"$resourceKey\")$semiColon",
                    null
                ))
            } else {
                val psiFactory = KtPsiFactory(project)
                currentExpression.replace(psiFactory
                    .createExpression("$MANTIS_INSTANCE_NAME.getResource(\"$resourceKey\")"))
            }
        }
    }

    fun createSingleResource(
        mantisResource: MantisResource,
        currentResources: JSONObject,
        language: String
    ) {
        val autoTranslate = mantisResource.autoTranslate
        val defLanguageValue = mantisResource.defLanguageValue!!
        var resource = mantisResource.resource
        val languageSet = currentResources.getJSONObject(language)
        val eLanguage = getLanguage(language)!!
        if(autoTranslate && eLanguage != defLanguageValue)
            resource = Translator.translate(defLanguageValue, eLanguage, resource)
        else if(eLanguage != defLanguageValue)
            resource = ""
        languageSet.put(formatKey(mantisResource.key), resource)
        currentResources.put(language, languageSet)
    }

    fun saveResources(
        resourcesFile: File,
        currentResources: JSONObject,
        project: Project
    ) {
        WriteCommandAction.writeCommandAction(project).run<Throwable> {
            val fileWriter = FileWriter(resourcesFile, false)
            fileWriter.write(currentResources.toString(4))
            fileWriter.flush()
            fileWriter.close()
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

    fun getLanguage(
        language: String
    ): Language? {
        var vLanguage: Language? = null
        languagesSupported.forEach { sLanguage ->
            if (vLanguage == null && sLanguage.code == language)
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
        var isJavaExpression: Boolean = true,
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