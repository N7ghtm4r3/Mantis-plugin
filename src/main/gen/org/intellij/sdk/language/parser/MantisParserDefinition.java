package org.intellij.sdk.language.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import configs.file.MantisFile;
import configs.MantisLanguage;
import configs.token.MantisTokenSets;
import configs.lexer.MantisLexerAdapter;
import org.intellij.sdk.language.psi.MantisTypes;
import org.jetbrains.annotations.NotNull;

final class MantisParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE = new IFileElementType(MantisLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new MantisLexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return MantisTokenSets.COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new MantisParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new MantisFile(viewProvider);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return MantisTypes.Factory.createElement(node);
    }

}
