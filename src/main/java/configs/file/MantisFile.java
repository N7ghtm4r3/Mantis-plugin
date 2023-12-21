package configs.file;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import configs.MantisLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code MantisFile} class is useful to define a file for the Mantis language
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PsiFileBase
 */
public class MantisFile extends PsiFileBase {

    /**
     * Constructor to init the {@link MantisFile} class
     *
     * @param viewProvider: the view provider for the Mantis file
     *
     */
    public MantisFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, MantisLanguage.INSTANCE);
    }

    /**
     * Method to get the type of the file <br>
     * No-any params required
     *
     * @return the type of the file as {@link FileType}
     */
    @NotNull
    @Override
    public FileType getFileType() {
        return MantisFileType.INSTANCE;
    }

    /**
     * Returns a string representation of the object <br>
     * Any params required
     *
     * @return a string representation of the object as {@link String}
     */
    @Override
    public String toString() {
        return "Mantis File";
    }

}
