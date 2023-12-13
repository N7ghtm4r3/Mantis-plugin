package com.tecknobit.mantis

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.dialog
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel


class CreateResource: AnAction() {

    private var resourceKey = ""

    override fun actionPerformed(action: AnActionEvent) {
        lateinit var autoTranslate: Cell<JBCheckBox>
        dialog(
            "Create Mantis Resource",
            panel = panel {
                row("Enter the resource key") {}
                row {
                    textField().bindText(::resourceKey)
                }
                row {
                    autoTranslate = checkBox("Auto translate the resource")
                }
            }
        ).show()
    }

}