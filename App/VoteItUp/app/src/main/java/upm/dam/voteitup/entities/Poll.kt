package upm.dam.voteitup.entities

data class Poll(
        val id: String? = null,
        val creationDate: String? = null,
        val text: String? = null,
        val Area: Area? = null,
        val Choices: List<Choice>? = null,
        val UserId: String? = null
)