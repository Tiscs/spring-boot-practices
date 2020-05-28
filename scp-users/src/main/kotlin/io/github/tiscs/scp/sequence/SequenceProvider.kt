package io.github.tiscs.scp.sequence

interface SequenceProvider {
    fun nextValue(): Long
}
