package language.lexer;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import language.configs.MantisFileType;
import language.configs.MantisLanguage;
import org.jetbrains.annotations.NotNull;

public class SimpleFile extends PsiFileBase {

    public SimpleFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, MantisLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return MantisFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Mantis File";
    }

}
