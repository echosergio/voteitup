package upm.dam.voteitup.entities

data class PollActivity(
        val votes: Int = 0,
        val date: String
)
data class UserActivity(
        val id: String,
        val text: String,
        val choice: String
)
