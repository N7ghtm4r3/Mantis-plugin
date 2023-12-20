package com.tecknobit.mantis.helpers

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.impl.file.PsiDirectoryFactory
import com.tecknobit.mantis.Mantis.MANTIS_RESOURCES_PATH
import net.suuft.libretranslate.Language
import net.suuft.libretranslate.Translator
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.json.JSONObject
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*


open class MantisManager {

    companion object {

        const val MANTIS_KEY_SUFFIX = "_key"

        const val MANTIS_INSTANCE_NAME = "mantis"

        val mantisManager = MantisManager()

        private var resourcesFile: VirtualFile? = null

        val languagesSupported = Language.values().toMutableList().subList(0, Language.values().size - 1)

        fun setCurrentResourcesFile(project: Project) {
            if(resourcesFile == null) {
                for (virtualFile in ProjectRootManager.getInstance(project).contentRoots) {
                    if (virtualFile.path.endsWith("main")) {
                        val resDirectory = virtualFile.findOrCreateChildData("mantis", "resources")
                        val mantisResourcesFile = resDirectory.findChild(MANTIS_RESOURCES_PATH)
                        if(mantisResourcesFile != null)
                            resourcesFile = mantisResourcesFile
                        else {
                            resourcesFile = PsiDirectoryFactory.getInstance(project)
                                .createDirectory(resDirectory)
                                .createFile(MANTIS_RESOURCES_PATH).virtualFile
                            resourcesFile!!.setBinaryContent(JSONObject().put(
                                Locale.getDefault().language,
                                JSONObject()
                            ).toString(4).toByteArray(UTF_8))
                        }
                    }
                }
            }
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
        saveResources(currentResources, project)
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
        currentResources: JSONObject,
        project: Project
    ) {
        ProjectRootManager.getInstance(project).fileIndex.iterateContent { file: VirtualFile ->
            val path = file.path
            if (path.endsWith(MANTIS_RESOURCES_PATH)) {
                WriteCommandAction.writeCommandAction(project).run<Throwable> {
                    file.setBinaryContent(currentResources.toString(4).toByteArray(UTF_8))
                }
            }
            true
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
        currentResources = JSONObject(String(resourcesFile!!.contentsToByteArray()))
    }

    data class MantisResource(
        var isJavaExpression: Boolean = true,
        var project: Project? = null,
        var resourceElement: PsiElement? = null,
        var resource: String = "",
        var defLanguageValue: Language? = null,
        var key: String = "",
        var autoTranslate: Boolean = true
    )

}