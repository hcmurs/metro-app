package org.com.hcmurs

data class FareMatrixResponse(
    val status: Int,
    val message: String,
    val data: List<FareMatrix>
)

// Represents a single fare matrix entry
data class FareMatrix(
    val fareMatrixId: Int,
    val name: String,
    val price: Int,
    val startStationId: Int,
    val endStationId: Int,
    val createdAt: String,
    val updatedAt: String
)
