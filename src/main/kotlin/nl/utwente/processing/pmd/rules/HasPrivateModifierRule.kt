package nl.utwente.processing.pmd.rules

import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule
import net.sourceforge.pmd.RuleContext
import net.sourceforge.pmd.lang.ast.Node

/// Rule that flags the use of the 'private' access modifier on variables (fields) and functions (methods)
class HasPrivateModifierRule : AbstractJavaRule() {
    private var found = false
    private var referenceNode: Node? = null

    override fun visit(node: ASTFieldDeclaration?, data: Any?): Any? {
        if (node != null && node.isPrivate) {
            found = true
            if (referenceNode == null) referenceNode = node
        }
        return super.visit(node, data)
    }

    override fun visit(node: ASTMethodDeclaration?, data: Any?): Any? {
        if (node != null && node.isPrivate) {
            found = true
            if (referenceNode == null) referenceNode = node
        }
        return super.visit(node, data)
    }

    override fun end(ctx: RuleContext?) {
        val node = referenceNode
        if (node != null && ctx != null) {
            addViolationWithMessage(
                ctx,
                node,
                message,
                node.beginLine,
                node.endLine
            )
        }
    }
    }

