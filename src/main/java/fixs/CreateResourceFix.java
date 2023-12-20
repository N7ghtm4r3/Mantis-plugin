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

public class CreateResourceFix extends BaseIntentionAction {

    private final MantisResource mantisResource;

    public CreateResourceFix(boolean isJavaExpression, PsiElement resourceValue) {
        mantisResource = new MantisResource();
        mantisResource.setJavaExpression(isJavaExpression);
        mantisResource.setResourceElement(resourceValue);
        mantisResource.setResource(resourceValue.getText().replace("\"", ""));
    }

    @Override
    public @IntentionName @NotNull String getText() {
        return "Create a new Mantis resource";
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Mantis resource creation";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        return true;
    }

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
