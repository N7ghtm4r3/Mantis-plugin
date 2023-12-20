package language;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.psi.KtStringTemplateExpression;

import static com.tecknobit.mantis.helpers.MantisManager.MANTIS_KEY_SUFFIX;

public class MantisAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        boolean isJavaExpression = element instanceof PsiLiteralExpression;
        if((isJavaExpression || element instanceof KtStringTemplateExpression) &&
                !element.getText().endsWith(MANTIS_KEY_SUFFIX + "\"")) {
            holder.newAnnotation(HighlightSeverity.WARNING, "Create a new Mantis resource")
                    .withFix(new CreateResourceFix(isJavaExpression, element))
                    .create();
        }
    }

}
