package APiRes.DTO

import jakarta.validation.constraints.*
import java.time.LocalDate

/**
 * Représente les données nécessaires à la création ou mise à jour d’une réservation.
 *
 * Ce DTO est utilisé pour valider les dates, le client, la chambre et éventuellement
 * l’employé associé à la réservation avant traitement.
 *
 * @property dateDebut Date de début de la réservation (présente ou future).
 * @property dateFin Date de fin de la réservation (future).
 * @property clientId Identifiant du client associé.
 * @property chambreId Identifiant de la chambre réservée.
 * @property employeId Identifiant optionnel de l’employé responsable.
 */
data class ReservationDTO(
    @field:NotNull(message = "La date de début est obligatoire")
    @field:FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
    val dateDebut: LocalDate,

    @field:NotNull(message = "La date de fin est obligatoire")
    @field:Future(message = "La date de fin doit être dans le futur")
    val dateFin: LocalDate,

    @field:NotNull(message = "L'ID du client est obligatoire")
    @field:Min(value = 1, message = "L'ID du client doit être valide")
    val clientId: Int,

    @field:NotNull(message = "L'ID de la chambre est obligatoire")
    @field:Min(value = 1, message = "L'ID de la chambre doit être valide")
    val chambreId: Int,

    @field:Min(value = 1, message = "L'ID de l'employé doit être valide")
    val employeId: Int? = null
)
