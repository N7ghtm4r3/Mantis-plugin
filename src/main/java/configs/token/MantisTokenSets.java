package configs.token;

import com.intellij.psi.tree.TokenSet;
import org.intellij.sdk.language.psi.MantisTypes;

public interface MantisTokenSets {

    TokenSet IDENTIFIERS = TokenSet.create(MantisTypes.KEY);

    TokenSet COMMENTS = TokenSet.create(MantisTypes.COMMENT);

}
