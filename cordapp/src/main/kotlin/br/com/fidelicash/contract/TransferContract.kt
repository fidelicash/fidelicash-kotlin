package br.com.fidelicash.contract

import br.com.fidelicash.state.UserEntityAccountState
import net.corda.core.contracts.*
import net.corda.core.contracts.Requirements.using
import net.corda.core.transactions.LedgerTransaction

const val TRANSFER_CONTRACT_ID = "br.com.fidelicash.Transfer"

class TransferContract: Contract {

    class Send: TypeOnlyCommandData()

    override fun verify(tx: LedgerTransaction) {
        "Nao pode nao haver inputs." using (tx.inputs.isEmpty())
        val transf = tx.outputsOfType<UserEntityAccountState>().single()
        "Nao pode haver transferencia para o mesmo titular" using (transf.from.taxID != transf.to.taxID)
    }
}