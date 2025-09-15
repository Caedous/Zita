package nl.utwente.processing.pmd.rules

import net.sourceforge.pmd.RuleContext
import net.sourceforge.pmd.lang.ast.Node
import net.sourceforge.pmd.lang.java.ast.ASTForStatement
import net.sourceforge.pmd.lang.java.ast.ASTWhileStatement
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule

class HasLoopRule : AbstractJavaRule() {

    private var hasLoop = false
    private var firstNode: Node? = null

    override fun visit(node: ASTCompilationUnit, data: Any?): Any? {
        if (firstNode == null) firstNode = node
        return super.visit(node, data)
    }

    override fun visit(node: ASTForStatement, data: Any?): Any? {
        hasLoop = true
        return super.visit(node, data)
    }

    override fun visit(node: ASTWhileStatement, data: Any?): Any? {
        hasLoop = true
        return super.visit(node, data)
    }

    override fun end(ctx: RuleContext) {
        if (!hasLoop && firstNode != null) {
            addViolationWithMessage(
                ctx,
                firstNode!!,
                message,
                0,
                0
            )
        }
        super.end(ctx)
    }
}