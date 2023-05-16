package data.model

data class Location (
    val institution: String,
    val city: String,
    val street: String
) {
    override fun toString(): String {
        return "$institution, $city, $street "
    }
}
