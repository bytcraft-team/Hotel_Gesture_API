package APiRes.DTO

import jakarta.validation.constraints.*

/**
 * Représente les données nécessaires à la création ou mise à jour d’un client VIP.
 *
 * Hérite des caractéristiques d’un client classique avec un champ supplémentaire
 * pour la remise appliquée aux réservations.
 *
 * @property nom Nom du client VIP.
 * @property prenom Prénom du client VIP.
 * @property email Adresse email valide.
 * @property telephone Numéro de téléphone valide.
 * @property remise Pourcentage de remise appliqué (0.0 à 1.0).
 */
data class ClientVIPDTO(
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
    val telephone: String,

    @field:DecimalMin(value = "0.0", message = "La remise doit être positive")
    @field:DecimalMax(value = "1.0", message = "La remise ne peut pas dépasser 100%")
    val remise: Double = 0.15
)
