package nl.utwente.processing.pmd.rules

import net.sourceforge.pmd.RuleContext
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule

/**
 * Rule that flags if there are no functions with a return type other than void.
 */
class HasNonVoidFunctionRule : AbstractJavaRule() {

    private var foundNonVoidFunction = false
    private var compilationUnit: ASTCompilationUnit? = null

    override fun visit(node: ASTCompilationUnit, data: Any?): Any? {
        compilationUnit = node
        return super.visit(node, data)
    }

    override fun visit(node: ASTMethodDeclaration, data: Any?): Any? {
        if (!node.isVoid) {
            foundNonVoidFunction = true
        }
        return super.visit(node, data)
    }

    override fun end(ctx: RuleContext?) {
        if (!foundNonVoidFunction && compilationUnit != null && ctx != null) {
            addViolationWithMessage(
                ctx,
                compilationUnit!!,
                message,
                0,
                0
                )
        }
        super.end(ctx)
    }
}