package configs.token;

import com.intellij.psi.tree.IElementType;
import configs.MantisLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code MantisTokenType} class is useful to define a custom token for Mantis language
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see IElementType
 */
public class MantisTokenType extends IElementType {

    /**
     * Constructor to init the {@link MantisTokenType} class
     *
     * @param debugName: the name for the debug
     *
     */
    public MantisTokenType(@NotNull @NonNls String debugName) {
        super(debugName, MantisLanguage.INSTANCE);
    }

    /**
     * Returns a string representation of the object <br>
     * Any params required
     *
     * @return a string representation of the object as {@link String}
     */
    @Override
    public String toString() {
        return "MantisTokenType." + super.toString();
    }

}
