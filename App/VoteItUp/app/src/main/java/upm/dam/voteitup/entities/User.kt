package upm.dam.voteitup.entities

data class User(
        val email: String,
        val username: String,
        val password: String,
        val bio: String?
)