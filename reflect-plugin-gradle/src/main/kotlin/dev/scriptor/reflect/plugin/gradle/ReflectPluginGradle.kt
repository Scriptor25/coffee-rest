package dev.scriptor.reflect.plugin.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@Suppress("unused")
class ReflectPluginGradle : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project) {
        target.extensions.create("reflect-plugin-gradle", ReflectPluginGradleExtension::class.java)
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    override fun getCompilerPluginId(): String = "dev.scriptor:reflect-plugin"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        "dev.scriptor",
        "reflect-plugin",
        "1.0.0",
    )

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>,
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project

        kotlinCompilation.compileTaskProvider.configure {
            it.compilerOptions.freeCompilerArgs.add("-Xplugin=dev.scriptor:reflect-plugin")
        }

        return project.provider {
            val extension = project.extensions.getByType(ReflectPluginGradleExtension::class.java)

            emptyList()
        }
    }
}
