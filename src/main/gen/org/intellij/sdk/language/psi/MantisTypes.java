// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import configs.MantisElementType;
import configs.token.MantisTokenType;
import org.intellij.sdk.language.psi.impl.MantisPropertyImpl;

public interface MantisTypes {

  IElementType PROPERTY = new MantisElementType("PROPERTY");
  IElementType COMMENT = new MantisTokenType("COMMENT");
  IElementType CRLF = new MantisTokenType("CRLF");
  IElementType KEY = new MantisTokenType("KEY");
  IElementType SEPARATOR = new MantisTokenType("SEPARATOR");
  IElementType VALUE = new MantisTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == PROPERTY) {
        return new MantisPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
