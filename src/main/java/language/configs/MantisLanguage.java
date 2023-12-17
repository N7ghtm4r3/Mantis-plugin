package language.configs;

import com.intellij.lang.Language;

public class MantisLanguage extends Language {

    public static final MantisLanguage INSTANCE = new MantisLanguage();

    private MantisLanguage() {
        super("Mantis");
    }

}
