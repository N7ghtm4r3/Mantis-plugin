package org.intellij.sdk.language.psi.impl;

import com.intellij.lang.ASTNode;
import org.intellij.sdk.language.psi.MantisProperty;
import org.intellij.sdk.language.psi.MantisTypes;

public class MantisPsiImplUtil {

    public static String getKey(MantisProperty element) {
        ASTNode keyNode = element.getNode().findChildByType(MantisTypes.KEY);
        if (keyNode != null) {
            // IMPORTANT: Convert embedded escaped spaces to simple spaces
            return keyNode.getText().replaceAll("\\\\ ", " ");
        } else {
            return null;
        }
    }

    public static String getValue(MantisProperty element) {
        ASTNode valueNode = element.getNode().findChildByType(MantisTypes.VALUE);
        if (valueNode != null) {
            return valueNode.getText();
        } else {
            return null;
        }
    }

}