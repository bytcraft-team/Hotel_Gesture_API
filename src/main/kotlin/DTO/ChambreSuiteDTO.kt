package APiRes.DTO

import jakarta.validation.constraints.*

/**
 * Représente les informations nécessaires à la création ou mise à jour d'une suite.
 *
 * Ce DTO permet de valider et transférer les données d'une suite avant leur
 * conversion en entité persistée.
 *
 * @property numero Numéro de la suite (minimum 1).
 * @property prix Prix de la suite, doit être positif.
 * @property suiteNom Nom de la suite, entre 2 et 100 caractères.
 * @property nombrePieces Nombre de pièces dans la suite (1 à 20).
 * @property jacuzzi Indique si la suite dispose d’un jacuzzi.
 */
data class ChambreSuiteDTO(
    @field:Min(value = 1, message = "Le numéro de chambre doit être supérieur à 0")
    val numero: Int,

    @field:DecimalMin(value = "0.0", message = "Le prix doit être positif")
    val prix: Double,

    @field:NotBlank(message = "Le nom de la suite est obligatoire")
    @field:Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    val suiteNom: String,

    @field:Min(value = 1, message = "Le nombre de pièces doit être au moins 1")
    @field:Max(value = 20, message = "Le nombre de pièces ne peut pas dépasser 20")
    val nombrePieces: Int = 2,

    val jacuzzi: Boolean = false
)
