package br.com.fidelicash.plugin

import br.com.fidelicash.api.TransfApi
import net.corda.webserver.services.WebServerPluginRegistry
import java.util.function.Function

class TransferWebPlugin : WebServerPluginRegistry {
    override val webApis = listOf(Function(::TransfApi))
    override val staticServeDirs = mapOf("transfer" to javaClass.classLoader.getResource("transferWeb").toExternalForm())
}