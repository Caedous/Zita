package nl.utwente.processing.pmd.rules

import net.sourceforge.pmd.RuleContext
import net.sourceforge.pmd.lang.ast.Node
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule

/**
 * Rule that flags the use of the 'final' keyword on fields.
 * Using 'final' may be unnecessary or confusing for beginners in Processing assignments.
 */
class HasFinalVariableRule : AbstractJavaRule() {

    private var referenceNode: Node? = null

    override fun visit(node: ASTFieldDeclaration?, data: Any?): Any? {
        if (node?.isFinal == true && referenceNode == null) {
            referenceNode = node
        }
        return super.visit(node, data)
    }

    override fun end(ctx: RuleContext?) {
        referenceNode?.let {
            addViolationWithMessage(
                ctx,
                it,
                 message,
                it.beginLine, it.endLine
            )
        }
        super.end(ctx)
    }
}