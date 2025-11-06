package APiRes.DTO

import jakarta.validation.constraints.*

/**
 * Représente les informations nécessaires à la création ou mise à jour d’un employé.
 *
 * Ce DTO est utilisé pour valider et transférer les données relatives à un employé,
 * notamment son nom, son poste et son salaire.
 *
 * @property nom Nom complet de l’employé.
 * @property poste Poste ou fonction occupée.
 * @property salaire Salaire de l’employé (positif, maximum 999999.99).
 */
data class EmployeeDTO(
    @field:NotBlank(message = "Le nom est obligatoire")
    @field:Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    val nom: String,

    @field:NotBlank(message = "Le poste est obligatoire")
    @field:Size(min = 2, max = 50, message = "Le poste doit contenir entre 2 et 50 caractères")
    val poste: String,

    @field:DecimalMin(value = "0.0", message = "Le salaire doit être positif")
    @field:DecimalMax(value = "999999.99", message = "Le salaire est trop élevé")
    val salaire: Double
)
