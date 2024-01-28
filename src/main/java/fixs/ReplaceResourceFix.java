package fixs;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.tecknobit.mantis.helpers.MantisManager;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code ReplaceResourceFix} class is useful to replace a {@link PsiElement} with a {@link com.tecknobit.mantis.Mantis}
 * resource
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see MantisFix
 * @see BaseIntentionAction
 */
public class ReplaceResourceFix extends MantisFix {

    /**
     * {@code resourceKey} the key of the resource to use
     */
    private final String resourceKey;

    /**
     * Constructor to init the {@link ReplaceResourceFix} controller
     *
     * @param isJavaExpression: whether is a Java expression or a Kotlin expression
     * @param resourceElement: the [PsiElement] to replace
     *
     */
    public ReplaceResourceFix(boolean isJavaExpression, PsiElement resourceElement, String resourceKey) {
        super(isJavaExpression, resourceElement);
        this.resourceKey = resourceKey;
    }

    /**
     * Method to get the text to display <br>
     * No-any params required
     *
     * @return the text to display as {@link String}
     */
    @Override
    public @IntentionName @NotNull String getText() {
        return "Matches with an already saved Mantis resource, transform it";
    }

    /**
     * Method to get the family name of the action <br>
     * No-any params required
     *
     * @return the family name of the action as {@link String}
     */
    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Mantis resources manager";
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
        MantisManager mantisManager = new MantisManager();
        mantisResource.setProject(project);
        mantisManager.useMantisInstance(resourceKey, mantisResource);
    }

}
