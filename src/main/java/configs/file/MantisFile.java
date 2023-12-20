package configs.file;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import configs.MantisLanguage;
import org.jetbrains.annotations.NotNull;

public class MantisFile extends PsiFileBase {

    public MantisFile(@NotNull FileViewProvider viewProvider) {
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
