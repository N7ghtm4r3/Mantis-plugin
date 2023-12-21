package com.tecknobit.mantis.dialogs

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import com.tecknobit.mantis.helpers.MantisManager.Companion.mantisManager
import com.tecknobit.mantis.helpers.MantisManager.MantisResource
import net.suuft.libretranslate.Language
import javax.swing.JComponent

/**
 * The {@code CreateResourceDialog} class is useful to handle the action to create a new resource
 *
 * @param mantisResource: the resource to create
 * @author N7ghtm4r3 - Tecknobit
 * @see DialogWrapper
 */
class CreateResourceDialog(
    private val mantisResource: MantisResource
): DialogWrapper(true) {

    /**
     * **panel** -> the main panel of the dialog
     */
    private lateinit var panel: DialogPanel

    /**
     * **resourceTextField** -> the [JBTextField] to get the key for the new resource
     */
    private lateinit var resourceTextField: Cell<JBTextField>

    init {
        title = "Create Mantis Resource"
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
        mantisManager.mantisResource = this.mantisResource
        panel = panel {
            row("Enter the resource key") {}
            row {
                resourceTextField = textField()
                    .bindText(mantisResource::key)
                    .onChangedContext { _, _ ->
                        isOKActionEnabled = true
                    }
            }
            row("Language destination source") {}
            row {
                val comboBox = comboBox(mantisManager.currentLanguagesSet())
                    .align(Align.FILL)
                mantisResource.defLanguageValue = comboBox.component.item
                comboBox.component.addItemListener { itemEvent ->
                    mantisResource.defLanguageValue = itemEvent.item as Language
                }
            }
            row {
                checkBox("Auto translate the resource")
                    .bindSelected(mantisResource::autoTranslate)
            }
        }
        return panel
    }

    /**
     * Function to do the validation of the input inserted
     *
     * No-any params required
     * @return the validation info as [ValidationInfo]
     */
    override fun doValidate(): ValidationInfo? {
        panel.apply()
        if(mantisResource.key.replace(" ", "").isEmpty()) {
            isOKActionEnabled = false
            return ValidationInfo("You must insert a valid resource key", resourceTextField.component)
        } else if(mantisManager.keyExists(mantisResource.key)) {
            isOKActionEnabled = false
            return ValidationInfo("This key is already used for a resource", resourceTextField.component)
        }
        isOKActionEnabled = true
        return null
    }

    /**
     * Function to perform the ok action
     *
     * No-any params required
     */
    override fun doOKAction() {
        super.doOKAction()
        mantisManager.createNewResource()
    }

}