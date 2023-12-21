package configs.token;

import com.intellij.psi.tree.TokenSet;
import org.intellij.sdk.language.psi.MantisTypes;

/**
 * The {@code MantisTokenSets} class is useful to define the custom token sets for the Mantis custom language
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public interface MantisTokenSets {

    /**
     * {@code IDENTIFIERS} of the Mantis custom language
     */
    TokenSet IDENTIFIERS = TokenSet.create(MantisTypes.KEY);

    /**
     * {@code COMMENTS} of the Mantis custom language
     */
    TokenSet COMMENTS = TokenSet.create(MantisTypes.COMMENT);

}
