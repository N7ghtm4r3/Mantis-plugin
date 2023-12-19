package com.tecknobit.mantis.dialogs

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiFile
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rd.util.first
import com.tecknobit.mantis.Mantis.MANTIS_RESOURCES_PATH
import com.tecknobit.mantis.helpers.MantisManager.Companion.getCurrentResourcesFile
import com.tecknobit.mantis.helpers.MantisManager.Companion.languagesSupported
import com.tecknobit.mantis.helpers.MantisManager.Companion.mantisManager
import com.tecknobit.mantis.helpers.MantisManager.MantisResource
import net.suuft.libretranslate.Language
import org.json.JSONObject
import javax.swing.JComponent

class LanguagesDialog: AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(PlatformDataKeys.PSI_FILE)
        if(file != null)
            e.presentation.isEnabledAndVisible = file.name == MANTIS_RESOURCES_PATH
        else
            e.presentation.isEnabledAndVisible = false
    }

    override fun actionPerformed(action: AnActionEvent) {
        val project = action.getData(PlatformDataKeys.PROJECT)
        val resourcesFile = action.getData(PlatformDataKeys.PSI_FILE)
        if(project != null && resourcesFile != null) {
            LanguagesSetDialog(
                project,
                resourcesFile,
                mutableListOf()
            ).show()
        }
    }

    private inner class LanguagesSetDialog(
        var project: Project,
        resourcesFile: PsiFile,
        var languagesSet: MutableList<LanguageSet>
    ): DialogWrapper(true) {

        private lateinit var panel: DialogPanel

        private val currentResources = JSONObject(resourcesFile.text)

        init {
            title = "Edit Current Languages Set"
            setSize(400, 600)
            init()
        }

        override fun createCenterPanel(): JComponent {
            panel = panel {
                languagesSupported.forEach { language ->
                    val languageSet = LanguageSet(
                        inserted = languageSetExists(language),
                        language = language,
                        translate = false
                    )
                    row {
                        checkBox("")
                            .bindSelected(languageSet::inserted)
                            .component.isSelected = languageSet.inserted
                        text(language.name)
                        checkBox("Auto translate the resource")
                            .bindSelected(languageSet::translate)
                    }
                    separator()
                    languagesSet.add(languageSet)
                }
            }
            return panel
        }

        private fun languageSetExists(
            language: Language
        ): Boolean {
            return currentResources.has(language.code)
        }

        override fun doOKAction() {
            panel.apply()
            super.doOKAction()
            workOnResources()
        }

        private fun workOnResources() {
            setKeysSet()
            createResources()
        }

        private fun setKeysSet() {
            val removedSetKeys = mutableListOf<LanguageSet>()
            languagesSet.forEach { languagesSet ->
                val keyLanguage = languagesSet.language
                val keySet = keyLanguage.code
                if(!languagesSet.inserted) {
                    currentResources.remove(keySet)
                    removedSetKeys.add(languagesSet)
                } else {
                    if(!languageSetExists(keyLanguage))
                        currentResources.put(keySet, JSONObject())
                }
            }
            languagesSet.removeAll(removedSetKeys)
        }

        private fun createResources() {
            val completeSet = getCompleteSet()
            if(completeSet != null) {
                val defaultSet = completeSet.first()
                val set = defaultSet.value
                val mantisResource = MantisResource()
                mantisResource.defLanguageValue = mantisManager.getLanguage(defaultSet.key)
                languagesSet.forEach { languageSet ->
                    val languageKey = mantisManager.getLanguage(languageSet.language.code)!!.code
                    if(!completeSet.containsKey(languageKey)) {
                        mantisResource.autoTranslate = languageSet.translate
                        set.keys().forEach { resourceKey ->
                            mantisResource.key = resourceKey
                            mantisResource.resource = set.getString(resourceKey)
                            mantisManager.createSingleResource(mantisResource, currentResources, languageKey)
                        }
                    }
                }
                mantisManager.saveResources(getCurrentResourcesFile(project), currentResources, project)
            }
        }

        private fun getCompleteSet(): Map<String, JSONObject>? {
            val completeSet = mutableMapOf<String, JSONObject>()
            currentResources.keys().forEach { key ->
                val set = currentResources.getJSONObject(key)
                var isCompleted = true
                if(!set.isEmpty) {
                    set.keys().forEach { resourceKey ->
                        if(set.getString(resourceKey).isEmpty())
                            isCompleted = false
                    }
                    if(isCompleted)
                        completeSet[key] = set
                }
            }
            return if(completeSet.isEmpty())
                null
            else
                completeSet
        }

    }

    data class LanguageSet(
        var inserted: Boolean,
        var language: Language,
        var translate: Boolean
    )

}