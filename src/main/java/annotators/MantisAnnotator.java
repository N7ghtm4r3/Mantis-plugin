package annotators;

import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiLiteralExpression;
import com.tecknobit.mantis.helpers.MantisManager;
import fixs.CreateResourceFix;
import fixs.ReplaceResourceFix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.psi.KtFile;
import org.jetbrains.kotlin.psi.KtStringTemplateExpression;

import static com.intellij.lang.annotation.HighlightSeverity.WARNING;
import static com.tecknobit.mantis.helpers.MantisManager.Companion;
import static com.tecknobit.mantis.helpers.MantisManager.MANTIS_KEY_SUFFIX;

/**
 * The {@code MantisAnnotator} class is useful to annotate and catch if there is a literal expression that can be
 * transformed to a Mantis resource
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Annotator
 */
public class MantisAnnotator implements Annotator {

    /**
     * Method to annotate
     *
     * @param element: the element where launch the annotation
     * @param holder: the holder which manage the annotation creation
     */
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        PsiFile containerFile = element.getContainingFile();
        if(containerFile instanceof PsiJavaFile || containerFile instanceof KtFile) {
            boolean isJavaExpression = element instanceof PsiLiteralExpression;
            String text = element.getText();
            if((isJavaExpression || element instanceof KtStringTemplateExpression) &&
                    text.startsWith("\"") && text.endsWith("\"") &&
                    !text.replaceAll(" ", "").equals("\"\"") &&
                    !text.endsWith(MANTIS_KEY_SUFFIX + "\"")) {
                AnnotationBuilder builder = holder.newAnnotation(WARNING, "Create a new Mantis resource");
                Companion.setCurrentResourcesFile(element.getProject());
                MantisManager mantisManager = new MantisManager();
                String resourceKey = mantisManager.resourceExists(text);
                if(resourceKey == null)
                    builder.withFix(new CreateResourceFix(isJavaExpression, element));
                else
                    builder.withFix(new ReplaceResourceFix(isJavaExpression, element, resourceKey));
                builder.create();
            }
        }
    }

}
