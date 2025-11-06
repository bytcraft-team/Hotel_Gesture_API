package APiRes.DTO

import jakarta.validation.constraints.*
import java.time.LocalDate

/**
 * Représente les données d’une réservation effectuée en ligne.
 *
 * Ce DTO permet de gérer les réservations réalisées via une plateforme numérique
 * en incluant la source de la réservation et une éventuelle remise.
 *
 * @property dateDebut Date de début de la réservation.
 * @property dateFin Date de fin de la réservation.
 * @property clientId Identifiant du client.
 * @property chambreId Identifiant de la chambre réservée.
 * @property plateforme Nom de la plateforme de réservation (par défaut "SiteWeb").
 * @property remise Remise appliquée (0.0 à 1.0).
 */
data class ReservationOnlineDTO(
    @field:NotNull(message = "La date de début est obligatoire")
    @field:FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
    val dateDebut: LocalDate,

    @field:NotNull(message = "La date de fin est obligatoire")
    @field:Future(message = "La date de fin doit être dans le futur")
    val dateFin: LocalDate,

    @field:NotNull(message = "L'ID du client est obligatoire")
    val clientId: Int,

    @field:NotNull(message = "L'ID de la chambre est obligatoire")
    val chambreId: Int,

    @field:NotBlank(message = "La plateforme est obligatoire")
    val plateforme: String = "SiteWeb",

    @field:DecimalMin(value = "0.0", message = "La remise doit être positive")
    @field:DecimalMax(value = "1.0", message = "La remise ne peut pas dépasser 100%")
    val remise: Double = 0.0
)
