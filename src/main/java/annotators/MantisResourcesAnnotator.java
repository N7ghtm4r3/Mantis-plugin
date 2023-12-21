package annotators;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code MantisResourcesAnnotator} class is useful to warn the user that there is a resource value saved but is
 * empty
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Annotator
 */
public class MantisResourcesAnnotator implements Annotator {

    /**
     * Method to annotate
     *
     * @param psiElement: the element where launch the annotation
     * @param annotationHolder: the holder which managet the annotation creation
     */
    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
        if(psiElement instanceof LeafPsiElement) {
            String text = psiElement.getText();
            if(psiElement.toString().contains("MantisTokenType.VALUE") && text.equals("\"\"")) {
                annotationHolder.newAnnotation(HighlightSeverity.WEAK_WARNING, "This resource value is empty")
                        .create();
            }
        }
    }

}
