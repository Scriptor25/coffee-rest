package dev.scriptor.reflect.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

@OptIn(ExperimentalCompilerApi::class)
class ReflectPluginRegistrar : CompilerPluginRegistrar() {

    override val pluginId: String = "dev.scriptor:reflect-plugin"
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(
        configuration: CompilerConfiguration,
    ) {
        println("Hello world from reflect plugin registrar!")

        IrGenerationExtension.registerExtension(ReflectPluginExtension())
    }
}
