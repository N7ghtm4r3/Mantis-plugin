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

/**
 * The {@code MantisManager} class is useful to manage the Mantis plugin handling the resource creation and the set of the
 * languages used by the user
 *
 * @author N7ghtm4r3 - Tecknobit
 */
open class MantisManager {

    companion object {

        /**
         * **MANTIS_KEY_SUFFIX** -> the suffix of a Mantis resource key
         */
        const val MANTIS_KEY_SUFFIX = "_key"

        /**
         * **MANTIS_INSTANCE_NAME** -> the default name of a mantis instance, useful when replaced and inserted in the
         * class where the resource has been created
         */
        const val MANTIS_INSTANCE_NAME = "mantis"

        /**
         * **mantisManager** -> the instance of the [MantisManager]
         */
        val mantisManager = MantisManager()

        /**
         * **resourcesFile** -> the current resources file
         */
        private var resourcesFile: VirtualFile? = null

        /**
         * **languagesSupported** -> list of the currently languages supported
         */
        val languagesSupported = Language.values().toMutableList().subList(0, Language.values().size - 1)

        /**
         * Function to set the [currentResources] file
         *
         * @param project: the current project where the plugin is working on
         */
        fun setCurrentResourcesFile(project: Project) {
            if(resourcesFile == null) {
                for (virtualFile in ProjectRootManager.getInstance(project).contentRoots) {
                    if (virtualFile.path.endsWith("main")) {
                        val resDirectory = virtualFile.findOrCreateChildData("mantis", "resources")
                        val mantisResourcesFile = resDirectory.findChild(MANTIS_RESOURCES_PATH)
                        if(mantisResourcesFile != null) {
                            resourcesFile = mantisResourcesFile
                            if(String(resourcesFile!!.contentsToByteArray()).isEmpty())
                                initResourcesFile()
                        } else {
                            resourcesFile = PsiDirectoryFactory.getInstance(project)
                                .createDirectory(resDirectory)
                                .createFile(MANTIS_RESOURCES_PATH).virtualFile
                            initResourcesFile()
                        }
                    }
                }
            }
        }

        /**
         * Function to init the [currentResources] file
         *
         * No-any params required
         */
        private fun initResourcesFile() {
            resourcesFile!!.setBinaryContent(JSONObject().put(
                Locale.getDefault().language,
                JSONObject()
            ).toString(4).toByteArray(UTF_8))
        }

    }

    /**
     * **currentResources** -> the current resources
     */
    private var currentResources = JSONObject()

    /**
     * **mantisResource** -> the instance of a [MantisResource]
     */
    var mantisResource = MantisResource()

    /**
     * Function to create a new resource
     *
     * No-any params required
     */
    fun createNewResource() {
        loadResources()
        val resourceKey = formatKey(mantisResource.key)
        currentResources.keys().forEach { language ->
            createSingleResource(mantisResource, currentResources, language)
        }
        val project = mantisResource.project!!
        saveResources(currentResources, project)
        useMantisInstance(resourceKey, mantisResource)
    }

    /**
     * Function to create and save a single new resource
     *
     * @param mantisResource: the resource to save
     * @param currentResources: the current resources saved
     * @param language: the language of the new resource
     */
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

    /**
     * Function to save and apply the new [currentResources]
     *
     * @param currentResources: the current resources saved
     * @param project: the current project where the plugin is working on
     */
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

    /**
     * Function to get the current languages set saved and used
     *
     * No-any params required
     *
     * @return the current languages set saved and used as [List] of [Language]
     */
    fun currentLanguagesSet(): List<Language> {
        loadResources()
        val languages = mutableListOf<Language>()
        currentResources.keys().forEach { language ->
            languages.add(getLanguage(language)!!)
        }
        return languages
    }

    /**
     * Function to get a language
     * @param language: the code of the language to get
     *
     * @return the language as [Language]
     */
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

    /**
     * Function to check whether a key of a resource already exists
     *
     * @param resourceKey: the resource key to check whether exists
     * @return whether a key of a resource already exists [Boolean]
     */
    open fun keyExists(
        resourceKey: String
    ): Boolean {
        loadResources()
        return currentResources.toString().contains("\"${formatKey(resourceKey)}\":")
    }

    /**
     * Function to format correctly a key for a resource
     *
     * @param key: the key to format
     * @return the key correclty formatted as [String]
     */
    private fun formatKey(
        key: String
    ): String {
        return key.lowercase().replace(" ", "").replace(MANTIS_KEY_SUFFIX, "")
            .replace("-key", "").replace("key", "") + MANTIS_KEY_SUFFIX
    }

    /**
     * Function to check whether a resource already exists
     *
     * @param resource: the resource value to check whether exists
     * @return the key of the resource as [String] if already exists, null otherwise
     */
    open fun resourceExists(resource: String): String? {
        loadResources()
        var resourceKey: String? = null
        val lowercaseResource = resource.lowercase()
        if(currentResources.toString().lowercase().contains(":$lowercaseResource")) {
            val matchResource = lowercaseResource.replace("\"", "")
            currentResources.keys().forEach { language ->
                if(resourceKey == null) {
                    val languageSet = currentResources.getJSONObject(language)
                    languageSet.keys().forEach { key ->
                        if(languageSet.getString(key).lowercase() == matchResource)
                            resourceKey = key
                    }
                } else
                    return@forEach
            }
        }
        return resourceKey
    }

    /**
     * Function to use the Mantis instance
     *
     * @param resourceKey: the key of the resource to use
     * @param mantisResource: the payload of the resource to use
     */
    fun useMantisInstance(
        resourceKey: String,
        mantisResource: MantisResource
    ) {
        val project = mantisResource.project!!
        val isJavaExpression = mantisResource.isJavaExpression
        val psiElement = mantisResource.resourceElement!!
        WriteCommandAction.writeCommandAction(project).run<Throwable> {
            if(isJavaExpression) {
                val factory: PsiElementFactory = JavaPsiFacade.getInstance(project).elementFactory
                val semiColon = if(psiElement.text.endsWith(";"))
                    ";"
                else
                    ""
                psiElement.replace(factory.createExpressionFromText(
                    "$MANTIS_INSTANCE_NAME.getResource(\"$resourceKey\")$semiColon",
                    null
                ))
            } else {
                val psiFactory = KtPsiFactory(project)
                psiElement.replace(psiFactory.createExpression("$MANTIS_INSTANCE_NAME.getResource(\"$resourceKey\")"))
            }
        }
    }

    /**
     * Function to load the [currentResources]
     *
     * No-any params required
     */
    private fun loadResources() {
        currentResources = JSONObject(String(resourcesFile!!.contentsToByteArray()))
    }

    /**
     * The {@code MantisResource} class is useful to define a Mantis resource
     *
     * @param isJavaExpression: whether is a Java expression or a Kotlin expression
     * @param project: the current project where the plugin is working on
     * @param resourceElement: the [PsiElement] to replace
     * @param resource: the resource content to save
     * @param defLanguageValue: the default language of the new resource to save
     * @param key: the key for the resource to save
     * @param autoTranslate: whether the resource must be translated automatically
     * @author N7ghtm4r3 - Tecknobit
     */
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