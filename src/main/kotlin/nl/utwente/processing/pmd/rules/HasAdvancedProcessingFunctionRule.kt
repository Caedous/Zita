package nl.utwente.processing.pmd.rules

import net.sourceforge.pmd.RuleContext
import net.sourceforge.pmd.lang.ast.Node
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix
import net.sourceforge.pmd.lang.java.ast.ASTName
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule

/**
 * Flags use of advanced Processing functions like translate, rotate, and matrix operations.
 * Useful for tutors to quickly find places to quiz students on more complex concepts.
 */
class HasAdvancedProcessingFunctionRule : AbstractJavaRule() {

    private val advancedFunctions = setOf(
        "shearX", "shearY", "translate",
        "vertex", "bezierVertex", "curveVertex",
        "quadraticVertex","costrain",
        "pushMatrix",
        "beginShape", "endShape",
        "curveVertex", "bezierVertex",
        "texture", "textureMode", "get", "set", "loadPixels", "updatePixels",
        "frameRate", "frameCount",
        "loadStrings", "saveStrings", "loadTable", "saveTable",
        "loadJSONObject", "saveJSONObject", "loadXML", "saveXML"
    )
    private var firstMatch: Pair<Node, String>? = null

    override fun visit(node: ASTPrimaryExpression?, data: Any?): Any? {
        if (node == null) return data

        val prefix = node.getFirstChildOfType(ASTPrimaryPrefix::class.java)
        val nameNode = prefix?.getFirstChildOfType(ASTName::class.java)
        val methodName = nameNode?.image?.substringAfterLast(".")

        if (firstMatch == null && methodName != null && methodName in advancedFunctions) {
            firstMatch = node to methodName
        }

        return super.visit(node, data)
    }

    override fun end(ctx: RuleContext?) {
        if (ctx != null && firstMatch != null) {
            val (node, method) = firstMatch!!
            val msg = "Submission uses '$method'. Consider asking the student about its purpose."
            addViolationWithMessage(ctx, node, msg, node.beginLine, node.endLine)
        }
        super.end(ctx)
    }
}