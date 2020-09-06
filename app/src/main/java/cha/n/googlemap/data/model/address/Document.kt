package cha.n.googlemap.data.model.address

data class Document(
    val address: Address,
    val address_name: String,
    val address_type: String,
    val road_address: RoadAddress,
    val x: String,
    val y: String
)