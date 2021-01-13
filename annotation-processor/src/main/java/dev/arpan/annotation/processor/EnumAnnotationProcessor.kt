package dev.arpan.annotation.processor

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

class EnumAnnotationProcessor : AbstractProcessor() {

    companion object {
        private const val KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
        private val GENERATED_ANNOTATION_CLASS_NAME = ClassName("javax.annotation.processing", "Generated")
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        val directory = processingEnv.options[KAPT_KOTLIN_GENERATED]?.let { File(it) }
        if (directory == null || (!directory.exists() && directory.mkdir())) {
            throw Exception("Can't find the target directory for generated Kotlin files.")
        }
    }

    override fun getSupportedSourceVersion() = SourceVersion.RELEASE_8

    override fun getSupportedAnnotationTypes() = listOf(Key::class).mapTo(mutableSetOf(), { it.java.name })

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf(KAPT_KOTLIN_GENERATED)
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val enumDataMap: MutableMap<TypeElement, MutableList<EnumData>> = mutableMapOf()
        roundEnv.getElementsAnnotatedWith(Key::class.java)
            .asSequence()
            .forEach { elem ->
                if (elem.kind != ElementKind.ENUM_CONSTANT || elem.enclosingElement.kind != ElementKind.ENUM) {
                    error("@Key must be on enum constant found $elem")
                    return true
                }
                if (!elem.enclosingElement.isPublic()) {
                    error("The enum ${elem.enclosingElement} is not public")
                    return true
                }
                enumDataMap.getOrPut((elem.enclosingElement as TypeElement)) {
                    mutableListOf()
                }.add(
                    EnumData(
                        field = elem as VariableElement,
                        key = elem.getAnnotation(Key::class.java)
                    )
                )
            }
        enumDataMap.forEach { (typeElement, list) ->
            val isIgnoreCase = list.any { it.key.ignoreCase }
            val enumClassName = ClassName(typeElement.getPackageQName(), typeElement.name())
            val companion = TypeSpec.companionObjectBuilder()
                .addFunction(
                    FunSpec.builder("fromKey")
                        .addParameter("value", String::class)
                        .returns(enumClassName).apply {
                            if (isIgnoreCase) {
                                list.forEach { enumField ->
                                    addStatement(
                                        "if (value.equals(%S, ignoreCase = %L)) return %T.%L",
                                        enumField.key.value, enumField.key.ignoreCase, typeElement, enumField.field
                                    )
                                }
                                addStatement("throw IllegalArgumentException(\"Invalid value: \$value\")")
                            } else {
                                beginControlFlow("return when(value)")
                                list.forEach {
                                    addStatement("%S -> %T.%L", it.key.value, typeElement, it.field)
                                }
                                addStatement("else -> throw IllegalArgumentException(\"Invalid value: \$value\")")
                                endControlFlow()
                            }
                        }

                        .build()
                )
                .addFunction(
                    FunSpec.builder("toKey")
                        .addParameter("value", enumClassName)
                        .returns(String::class)
                        .beginControlFlow("return when(value)").apply {
                            list.forEach { enumField ->
                                addStatement("%T.%L -> %S", typeElement, enumField.field, enumField.key.value)
                            }
                            addStatement("else -> throw IllegalArgumentException(\"Invalid value: \$value\")")
                        }
                        .endControlFlow()
                        .build()
                ).build()

            val generatedAnnotationSpec = AnnotationSpec.builder(GENERATED_ANNOTATION_CLASS_NAME)
                .addMember("%S", EnumAnnotationProcessor::class.java.canonicalName).build()

            val classTypeSpec = TypeSpec.classBuilder("${typeElement.simpleName}TypeConverter")
                .addAnnotation(generatedAnnotationSpec)
                .addType(companion)
                .build()
            val fileSpec = FileSpec.builder(typeElement.getPackageQName(), "${typeElement.name()}TypeConverter")
                .addType(classTypeSpec)
                .build()

            fileSpec.writeTo(processingEnv.filer)
        }
        return true
    }

    private fun error(vararg args: Any?) {
        processingEnv.messager.printMessage(
            Diagnostic.Kind.ERROR,
            args.filterNotNull().joinToString(separator = " ") { it.toString() })
    }
}