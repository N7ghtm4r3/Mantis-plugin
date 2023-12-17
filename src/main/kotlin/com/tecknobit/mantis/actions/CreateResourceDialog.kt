package com.tecknobit.mantis.actions

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.tecknobit.mantis.helpers.MantisManager.Companion.createNewResource
import com.tecknobit.mantis.helpers.MantisManager.Companion.mantis
import com.tecknobit.mantis.helpers.MantisManager.MantisResource
import javax.swing.JComponent

class CreateResourceDialog(
    private val mantisResource: MantisResource
): DialogWrapper(true) {

    private lateinit var panel: DialogPanel

    private lateinit var resourceTextField: Cell<JBTextField>

    init {
        title = "Create Mantis Resource"
        init()
    }

    override fun createCenterPanel(): JComponent {
        panel = panel {
            row("Enter the resource key") {}
            row {
                resourceTextField = textField()
                    .bindText(mantisResource::key)
                    .onChangedContext { _, _ ->
                        isOKActionEnabled = true
                    }
            }
            row {
                checkBox("Auto translate the resource")
                    .bindSelected(mantisResource::autoTranslate)
            }
        }
        return panel
    }

    override fun doValidate(): ValidationInfo? {
        panel.apply()
        if(mantisResource.key.isEmpty()) {
            isOKActionEnabled = false
            return ValidationInfo("You must insert a valid resource key", resourceTextField.component)
        } else if(mantis.getResource(mantisResource.key).isNotEmpty()) {
            isOKActionEnabled = false
            return ValidationInfo("This key is already used for a resource", resourceTextField.component)
        }
        isOKActionEnabled = true
        return null
    }

    override fun doOKAction() {
        super.doOKAction()
        createNewResource(mantisResource)
    }

}