// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.intellij.sdk.language.psi.MantisTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.intellij.sdk.language.psi.*;

public class MantisPropertyImpl extends ASTWrapperPsiElement implements MantisProperty {

  public MantisPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MantisVisitor visitor) {
    visitor.visitProperty(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MantisVisitor) accept((MantisVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public String getKey() {
    return MantisPsiImplUtil.getKey(this);
  }

  @Override
  public String getValue() {
    return MantisPsiImplUtil.getValue(this);
  }

}
