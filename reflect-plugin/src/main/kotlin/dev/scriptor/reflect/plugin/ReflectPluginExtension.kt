package dev.scriptor.reflect.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.dump

class ReflectPluginExtension : IrGenerationExtension {

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun generate(
        moduleFragment: IrModuleFragment,
        pluginContext: IrPluginContext,
    ) {
        println("Hello world from reflect plugin extension!")

        for (file in moduleFragment.files) {
            println("in ${file.fileEntry.name}:")

            for (declaration in file.declarations) {
                print(" - ")
                println(declaration.dump())
            }
        }
    }
}
