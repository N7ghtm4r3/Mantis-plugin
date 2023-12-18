package com.tecknobit.mantis.dialogs

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.panel
import com.tecknobit.mantis.Mantis.MANTIS_RESOURCES_PATH
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
        val resourcesFile = action.getData(PlatformDataKeys.PSI_FILE)!!
    }

    private inner class LanguagesSetDialog: DialogWrapper(true) {

        private lateinit var panel: DialogPanel

        init {
            title = "Edit Current Languages Set"
            init()
        }

        override fun createCenterPanel(): JComponent {
            panel = panel {

            }
            return panel
        }

    }

}