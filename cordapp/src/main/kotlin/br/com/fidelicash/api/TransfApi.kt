package br.com.fidelicash.api

import br.com.fidelicash.flow.TransferFlow
import br.com.fidelicash.model.UserEntityAccount
import br.com.fidelicash.state.UserEntityAccountState
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.utilities.getOrThrow
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("transfer")
class TransfApi(val rpcOps: CordaRPCOps) {

    @GET
    @Path("transfer")
    fun doTransfer(@QueryParam("from") from: String,
                   @QueryParam("to") to: String,
                   @QueryParam("value") value: Int): Response {
        val (status, message) = try {
            val objFrom = UserEntityAccount(from, 0)
            val objTo = UserEntityAccount(to, 0)
            val result = rpcOps.startFlowDynamic(TransferFlow::class.java, objFrom, objTo, value).returnValue.getOrThrow()
            Response.Status.CREATED to "Transferencia efetuada. ID: ${result.tx.id}"
        } catch (e: Exception) {
            Response.Status.BAD_GATEWAY to e.message
        }
        return Response.status(status).entity(message).build()
    }

    @GET
    @Path("amounts")
    @Produces(MediaType.APPLICATION_JSON)
    fun amounts() = rpcOps.vaultQuery(UserEntityAccountState::class.java).states

    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    fun me() = mapOf("me" to rpcOps.nodeInfo().legalIdentities.first().name)

    @GET
    @Path("peers")
    @Produces(MediaType.APPLICATION_JSON)
    fun peers() = mapOf("peers" to rpcOps.networkMapSnapshot().map { it.legalIdentities.first().name })
}