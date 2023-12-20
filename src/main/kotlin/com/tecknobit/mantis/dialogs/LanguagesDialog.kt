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
            setSize(400, 500)
            init()
        }

        override fun createCenterPanel(): JComponent {
            panel = panel {
                for (j in 0 until languagesSupported.size step 3) {
                    val rowLanguages = mutableListOf<LanguageSet>()
                    for (k in 0 until 3) {
                        val language = languagesSupported[(k + j)]
                        rowLanguages.add(
                            LanguageSet(
                                inserted = languageSetExists(language),
                                language = language,
                                translate = false
                            )
                        )
                    }
                    threeColumnsRow(
                        column1 = {
                            checkBox("")
                                .bindSelected(rowLanguages[0]::inserted)
                                .component.isSelected = rowLanguages[0].inserted
                            text(rowLanguages[0].language.name)
                            /*for(i in 0 until 3) {
                                val languageSet = rowLanguages[i]
                                checkBox("")
                                    .bindSelected(languageSet::inserted)
                                    .component.isSelected = languageSet.inserted
                                text(languageSet.language.name)
                            }*/
                        },
                        column2 = {
                            checkBox("")
                                .bindSelected(rowLanguages[1]::inserted)
                                .component.isSelected = rowLanguages[1].inserted
                            text(rowLanguages[1].language.name)
                            /*for(i in 0 until 3) {
                                val languageSet = rowLanguages[i]
                                checkBox("Auto translate the set")
                                    .bindSelected(languageSet::translate)
                                    //.component.isVisible = languageSet.inserted
                            }*/
                        },
                        column3 = {
                            checkBox("")
                                .bindSelected(rowLanguages[2]::inserted)
                                .component.isSelected = rowLanguages[2].inserted
                            text(rowLanguages[2].language.name)
                            checkBox("Auto translate the set")
                                .bindSelected(rowLanguages[2]::translate)
                        }
                    )
                    separator()
                    languagesSet.addAll(rowLanguages)
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