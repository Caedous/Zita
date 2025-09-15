package nl.utwente.processing.pmd.rules

import net.sourceforge.pmd.RuleContext
import net.sourceforge.pmd.lang.ast.Node
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule

class HasThisKeywordRule : AbstractJavaRule() {

    private var referenceNode: Node? = null

    override fun visit(node: ASTPrimaryExpression?, data: Any?): Any? {
        val prefix = node?.getFirstChildOfType(ASTPrimaryPrefix::class.java)
        if (prefix?.usesThisModifier() == true && referenceNode == null) {
            referenceNode = node
        }
        return super.visit(node, data)
    }

    override fun end(ctx: RuleContext?) {
        val node = referenceNode
        if (ctx != null && node != null) {
            val message = "Submission uses 'this' keyword. Consider asking the student about its purpose."
            addViolationWithMessage(ctx, node, message, node.beginLine, node.endLine)
        }
        super.end(ctx)
    }
}