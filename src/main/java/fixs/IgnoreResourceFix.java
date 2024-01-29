package fixs;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.tecknobit.mantis.helpers.MantisManager;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code IgnoreResourceFix} class is useful to ignore a possible Mantis resource
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see BaseIntentionAction
 */
public class IgnoreResourceFix extends BaseIntentionAction {

    /**
     * {@code resourceToIgnore} the resource to ignore
     */
    private final String resourceToIgnore;

    /**
     * Constructor to init the {@link IgnoreResourceFix} class
     *
     * @param resourceToIgnore: the resource to ignore
     *
     */
    public IgnoreResourceFix(String resourceToIgnore) {
        this.resourceToIgnore = resourceToIgnore;
    }

    /**
     * Method to get the text to display <br>
     * No-any params required
     *
     * @return the text to display as {@link String}
     */
    @Override
    public @IntentionName @NotNull String getText() {
        return "Ignore this possible Mantis resource";
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
        MantisManager manager = new MantisManager();
        manager.insertIgnoredResource(resourceToIgnore, project);
    }

}
