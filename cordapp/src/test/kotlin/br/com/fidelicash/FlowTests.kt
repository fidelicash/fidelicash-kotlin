package br.com.fidelicash

import br.com.fidelicash.flow.TransferFlow
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.StartedMockNode
import org.junit.After
import org.junit.Before
import org.junit.Test

class FlowTests {
    lateinit var network: MockNetwork
    lateinit var a: StartedMockNode
    lateinit var b: StartedMockNode

    @Before
    fun setup() {
        network = MockNetwork(listOf("br.com.fidelicash"))
        a = network.createNode()
        b = network.createNode()
        listOf(a, b).forEach {
            it.registerInitiatedFlow(TransferFlow::class.java)
        }
        network.runNetwork()
    }

    @After
    fun tearDown() {
        network.stopNodes()
    }

    @Test
    fun `dummy test`() {

    }
}