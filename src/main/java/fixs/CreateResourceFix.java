package fixs;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.tecknobit.mantis.dialogs.CreateResourceDialog;
import com.tecknobit.mantis.helpers.MantisManager.MantisResource;
import org.jetbrains.annotations.NotNull;

import static com.tecknobit.mantis.helpers.MantisManager.Companion;

/**
 * The {@code CreateResourceFix} class is useful to create a new {@link MantisResource}
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see BaseIntentionAction
 */
public class CreateResourceFix extends BaseIntentionAction {

    /**
     * {@code mantisResource} the mantis resource to save
     */
    private final MantisResource mantisResource;

    /**
     * Constructor to init the {@link CreateResourceFix} controller
     *
     * @param isJavaExpression: whether is a Java expression or a Kotlin expression
     * @param resourceElement: the [PsiElement] to replace
     *
     */
    public CreateResourceFix(boolean isJavaExpression, PsiElement resourceElement) {
        mantisResource = new MantisResource();
        mantisResource.setJavaExpression(isJavaExpression);
        mantisResource.setResourceElement(resourceElement);
        mantisResource.setResource(resourceElement.getText().replace("\"", ""));
    }

    /**
     * Method to get the text to display <br>
     * No-any params required
     *
     * @return the text to display as {@link String}
     */
    @Override
    public @IntentionName @NotNull String getText() {
        return "Create a new Mantis resource";
    }

    /**
     * Method to get the family name of the action <br>
     * No-any params required
     *
     * @return the family name of the action as {@link String}
     */
    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Mantis resource creation";
    }

    /**
     * Method to check whether show the annotation fix
     *
     * @param project: the current project where the plugin is working on
     * @param editor: the current editor where the plugin is working on
     * @param psiFile: the {@link PsiFile} where has been requested the resource creation
     * @return whether show the annotation fix as boolean
     */
    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        return true;
    }

    /**
     * Method to invoke the annotation fix
     *
     * @param project: the current project where the plugin is working on
     * @param editor: the current editor where the plugin is working on
     * @param psiFile: the {@link PsiFile} where has been requested the resource creation
     */
    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        try {
            Companion.setCurrentResourcesFile(project);
            mantisResource.setProject(project);
            ApplicationManager.getApplication().invokeLater(() -> new CreateResourceDialog(mantisResource).show());
        } catch (Exception ignored){
        }
    }

}
