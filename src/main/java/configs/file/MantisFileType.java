package configs.file;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import configs.MantisLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * The {@code MantisFileType} class is useful to define the custom file type for the Mantis language
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see LanguageFileType
 */
public final class MantisFileType extends LanguageFileType {

    /**
     * {@code INSTANCE} of the {@link MantisFileType}
     */
    public static final MantisFileType INSTANCE = new MantisFileType();

    /**
     * {@code MANTIS_ICON} icon for the {@link MantisFileType}
     */
    public static final Icon MANTIS_ICON = IconLoader.getIcon("/icons/mantis.svg", MantisFileType.class);

    /**
     * Constructor to init the {@link MantisFileType} class
     *
     *
     */
    private MantisFileType() {
        super(MantisLanguage.INSTANCE);
    }

    /**
     * Method to get the name of the Mantis file<br>
     * No-any params required
     *
     * @return name of the Mantis file as {@link String}
     */
    @NotNull
    @Override
    public String getName() {
        return "Mantis Resources File";
    }

    /**
     * Method to get the description of the Mantis file<br>
     * No-any params required
     *
     * @return the description of the Mantis file as {@link String}
     */
    @NotNull
    @Override
    public String getDescription() {
        return "Resources file for Mantis";
    }

    /**
     * Method to get the default extension of the Mantis file<br>
     * No-any params required
     *
     * @return the default extension of the Mantis file as {@link String}
     */
    @NotNull
    @Override
    public String getDefaultExtension() {
        return "mantis";
    }

    /**
     * Method to get the icon of the Mantis file<br>
     * No-any params required
     *
     * @return get the icon of the Mantis file as {@link Icon}
     */
    @Nullable
    @Override
    public Icon getIcon() {
        return MANTIS_ICON;
    }

}