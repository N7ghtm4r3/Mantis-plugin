package configs.file;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import configs.MantisLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public final class MantisFileType extends LanguageFileType {

    public static final MantisFileType INSTANCE = new MantisFileType();

    public static final Icon MANTIS_ICON = IconLoader.getIcon("/icons/mantis.svg", MantisFileType.class);

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