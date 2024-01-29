package com.tecknobit.mantis.dialogs

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiFile
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rd.util.first
import com.tecknobit.mantis.Mantis.MANTIS_RESOURCES_PATH
import com.tecknobit.mantis.helpers.MantisManager.Companion.IGNORED_RESOURCES_KEY
import com.tecknobit.mantis.helpers.MantisManager.Companion.languagesSupported
import com.tecknobit.mantis.helpers.MantisManager.Companion.mantisManager
import com.tecknobit.mantis.helpers.MantisManager.MantisResource
import net.suuft.libretranslate.Language
import org.json.JSONObject
import javax.swing.JComponent

/**
 * The {@code LanguagesDialog} class is useful to handle the action to show the languages set of the user currently selected and
 * managed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see AnAction
 */
class LanguagesDialog: AnAction() {

    /**
     * Function to get the action update thread
     *
     * No-any params required
     *
     * @return [ActionUpdateThread.BGT] as [ActionUpdateThread]
     */
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    /**
     * Function to catch the update event occurred
     *
     * @param e: the action event occurred
     */
    override fun update(e: AnActionEvent) {
        val file = e.getData(PlatformDataKeys.PSI_FILE)
        if(file != null)
            e.presentation.isEnabledAndVisible = file.name == MANTIS_RESOURCES_PATH
        else
            e.presentation.isEnabledAndVisible = false
    }

    /**
     * Function to catch the action performed by the user
     *
     * @param action: the action event occurred
     */
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

    /**
     * The {@code LanguagesSetDialog} class to launch the dialog to show the languages set of the user currently
     * selected and managed
     *
     * @param project: the current project where the plugin is working on
     * @param resourcesFile: the current resources file
     * @param languagesSet: the set of languages to manage
     * @author N7ghtm4r3 - Tecknobit
     * @see DialogWrapper
     */
    private inner class LanguagesSetDialog(
        var project: Project,
        resourcesFile: PsiFile,
        var languagesSet: MutableList<LanguageSet>
    ): DialogWrapper(true) {

        /**
         * **panel** -> the main panel of the dialog
         */
        private lateinit var panel: DialogPanel

        /**
         * **autoTranslateSet** -> whether translate the resources of the set selected
         */
        private var autoTranslateSet: Boolean = true

        /**
         * **currentResources** -> the current resources of languages
         */
        private val currentResources = JSONObject(resourcesFile.text)

        init {
            currentResources.remove(IGNORED_RESOURCES_KEY)
            title = "Edit Current Languages Set"
            setSize(400, 550)
            init()
        }

        /**
         * Function to create the center panel of the dialog
         *
         * No-any params required
         *
         * @return panel as [JComponent]
         */
        override fun createCenterPanel(): JComponent {
            panel = panel {
                for (j in 0 until languagesSupported.size step 3) {
                    val rowLanguages = mutableListOf<LanguageSet>()
                    for (k in 0 until 3) {
                        val language = languagesSupported[(k + j)]
                        rowLanguages.add(
                            LanguageSet(
                                inserted = languageSetExists(language),
                                language = language
                            )
                        )
                    }
                    threeColumnsRow(
                        column1 = {
                            checkBox(rowLanguages[0].language.name)
                                .bindSelected(rowLanguages[0]::inserted)
                                .component.isSelected = rowLanguages[0].inserted
                        },
                        column2 = {
                            checkBox(rowLanguages[1].language.name)
                                .bindSelected(rowLanguages[1]::inserted)
                                .component.isSelected = rowLanguages[1].inserted
                        },
                        column3 = {
                            checkBox(rowLanguages[2].language.name)
                                .bindSelected(rowLanguages[2]::inserted)
                                .component.isSelected = rowLanguages[2].inserted
                        }
                    )
                    separator()
                    languagesSet.addAll(rowLanguages)
                }
                row {
                    checkBox("Auto translate the set")
                        .bindSelected(::autoTranslateSet)
                        .align(Align.CENTER)
                }
            }
            return panel
        }

        /**
         * Function to check whether a language set already exists
         *
         * @param language: the language to check the existence of its set
         * @return whether a language set already exists as [Boolean]
         */
        private fun languageSetExists(
            language: Language
        ): Boolean {
            return currentResources.has(language.code)
        }

        /**
         * Function to perform the ok action
         *
         * No-any params required
         */
        override fun doOKAction() {
            panel.apply()
            super.doOKAction()
            workOnResources()
        }

        /**
         * Function to work and update the current resources
         *
         * No-any params required
         */
        private fun workOnResources() {
            setKeysSet()
            createResources()
        }

        /**
         * Function to set the keys of the set
         *
         * No-any params required
         */
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

        /**
         * Function to fill the languages set with the resources
         *
         * No-any params required
         */
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
                        mantisResource.autoTranslate = autoTranslateSet
                        set.keys().forEach { resourceKey ->
                            mantisResource.key = resourceKey
                            mantisResource.resource = set.getString(resourceKey)
                            mantisManager.createSingleResource(mantisResource, currentResources, languageKey)
                        }
                    }
                }
                mantisManager.saveResources(currentResources, project, true)
            }
        }

        /**
         * Function to get the current completed languages set
         *
         * No-any params required
         * @return the current completed languages set as [Map]<[String], [JSONObject]>
         */
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

    /**
     * The {@code LanguageSet} class is useful to define a language set
     *
     * @param inserted: whether the set is currently inserted
     * @param language: the language of the set
     * @author N7ghtm4r3 - Tecknobit
     */
    data class LanguageSet(
        var inserted: Boolean,
        var language: Language
    )

}