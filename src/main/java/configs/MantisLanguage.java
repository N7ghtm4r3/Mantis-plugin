package configs;

import com.intellij.lang.Language;

/**
 * The {@code MantisLanguage} class is useful to define the custom language for Mantis
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Language
 */
public class MantisLanguage extends Language {

    /**
     * {@code INSTANCE} of the {@link MantisLanguage}
     */
    public static final MantisLanguage INSTANCE = new MantisLanguage();

    /**
     * Constructor to init the {@link MantisLanguage} class
     *
     */
    private MantisLanguage() {
        super("Mantis");
    }

}
