package configs;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code MantisElementType} class is useful to define an element of the Mantis custom language
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see IElementType
 */
public class MantisElementType extends IElementType {

    /**
     * Constructor to init the {@link MantisElementType} class
     *
     * @param debugName: the debug name of the item
     *
     */
    public MantisElementType(@NotNull @NonNls String debugName) {
        super(debugName, MantisLanguage.INSTANCE);
    }

}
