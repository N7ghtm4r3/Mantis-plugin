package language;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.tecknobit.mantis.actions.CreateResourceDialog;
import com.tecknobit.mantis.helpers.MantisManager;
import org.jetbrains.annotations.NotNull;

public class CreateResourceFix extends BaseIntentionAction {

    private final MantisManager.MantisResource mantisResource;

    public CreateResourceFix(PsiElement resourceValue) {
        mantisResource = new MantisManager.MantisResource();
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
            ApplicationManager.getApplication().invokeLater(() -> new CreateResourceDialog(mantisResource).show());
        } catch (Exception ignored){}
    }

}
