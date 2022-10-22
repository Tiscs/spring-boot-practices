package io.github.tiscs.sbp.snowflake

// 2020-01-01T00:00:00.000Z
const val DEFAULT_ID_EPOCH = 1577836800000L
const val LOWER_HEX_FORMAT = "%016x"
const val UPPER_HEX_FORMAT = "%016X"

class IdWorker(
    private val clusterId: Long,
    private val workerId: Long,
    private val clusterIdBits: Int = 5,
    private val workerIdBits: Int = 5,
    private val sequenceBits: Int = 12,
    private val idEpoch: Long = DEFAULT_ID_EPOCH,
) {
    private val sequenceMask: Long = (-1L shl sequenceBits).inv()

    private var sequence: Long = 0
    private var lastMillis: Long = 0

    init {
        val maxClusterId = (-1L shl clusterIdBits).inv()
        val maxWorkerId = (-1L shl workerIdBits).inv()
        // Preconditions
        require(clusterId in 0..maxClusterId) {
            """The value of "clusterId" must be in the range 0 to $maxClusterId."""
        }
        require(workerId in 0..maxWorkerId) {
            """The value of "workerId" must be in the range 0 to $maxWorkerId."""
        }
        require(idEpoch > 0) {
            """The value of "idEpoch" must be greater than 0."""
        }
        require(clusterIdBits > 0) {
            """The value of "clusterIdBits" must be greater than 0."""
        }
        require(workerIdBits > 0) {
            """The value of "workerIdBits" must be greater than 0."""
        }
        require(sequenceBits > 0) {
            """The value of "sequenceBits" must be greater than 0."""
        }
        require(clusterIdBits + workerIdBits + sequenceBits < 23) {
            """The sum of "clusterIdBits" "workerIdBits" and "sequenceBits" must be less than 23."""
        }
    }

    @Synchronized
    private fun timeMillis(): Long {
        var timeMillis = System.currentTimeMillis()
        if (timeMillis == lastMillis) {
            sequence = sequence + 1L and sequenceMask
            if (sequence == 0L) {
                var nextMillis = System.currentTimeMillis()
                while (nextMillis <= lastMillis) {
                    nextMillis = System.currentTimeMillis()
                }
                timeMillis = nextMillis
            }
        } else {
            sequence = 0L
        }
        lastMillis = timeMillis
        return timeMillis
    }

    fun nextLong(): Long {
        return (timeMillis() - idEpoch shl clusterIdBits + workerIdBits + sequenceBits) or
                (clusterId shl workerIdBits + sequenceBits) or
                (workerId shl sequenceBits) or
                sequence
    }

    fun nextHex(lowerCase: Boolean = true): String {
        return (if (lowerCase) LOWER_HEX_FORMAT else UPPER_HEX_FORMAT).format(nextLong())
    }
}
