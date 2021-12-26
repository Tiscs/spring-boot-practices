package io.github.tiscs.sbp.sequence

interface SequenceProvider {
    fun nextValue(): Long
}
