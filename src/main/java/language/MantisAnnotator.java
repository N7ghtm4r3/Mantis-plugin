package language;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import org.jetbrains.annotations.NotNull;

public class MantisAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if ((element instanceof PsiLiteralExpression)) {
            holder.newAnnotation(HighlightSeverity.WARNING, "Create a new Mantis resource")
                    .withFix(new CreateResourceFix())
                    .create();
        }
    }

}
