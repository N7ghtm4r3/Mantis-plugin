package configs;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import configs.file.MantisFileType;
import configs.highlighter.MantisSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

final class MantisColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Key", MantisSyntaxHighlighter.KEY),
            new AttributesDescriptor("Separator", MantisSyntaxHighlighter.SEPARATOR),
            new AttributesDescriptor("Value", MantisSyntaxHighlighter.VALUE),
            new AttributesDescriptor("Bad value", MantisSyntaxHighlighter.BAD_CHARACTER)
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return MantisFileType.MANTIS_ICON;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new MantisSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return """
                # You are reading the ".mantis" entry.
                {
                    "it": {
                      "string_one" : "ciao!",
                      "string_two": "prova questa libreria :)"
                    },
                    "en": {
                      "string_one" : "hello!",\s
                      "string_two": "try this library :)"
                    }\s
                }""";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Mantis";
    }

}
