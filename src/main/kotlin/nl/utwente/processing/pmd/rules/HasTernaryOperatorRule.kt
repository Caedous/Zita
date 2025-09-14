package nl.utwente.processing.pmd.rules

import net.sourceforge.pmd.RuleContext
import net.sourceforge.pmd.lang.ast.Node
import net.sourceforge.pmd.lang.java.ast.ASTConditionalExpression
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule

/**
 * Rule that checks whether any ternary (conditional) operators are used in the sketch.
 * Example: int result = (a > b) ? a : b;
 */
class HasTernaryOperatorRule : AbstractJavaRule() {

    private var referenceNode: Node? = null

    override fun visit(node: ASTConditionalExpression, data: Any?): Any? {
        if (referenceNode == null) {
            referenceNode = node
        }
        return super.visit(node, data)
    }

    override fun end(ctx: RuleContext?) {
        if (ctx != null && referenceNode != null) {
            addViolationWithMessage(
                ctx,
                referenceNode,
                message,
                referenceNode!!.beginLine,
                referenceNode!!.endLine
            )
        }
        super.end(ctx)
    }
}