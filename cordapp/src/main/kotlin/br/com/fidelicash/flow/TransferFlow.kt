package br.com.fidelicash.flow

import br.com.fidelicash.contract.TransferContract
import br.com.fidelicash.model.UserEntityAccount
import br.com.fidelicash.state.UserEntityAccountState
import co.paralleluniverse.fibers.Suspendable
import net.corda.core.contracts.Command
import net.corda.core.contracts.StateAndContract
import net.corda.core.flows.*
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

@InitiatingFlow
@StartableByRPC
class TransferFlow (    val from: UserEntityAccount,
                        val to: UserEntityAccount,
                        val value: Int) : FlowLogic<SignedTransaction>() {

    override val progressTracker : ProgressTracker = tracker()

    companion object {
        object CREATING : ProgressTracker.Step("Creating a new transfer amount")
        object SIGNING : ProgressTracker.Step("Verifying the transfer amount")
        object VERIFYING : ProgressTracker.Step("Verifying the transfer amount")
        object FINALISING : ProgressTracker.Step("Sending the transfer amount") {
            override fun childProgressTracker() = FinalityFlow.tracker()
        }

        fun tracker() = ProgressTracker(CREATING, SIGNING, VERIFYING, FINALISING)
    }

    @Suspendable
    override fun call(): SignedTransaction {
        progressTracker.currentStep = CREATING
        val me = serviceHub.myInfo.legalIdentities.first()
        val notary = serviceHub.networkMapCache.notaryIdentities.single()
        val command = Command(TransferContract.Send(), listOf(me.owningKey))
        val state = UserEntityAccountState(serviceHub.myInfo.legalIdentities.first(), from, to, value)
        val stateAndContract = StateAndContract(state, TransferContract::class.java.canonicalName)
        val utx = TransactionBuilder(notary = notary).withItems(stateAndContract, command)

        progressTracker.currentStep = SIGNING
        val stx = serviceHub.signInitialTransaction(utx)

        progressTracker.currentStep = VERIFYING
        stx.verify(serviceHub)

        progressTracker.currentStep = FINALISING
        return subFlow(FinalityFlow(stx, FINALISING.childProgressTracker()))
    }
}

