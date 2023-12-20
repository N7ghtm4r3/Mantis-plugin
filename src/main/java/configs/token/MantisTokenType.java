package configs.token;

import com.intellij.psi.tree.IElementType;
import configs.MantisLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class MantisTokenType extends IElementType {

    public MantisTokenType(@NotNull @NonNls String debugName) {
        super(debugName, MantisLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "MantisTokenType." + super.toString();
    }

}
