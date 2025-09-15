package nl.utwente.processing.pmd.rules

import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule
import net.sourceforge.pmd.RuleContext
import nl.utwente.processing.pmd.symbols.ProcessingApplet
import nl.utwente.processing.pmd.symbols.ProcessingAppletMethodCategory
import nl.utwente.processing.pmd.utils.matches
import net.sourceforge.pmd.lang.ast.Node

class Has2DShapesRule : AbstractJavaRule() {

    private var shape2DCount = 0
    private var firstNode: Node? = null
    private var firstShapeNode: Node? = null

    override fun visit(node: ASTPrimaryExpression, data: Any): Any? {
        if (firstNode == null) {
            firstNode = node
        }

        if (
            ProcessingApplet.DRAW_METHODS
                .filter { it.category == ProcessingAppletMethodCategory.SHAPE_2D }
                .any { shapeMethod -> node.matches(shapeMethod) }
        ) {
            shape2DCount++
            if (firstShapeNode == null) {
                firstShapeNode = node
            }
        }

        return super.visit(node, data)
    }

    override fun end(ctx: RuleContext?) {
        if (ctx != null && shape2DCount < 2) {
            val nodeToReport = firstShapeNode ?: firstNode
            if (nodeToReport != null) {
                addViolationWithMessage(ctx, nodeToReport, message, 0, 0)
            }
        }
        super.end(ctx)
    }
}