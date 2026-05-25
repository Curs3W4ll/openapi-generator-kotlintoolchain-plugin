package io.gitlab.curs3w4ll.openapi.generator.kotlintoolchain.plugin.settings

import org.jetbrains.amper.plugins.Configurable

@Configurable
interface Settings {
    /**
     * The Open API specification location.
     */
    val inputSpec: String

    /**
     * The name of the generator which will handle codegen.
     */
    val generatorName: String
}
