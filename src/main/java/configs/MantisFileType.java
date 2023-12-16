package configs;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public final class MantisFileType extends LanguageFileType {

    public static final MantisFileType INSTANCE = new MantisFileType();

    // TODO: SET THE CORRECT ICON
    public static final Icon MANTIS_ICON = IconLoader.getIcon("/icons/tecknobit.png", MantisFileType.class);

    private MantisFileType() {
        super(MantisLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Mantis Resources File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Resources file for Mantis";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "mantis";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return MANTIS_ICON;
    }

}