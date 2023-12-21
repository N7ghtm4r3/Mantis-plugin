package configs.lexer;

import com.intellij.lexer.FlexAdapter;

/**
 * The {@code MantisLexerAdapter} class is useful to define a custom lexer adapter for the Mantis language
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see FlexAdapter
 */
public class MantisLexerAdapter extends FlexAdapter {

    /**
     * Constructor to init the {@link MantisLexerAdapter} class
     *
     */
    public MantisLexerAdapter() {
        super(new MantisLexer(null));
    }
}
