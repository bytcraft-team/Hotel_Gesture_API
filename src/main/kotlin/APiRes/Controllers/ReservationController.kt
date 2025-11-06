package APiRes.Controllers

import APiRes.DTO.ReservationDTO
import APiRes.DTO.ReservationOnlineDTO
import APiRes.Models.Reservation
import APiRes.Service.ReservationService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

/**
 * Contrôleur REST pour la gestion des réservations.
 *
 * Endpoints :
 * - Liste paginée, CRUD,
 * - Création classique et création en ligne (avec remise),
 * - Confirmer / annuler une réservation,
 * - Calculer le montant d'une réservation,
 * - Requêtes filtrées (statut, client, chambre, plage de dates).
 *
 * @property reservationService service métier gérant les réservations.
 */
@RestController
@RequestMapping("/api/reservations")
class ReservationController(private val reservationService: ReservationService) {

    /**
     * Récupère une page de réservations.
     *
     * @param page index de page (0 par défaut).
     * @param size taille (10 par défaut).
     * @param sortBy champ de tri par défaut "reservationId".
     * @return `ResponseEntity<Page<Reservation>>`.
     */
    @GetMapping
    fun getAllReservations(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "reservationId") sortBy: String
    ): ResponseEntity<Page<Reservation>> {
        val pageable = PageRequest.of(page, size, Sort.by(sortBy))
        return ResponseEntity.ok(reservationService.getAllReservations(pageable))
    }

    /**
     * Récupère une réservation par id.
     *
     * @param id identifiant de la réservation.
     * @return `ResponseEntity<Reservation>` (200).
     */
    @GetMapping("/{id}")
    fun getReservationById(@PathVariable id: Int): ResponseEntity<Reservation> {
        return ResponseEntity.ok(reservationService.getReservationById(id))
    }

    /**
     * Crée une réservation standard à partir d'un DTO.
     *
     * @param dto DTO contenant dateDebut, dateFin, clientId, chambreId, employeId (optionnel).
     * @return `ResponseEntity` avec la réservation créée (201).
     * @throws BadRequestException si les dates sont invalides.
     * @throws ResourceNotFoundException si client/chambre/employe introuvable.
     */
    @PostMapping
    fun addReservation(@Valid @RequestBody dto: ReservationDTO): ResponseEntity<Reservation> {
        val reservation = reservationService.createReservation(
            dateDebut = dto.dateDebut,
            dateFin = dto.dateFin,
            clientId = dto.clientId,
            chambreId = dto.chambreId,
            employeId = dto.employeId
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation)
    }

    /**
     * Crée une réservation en ligne (avec plateforme + remise).
     *
     * @param dto DTO en ligne.
     * @return `ResponseEntity` avec la réservation créée (201).
     * @throws BadRequestException si remise invalide ou dates invalides.
     */
    @PostMapping("/online")
    fun addOnlineReservation(@Valid @RequestBody dto: ReservationOnlineDTO): ResponseEntity<Reservation> {
        val reservation = reservationService.createOnlineReservation(
            dateDebut = dto.dateDebut,
            dateFin = dto.dateFin,
            clientId = dto.clientId,
            chambreId = dto.chambreId,
            plateforme = dto.plateforme,
            remise = dto.remise
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation)
    }

    /**
     * Confirme une réservation.
     *
     * @param id identifiant de la réservation.
     * @param employeId identifiant de l'employé qui confirme (optionnel).
     * @return réservation confirmée (200).
     * @throws ResourceNotFoundException si éléments introuvables.
     */
    @PutMapping("/{id}/confirmer")
    fun confirmerReservation(
        @PathVariable id: Int,
        @RequestParam(required = false) employeId: Int?
    ): ResponseEntity<Reservation> {
        return ResponseEntity.ok(reservationService.confirmerReservation(id, employeId))
    }

    /**
     * Annule une réservation.
     *
     * @param id identifiant de la réservation.
     * @param employeId identifiant de l'employé qui annule (optionnel).
     * @return réservation annulée (200).
     */
    @PutMapping("/{id}/annuler")
    fun annulerReservation(
        @PathVariable id: Int,
        @RequestParam(required = false) employeId: Int?
    ): ResponseEntity<Reservation> {
        return ResponseEntity.ok(reservationService.annulerReservation(id, employeId))
    }

    /**
     * Calcule le montant total d'une réservation.
     *
     * @param id identifiant de la réservation.
     * @return `ResponseEntity` contenant une Map {"montant" -> valeur} (200).
     */
    @GetMapping("/{id}/montant")
    fun calculerMontant(@PathVariable id: Int): ResponseEntity<Map<String, Double>> {
        val montant = reservationService.calculerMontant(id)
        return ResponseEntity.ok(mapOf("montant" to montant))
    }

    /**
     * Supprime une réservation.
     *
     * @param id identifiant de la réservation.
     * @return 204 No Content si suppression réussie.
     */
    @DeleteMapping("/{id}")
    fun deleteReservation(@PathVariable id: Int): ResponseEntity<Void> {
        reservationService.deleteReservation(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Récupère les réservations par statut.
     *
     * @param statut statut recherché (ex: "CONFIRMEE", "ANNULEE", "EN_ATTENTE").
     * @return liste des réservations matching.
     */
    @GetMapping("/statut/{statut}")
    fun getByStatut(@PathVariable statut: String): ResponseEntity<List<Reservation>> {
        return ResponseEntity.ok(reservationService.getReservationsByStatus(statut))
    }

    /**
     * Récupère les réservations d'un client.
     *
     * @param clientId identifiant du client.
     * @return liste de réservations du client.
     */
    @GetMapping("/client/{clientId}")
    fun getByClient(@PathVariable clientId: Int): ResponseEntity<List<Reservation>> {
        return ResponseEntity.ok(reservationService.getReservationsByClient(clientId))
    }

    /**
     * Récupère les réservations d'une chambre.
     *
     * @param chambreId identifiant de la chambre.
     * @return liste des réservations pour la chambre.
     */
    @GetMapping("/chambre/{chambreId}")
    fun getByChambre(@PathVariable chambreId: Int): ResponseEntity<List<Reservation>> {
        return ResponseEntity.ok(reservationService.getReservationsByChambre(chambreId))
    }

    /**
     * Récupère les réservations entre deux dates (inclusives).
     *
     * Les dates doivent être passées en ISO (yyyy-MM-dd).
     *
     * @param start date de début (ISO).
     * @param end date de fin (ISO).
     * @return liste des réservations comprises dans l'intervalle.
     */
    @GetMapping("/dates")
    fun getByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: LocalDate
    ): ResponseEntity<List<Reservation>> {
        return ResponseEntity.ok(reservationService.getReservationsBetweenDates(start, end))
    }
}
