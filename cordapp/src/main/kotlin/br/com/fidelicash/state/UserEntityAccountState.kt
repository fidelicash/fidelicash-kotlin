package br.com.fidelicash.state

import br.com.fidelicash.model.Transfer
import br.com.fidelicash.model.UserEntityAccount
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import net.corda.core.serialization.CordaSerializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@CordaSerializable
data class UserEntityAccountState(  val nodeHost: Party,
                                    val from: UserEntityAccount,
                                    val to: UserEntityAccount,
                                    val value: Int) : ContractState, QueryableState {
    override val participants get() = listOf(nodeHost)
    override fun toString() = "${from.taxID}: $value"
    override fun supportedSchemas() = listOf(TransferSchemaV1)
    override fun generateMappedObject(schema: MappedSchema) = TransferSchemaV1.PersistentTransferState(from.taxID, to.taxID, value)

    object TransferSchema

    object TransferSchemaV1 : MappedSchema(TransferSchema.javaClass, 1, listOf(PersistentTransferState::class.java)) {
        @Entity
        @Table(name = "transferencias")
        class PersistentTransferState (
                @Column(name = "from")
                var from: String = "",
                @Column(name = "to")
                var to: String = "",
                @Column(name = "value")
                var value: Int = 0

        ) : PersistentState()
    }
}