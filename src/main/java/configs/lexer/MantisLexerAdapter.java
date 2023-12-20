package configs.lexer;

import com.intellij.lexer.FlexAdapter;

public class MantisLexerAdapter extends FlexAdapter {

    public MantisLexerAdapter() {
        super(new MantisLexer(null));
    }
}
