package fixs;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.psi.PsiElement;
import com.tecknobit.mantis.helpers.MantisManager.MantisResource;

/**
 * The {@code MantisFix} class is useful to manage a fix of the editor
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see BaseIntentionAction
 */
public abstract class MantisFix extends BaseIntentionAction {

    /**
     * {@code mantisResource} the mantis resource to save
     */
    protected final MantisResource mantisResource;

    /**
     * Constructor to init the {@link MantisFix} controller
     *
     * @param isJavaExpression: whether is a Java expression or a Kotlin expression
     * @param resourceElement: the [PsiElement] to replace
     *
     */
    public MantisFix(boolean isJavaExpression, PsiElement resourceElement) {
        mantisResource = new MantisResource();
        mantisResource.setJavaExpression(isJavaExpression);
        mantisResource.setResourceElement(resourceElement);
        mantisResource.setResource(resourceElement.getText().replace("\"", ""));
    }

}
