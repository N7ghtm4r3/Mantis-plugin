package configs.highlighter;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code MantisSyntaxHighlighterFactory} class is useful to define a custom syntax highlighter factory for the
 * Mantis language
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see SyntaxHighlighterFactory
 */
final class MantisSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    /**
     * Method to get the syntax highlighter
     * @param project: the project where the syntax highlighter is working on
     * @param virtualFile: the virtual file where the syntax highlighter is working on
     * @return the syntax highlighter as {@link SyntaxHighlighter}
     */
    @NotNull
    @Override
    public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
        return new MantisSyntaxHighlighter();
    }

}