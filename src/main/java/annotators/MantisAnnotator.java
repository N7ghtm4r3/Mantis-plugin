package annotators;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import fixs.CreateResourceFix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.psi.KtStringTemplateExpression;

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
     * @param holder: the holder which managet the annotation creation
     */
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        boolean isJavaExpression = element instanceof PsiLiteralExpression;
        String text = element.getText();
        if((isJavaExpression || element instanceof KtStringTemplateExpression) &&
                !text.replaceAll(" ", "").equals("\"\"") &&
                !text.endsWith(MANTIS_KEY_SUFFIX + "\"")) {
            holder.newAnnotation(HighlightSeverity.WARNING, "Create a new Mantis resource")
                    .withFix(new CreateResourceFix(isJavaExpression, element))
                    .create();
        }
    }

}
