package language.configs;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class MantisElementType extends IElementType {

    public MantisElementType(@NotNull @NonNls String debugName) {
        super(debugName, MantisLanguage.INSTANCE);
    }

}
