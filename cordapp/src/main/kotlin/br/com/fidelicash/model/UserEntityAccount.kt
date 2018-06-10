package br.com.fidelicash.model

import net.corda.core.serialization.CordaSerializable

@CordaSerializable
data class UserEntityAccount(val taxID : String, val balance : Int) { }