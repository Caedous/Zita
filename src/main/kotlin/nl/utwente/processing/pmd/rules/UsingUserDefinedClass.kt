package nl.utwente.processing.pmd.rules
import net.sourceforge.pmd.RuleContext
import net.sourceforge.pmd.lang.ast.Node
import net.sourceforge.pmd.lang.java.ast.*
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule

class UsingUserDefinedClass: AbstractJavaRule() {

    private var outerClassName: String? = null

    private val definedClasses = mutableSetOf<String>()
    private val classesWithConstructors = mutableSetOf<String>()
    private val classesUsedWithNew = mutableSetOf<String>()
    private var referenceNode: Node? = null

    override fun visit(node: ASTClassOrInterfaceDeclaration?, data: Any?): Any? {
        if (node == null || node.isInterface) return data

        if (outerClassName == null) {
            // First class = outer Processing wrapper
            outerClassName = node.image
            referenceNode = node
        } else {
            val className = node.image
            definedClasses.add(className)

            val hasConstructor = node
                .findDescendantsOfType(ASTConstructorDeclaration::class.java)
                .any { it.image == null || it.image == className }

            if (hasConstructor) {
                classesWithConstructors.add(className)
            }
        }

        return super.visit(node, data)
    }

    override fun visit(node: ASTAllocationExpression?, data: Any?): Any? {
        val type = node?.getFirstChildOfType(ASTClassOrInterfaceType::class.java)
        val typeName = type?.image
        if (typeName != null) {
            classesUsedWithNew.add(typeName)
        }
        return super.visit(node, data)
    }

    override fun end(ctx: RuleContext?) {
        if (ctx == null || referenceNode == null) return



        for (className in definedClasses) {
            val hasConstructor = className in classesWithConstructors
            val wasUsed = className in classesUsedWithNew

            if (hasConstructor && !wasUsed) {
                addViolationWithMessage(
                    ctx,
                    referenceNode,
                    "Your Submission has a Class: '$className' with a constructor but was never used. Please consider using the class in your code and reuploading your submission before your viva.",
                    0, 0
                )
            } else if (!hasConstructor && !wasUsed) {
                addViolationWithMessage(
                    ctx,
                    referenceNode,
                    "Your submission has a Class: '$className',but has no user-defined constructor and was never used. Please consider adding a constructor to the class and reuploading your submission before your viva.",
        
                    0, 0
                )
            }
        }

        super.end(ctx)
    }
}
