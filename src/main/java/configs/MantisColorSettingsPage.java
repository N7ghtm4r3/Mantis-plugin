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

/**
 * The {@code MantisColorSettingsPage} class is useful to customize the color of the resources.mantis file
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ColorSettingsPage
 */
final class MantisColorSettingsPage implements ColorSettingsPage {

    /**
     * {@code DESCRIPTORS} the items customizable
     */
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Key", MantisSyntaxHighlighter.KEY),
            new AttributesDescriptor("Separator", MantisSyntaxHighlighter.SEPARATOR),
            new AttributesDescriptor("Value", MantisSyntaxHighlighter.VALUE),
            new AttributesDescriptor("Bad value", MantisSyntaxHighlighter.BAD_CHARACTER)
    };

    /**
     * Method to get the icon of this plugin <br>
     * No-any params required
     *
     * @return the icon of this plugin instance as {@link Icon}
     */
    @Override
    public @NotNull Icon getIcon() {
        return MantisFileType.MANTIS_ICON;
    }

    /**
     * Method to get the mantis custom syntax highlighter instance <br>
     * No-any params required
     *
     * @return the custom syntax highlighter instance as {@link SyntaxHighlighter}
     */
    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new MantisSyntaxHighlighter();
    }

    /**
     * Method to get the demo text <br>
     * No-any params required
     *
     * @return the demo text as {@link String}
     */
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

    /**
     * Method to get map of the text attributes key instance <br>
     * No-any params required
     *
     * @return map of the text attributes key instance as {@link Map} of {@link String} and {@link TextAttributesKey}
     */
    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    /**
     * Method to get {@link #DESCRIPTORS} instance <br>
     * No-any params required
     *
     * @return {@link #DESCRIPTORS} instance as {@link AttributesDescriptor[]}
     */
    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    /**
     * Method to get the color descriptor <br>
     * No-any params required
     *
     * @return the color descriptor as {@link ColorDescriptor[]}
     */
    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    /**
     * Method to get the display name <br>
     * No-any params required
     *
     * @return the display name as {@link String}
     */
    @NotNull
    @Override
    public String getDisplayName() {
        return "Mantis";
    }

}
