package nl.utwente.processing.pmd.rules

import net.sourceforge.pmd.lang.ast.Node
import net.sourceforge.pmd.lang.java.ast.*
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule
import net.sourceforge.pmd.RuleContext

class VariableArithmeticRule : AbstractJavaRule() {

    private var meaningfulArithmeticCount = 0
    private var referenceNode: Node? = null
    private var firstSeenNode: Node? = null

    override fun visit(node: ASTAdditiveExpression, data: Any?): Any? {
        if (firstSeenNode == null) firstSeenNode = node

        if (hasVariableOperand(node)) {
            meaningfulArithmeticCount++
            if (referenceNode == null) referenceNode = node
        }
        return super.visit(node, data)
    }

    override fun visit(node: ASTMultiplicativeExpression, data: Any?): Any? {
        if (firstSeenNode == null) firstSeenNode = node

        if (hasVariableOperand(node)) {
            meaningfulArithmeticCount++
            if (referenceNode == null) referenceNode = node
        }
        return super.visit(node, data)
    }

    override fun visit(node: ASTExpression, data: Any?): Any? {
        if (firstSeenNode == null) firstSeenNode = node

        val operator = node.getFirstDescendantOfType(ASTAssignmentOperator::class.java)?.image
        if (operator in listOf("+=", "-=", "*=", "/=", "%=")) {
            if (hasVariableOperand(node)) {
                meaningfulArithmeticCount++
                if (referenceNode == null) referenceNode = node
            }
        }
        return super.visit(node, data)
    }

    override fun visit(node: ASTUnaryExpression, data: Any?): Any? {
        if (firstSeenNode == null) firstSeenNode = node

        val operator = node.image
        if (operator == "++" || operator == "--") {
            meaningfulArithmeticCount++
            if (referenceNode == null) referenceNode = node
        }
        return super.visit(node, data)
    }

    override fun end(ctx: RuleContext?) {
        if (ctx != null && meaningfulArithmeticCount < 2) {
            val fallbackNode = referenceNode ?: firstSeenNode
            if (fallbackNode != null) {
                addViolationWithMessage(
                    ctx,
                    fallbackNode,
                    message,
                    0,
                    0
                )
            }
        }
        super.end(ctx)
    }

    private fun hasVariableOperand(node: JavaNode): Boolean {
        return node.findDescendantsOfType(ASTName::class.java).isNotEmpty()
    }
}