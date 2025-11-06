package APiRes.DTO

import jakarta.validation.constraints.*

/**
 * Représente les données nécessaires à la création ou mise à jour d’un client.
 *
 * Ce DTO permet de transférer les informations client avec validation des champs
 * essentiels tels que le nom, le prénom, l’adresse email et le téléphone.
 *
 * @property nom Nom du client (2 à 50 caractères).
 * @property prenom Prénom du client (2 à 50 caractères).
 * @property email Adresse email valide.
 * @property telephone Numéro de téléphone au format international.
 */
data class ClientDTO(
    @field:NotBlank(message = "Le nom est obligatoire")
    @field:Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    val nom: String,

    @field:NotBlank(message = "Le prénom est obligatoire")
    @field:Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    val prenom: String,

    @field:NotBlank(message = "L'email est obligatoire")
    @field:Email(message = "Format d'email invalide")
    val email: String,

    @field:NotBlank(message = "Le téléphone est obligatoire")
    @field:Pattern(regexp = "^[0-9+\\-\\s()]{10,20}$", message = "Format de téléphone invalide")
    val telephone: String
)
