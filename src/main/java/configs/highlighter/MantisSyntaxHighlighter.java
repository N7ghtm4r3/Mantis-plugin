package configs.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import configs.lexer.MantisLexerAdapter;
import org.intellij.sdk.language.psi.MantisTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * The {@code MantisSyntaxHighlighter} class is useful to define a custom Syntax highlighter for the Mantis language
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see SyntaxHighlighterBase
 */
public class MantisSyntaxHighlighter extends SyntaxHighlighterBase {

    /**
     * {@code SEPARATOR} token
     */
    public static final TextAttributesKey SEPARATOR =
            createTextAttributesKey("MANTIS_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);

    /**
     * {@code BRACKETS} token
     */
    public static final TextAttributesKey BRACKETS =
            createTextAttributesKey("MANTIS_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);

    /**
     * {@code KEY} token
     */
    public static final TextAttributesKey KEY =
            createTextAttributesKey("MANTIS_KEY", DefaultLanguageHighlighterColors.KEYWORD);
    /**
     * {@code VALUE} token
     */
    public static final TextAttributesKey VALUE =
            createTextAttributesKey("MANTIS_VALUE", DefaultLanguageHighlighterColors.STRING);
    /**
     * {@code COMMENT} token
     */
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("MANTIS_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    /**
     * {@code BAD_CHARACTER} token
     */
    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("MANTIS_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    /**
     * {@code BAD_CHAR_KEYS} token
     */
    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};

    /**
     * {@code SEPARATOR_KEYS} token
     */
    private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[]{SEPARATOR, BRACKETS};

    /**
     * {@code KEY_KEYS} token
     */
    private static final TextAttributesKey[] KEY_KEYS = new TextAttributesKey[]{KEY};

    /**
     * {@code VALUE_KEYS} token
     */
    private static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[]{VALUE};

    /**
     * {@code COMMENT_KEYS} token
     */
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};

    /**
     * {@code EMPTY_KEYS} token
     */
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    /**
     * Method to get the custom Mantis highlighting lexer<br>
     * No-any params required
     *
     * @return the custom Mantis highlighting lexer as {@link Lexer}
     */
    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new MantisLexerAdapter();
    }

    /**
     * Method to get the text attributes key
     * @param tokenType: the token type
     * @return the text attributes key as {@link TextAttributesKey}
     */
    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(MantisTypes.SEPARATOR)) {
            return SEPARATOR_KEYS;
        }
        if (tokenType.equals(MantisTypes.KEY)) {
            return KEY_KEYS;
        }
        if (tokenType.equals(MantisTypes.VALUE)) {
            return VALUE_KEYS;
        }
        if (tokenType.equals(MantisTypes.COMMENT)) {
            return COMMENT_KEYS;
        }
        if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }
        return EMPTY_KEYS;
    }

}