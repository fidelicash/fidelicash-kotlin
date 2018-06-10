package br.com.fidelicash.model

import net.corda.core.serialization.CordaSerializable
import java.util.*

@CordaSerializable
data class Transfer(val from : UserEntityAccount, val to : UserEntityAccount, val value : Int, val transferDate: Date) { }