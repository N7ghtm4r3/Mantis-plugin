package language;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.tecknobit.mantis.actions.CreateResourceDialog;
import com.tecknobit.mantis.helpers.MantisManager;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static com.tecknobit.mantis.Mantis.MANTIS_RESOURCES_PATH;

public class CreateResourceFix extends BaseIntentionAction {

    private static String RESOURCES_PATH = null;

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
            mantisResource.setResourcesFile(getCurrentResourcesFile(project));
            ApplicationManager.getApplication().invokeLater(() -> new CreateResourceDialog(mantisResource).show());
        } catch (Exception ignored){}
    }

    private File getCurrentResourcesFile(Project project) {
        if(RESOURCES_PATH == null) {
            AtomicReference<String> resourcesLeafPath = new AtomicReference<>("");
            ProjectRootManager.getInstance(project).getFileIndex().iterateContent(file -> {
                String path = file.getPath();
                if(path.endsWith(MANTIS_RESOURCES_PATH))
                    RESOURCES_PATH = path;
                else if (path.endsWith("/main/resources")) {
                    resourcesLeafPath.set(path);
                }
                return true;
            });
            if(RESOURCES_PATH == null) {
                try {
                    String resourcesPath = resourcesLeafPath.get() + "/" + MANTIS_RESOURCES_PATH;
                    if(new File(resourcesPath).createNewFile()) {
                        FileWriter fileWriter = new FileWriter(resourcesPath, false);
                        fileWriter.write(new JSONObject().put(Locale.getDefault().toLanguageTag(),
                                new JSONObject()).toString(4));
                        fileWriter.flush();
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new File(RESOURCES_PATH);
    }

}
