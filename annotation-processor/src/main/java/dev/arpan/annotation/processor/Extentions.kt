package dev.arpan.annotation.processor

import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.PackageElement

fun Element.getPackage(): PackageElement {
    var element: Element = this
    while (element.kind != ElementKind.PACKAGE) {
        element = element.enclosingElement
    }
    return element as PackageElement
}

fun Element.getPackageQName() = getPackage().qualifiedName.toString()

fun Element.name() = simpleName.toString()

fun Element.simpleNames(): List<String> {
    val simpleNames = mutableListOf<String>()
    var element: Element = this
    while (element.kind != ElementKind.PACKAGE) {
        simpleNames.add(element.name())
        element = element.enclosingElement
    }
    return simpleNames.reversed()
}

fun Element.isPublic() = modifiers.contains(Modifier.PUBLIC)
