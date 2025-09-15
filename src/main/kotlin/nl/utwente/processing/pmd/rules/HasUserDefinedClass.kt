package nl.utwente.processing.pmd.rules

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule

class HasUserDefinedClass: AbstractJavaRule() {

    private var checkedOuter = false

    override fun visit(node: ASTClassOrInterfaceDeclaration?, data: Any?): Any? {
        if (node == null || checkedOuter || node.isNested) return super.visit(node, data)

        // This is the outermost class
        checkedOuter = true

        val hasInnerClass = node.findDescendantsOfType(ASTClassOrInterfaceDeclaration::class.java)
            .any { it != node } // Exclude the outer class itself


        if (!hasInnerClass) {
            addViolationWithMessage(data, node,message,0,0)
        }

        return super.visit(node, data)
    }
}