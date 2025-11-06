package APiRes.DTO

import jakarta.validation.constraints.*

/**
 * Représente les informations nécessaires à la création ou mise à jour d'une chambre.
 *
 * Cette classe sert de DTO (Data Transfer Object) pour transporter les données
 * entre le client et le serveur. Les validations assurent que les données
 * respectent les contraintes métier avant leur enregistrement.
 *
 * @property numero Numéro de la chambre (entre 1 et 9999).
 * @property prix Prix de la chambre (positif, maximum 999999.99).
 * @property typeChambre Type de la chambre : peut être **SIMPLE** ou **SUITE**.
 */
data class ChambreDTO(
    @field:Min(value = 1, message = "Le numéro de chambre doit être supérieur à 0")
    @field:Max(value = 9999, message = "Le numéro de chambre ne peut pas dépasser 9999")
    val numero: Int,

    @field:DecimalMin(value = "0.0", message = "Le prix doit être positif")
    @field:DecimalMax(value = "999999.99", message = "Le prix est trop élevé")
    val prix: Double,

    @field:NotBlank(message = "Le type de chambre est obligatoire")
    @field:Pattern(regexp = "^(SIMPLE|SUITE)$", message = "Type de chambre invalide")
    val typeChambre: String
)
