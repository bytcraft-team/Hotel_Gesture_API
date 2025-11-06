package APiRes.Service

import APiRes.Exception.ResourceNotFoundException
import APiRes.Exception.BadRequestException
import APiRes.Models.*
import APiRes.Repository.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

/**
 * Service métier pour les réservations.
 *
 * Gère la création (standard et en ligne), la mise à jour, la confirmation,
 * l'annulation, le calcul de montant et les recherches filtrées.
 *
 * @property reservationRepo repository des réservations.
 * @property clientRepo repository des clients.
 * @property chambreRepo repository des chambres.
 * @property employeeRepo repository des employés.
 */
@Service
class ReservationService(
    private val reservationRepo: ReservationRepository,
    private val clientRepo: ClientRepository,
    private val chambreRepo: ChambreRepository,
    private val employeeRepo: EmployeeRepository
) {

    /**
     * Récupère une page de réservations.
     *
     * @param pageable configuration de pagination.
     * @return page de réservations.
     */
    fun getAllReservations(pageable: Pageable): Page<Reservation> =
        reservationRepo.findAll(pageable)

    /**
     * Récupère une réservation par id.
     *
     * @param id identifiant de la réservation.
     * @return réservation trouvée.
     * @throws ResourceNotFoundException si non trouvée.
     */
    fun getReservationById(id: Int): Reservation =
        reservationRepo.findById(id)
            .orElseThrow { ResourceNotFoundException("Réservation avec l'ID $id introuvable") }

    /**
     * Ajoute une réservation après validation des dates.
     *
     * @param reservation entité Reservation.
     * @return réservation sauvegardée.
     * @throws BadRequestException si dates invalides.
     */
    fun addReservation(reservation: Reservation): Reservation {
        validateReservationDates(reservation.dateDebut, reservation.dateFin)
        return reservationRepo.save(reservation)
    }

    /**
     * Crée et sauvegarde une réservation via IDs (client, chambre, optional employe).
     *
     * @param dateDebut date de début.
     * @param dateFin date de fin.
     * @param clientId id du client.
     * @param chambreId id de la chambre.
     * @param employeId id de l'employé (optionnel).
     * @return réservation créée et sauvegardée.
     * @throws ResourceNotFoundException si client/chambre/employé introuvable.
     * @throws BadRequestException si dates invalides.
     */
    fun createReservation(
        dateDebut: LocalDate,
        dateFin: LocalDate,
        clientId: Int,
        chambreId: Int,
        employeId: Int?
    ): Reservation {
        validateReservationDates(dateDebut, dateFin)

        val client = clientRepo.findById(clientId)
            .orElseThrow { ResourceNotFoundException("Client avec l'ID $clientId introuvable") }

        val chambre = chambreRepo.findById(chambreId)
            .orElseThrow { ResourceNotFoundException("Chambre avec l'ID $chambreId introuvable") }

        val employe = employeId?.let {
            employeeRepo.findById(it)
                .orElseThrow { ResourceNotFoundException("Employé avec l'ID $it introuvable") }
        }

        val reservation = Reservation(
            dateDebut = dateDebut,
            dateFin = dateFin,
            client = client,
            chambre = chambre,
            employe = employe
        )

        return reservationRepo.save(reservation)
    }

    /**
     * Ajoute une réservation en ligne (entité [ReservationOnline]) après validations.
     *
     * @param resOnline réservation en ligne à sauvegarder.
     * @return réservation sauvegardée.
     * @throws BadRequestException si remise invalide ou dates invalides.
     */
    fun addOnlineReservation(resOnline: ReservationOnline): Reservation {
        validateReservationDates(resOnline.dateDebut, resOnline.dateFin)
        if (resOnline.remise < 0 || resOnline.remise > 1) {
            throw BadRequestException("La remise doit être entre 0 et 1")
        }
        return reservationRepo.save(resOnline)
    }

    /**
     * Crée une réservation en ligne à partir de paramètres.
     *
     * @param dateDebut date début.
     * @param dateFin date fin.
     * @param clientId id client.
     * @param chambreId id chambre.
     * @param plateforme plateforme (ex: "SiteWeb").
     * @param remise remise en fraction (0.15 = 15%).
     * @return réservation en ligne créée (ReservationOnline).
     * @throws ResourceNotFoundException / BadRequestException selon validation.
     */
    fun createOnlineReservation(
        dateDebut: LocalDate,
        dateFin: LocalDate,
        clientId: Int,
        chambreId: Int,
        plateforme: String,
        remise: Double
    ): ReservationOnline {
        validateReservationDates(dateDebut, dateFin)

        if (remise < 0 || remise > 1) {
            throw BadRequestException("La remise doit être entre 0 et 1")
        }

        val client = clientRepo.findById(clientId)
            .orElseThrow { ResourceNotFoundException("Client avec l'ID $clientId introuvable") }

        val chambre = chambreRepo.findById(chambreId)
            .orElseThrow { ResourceNotFoundException("Chambre avec l'ID $chambreId introuvable") }

        val reservation = ReservationOnline(
            dateDebut = dateDebut,
            dateFin = dateFin,
            client = client,
            chambre = chambre,
            plateforme = plateforme,
            remise = remise
        )

        return reservationRepo.save(reservation)
    }

    /**
     * Met à jour une réservation existante.
     *
     * @param id id de la réservation à mettre à jour.
     * @param updated entité contenant nouvelles valeurs.
     * @return réservation mise à jour.
     * @throws ResourceNotFoundException si introuvable.
     * @throws BadRequestException si dates invalides.
     */
    fun updateReservation(id: Int, updated: Reservation): Reservation {
        val existing = reservationRepo.findById(id)
            .orElseThrow { ResourceNotFoundException("Réservation avec l'ID $id introuvable") }

        validateReservationDates(updated.dateDebut, updated.dateFin)

        existing.dateDebut = updated.dateDebut
        existing.dateFin = updated.dateFin
        existing.statut = updated.statut
        existing.client = updated.client
        existing.chambre = updated.chambre
        existing.employe = updated.employe
        return reservationRepo.save(existing)
    }

    /**
     * Supprime une réservation.
     *
     * @param id identifiant de la réservation à supprimer.
     * @throws ResourceNotFoundException si introuvable.
     */
    fun deleteReservation(id: Int) {
        if (!reservationRepo.existsById(id)) {
            throw ResourceNotFoundException("Réservation avec l'ID $id introuvable")
        }
        reservationRepo.deleteById(id)
    }

    /**
     * Confirme une réservation en option via un employé.
     *
     * @param id id de la réservation.
     * @param employeId id de l'employé qui confirme (nullable).
     * @return réservation confirmée.
     * @throws ResourceNotFoundException si introuvable.
     */
    fun confirmerReservation(id: Int, employeId: Int?): Reservation {
        val reservation = getReservationById(id)

        val employe = employeId?.let {
            employeeRepo.findById(it)
                .orElseThrow { ResourceNotFoundException("Employé avec l'ID $it introuvable") }
        }

        reservation.confirmer(employe)
        return reservationRepo.save(reservation)
    }

    /**
     * Annule une réservation.
     *
     * @param id id de la réservation.
     * @param employeId id de l'employé qui annule (nullable).
     * @return réservation annulée.
     */
    fun annulerReservation(id: Int, employeId: Int?): Reservation {
        val reservation = getReservationById(id)

        val employe = employeId?.let {
            employeeRepo.findById(it)
                .orElseThrow { ResourceNotFoundException("Employé avec l'ID $it introuvable") }
        }

        reservation.annuler(employe)
        return reservationRepo.save(reservation)
    }

    /**
     * Calcule le montant total d'une réservation via la méthode de l'entité.
     *
     * @param id id de la réservation.
     * @return montant calculé.
     */
    fun calculerMontant(id: Int): Double {
        val reservation = getReservationById(id)
        return reservation.calculerMontant()
    }

    /**
     * Recherche par statut.
     *
     * @param statut statut recherché.
     * @return liste de réservations.
     */
    fun getReservationsByStatus(statut: String): List<Reservation> =
        reservationRepo.findByStatut(statut)

    /**
     * Recherche par client id.
     *
     * @param clientId id du client.
     * @return liste de réservations.
     */
    fun getReservationsByClient(clientId: Int): List<Reservation> =
        reservationRepo.findByClient_ClientId(clientId)

    /**
     * Recherche par chambre id.
     *
     * @param chambreId id de la chambre.
     * @return liste de réservations.
     */
    fun getReservationsByChambre(chambreId: Int): List<Reservation> =
        reservationRepo.findByChambre_ChambreId(chambreId)

    /**
     * Recherche entre deux dates (dateDebut entre start et end).
     *
     * @param start date de début.
     * @param end date de fin.
     * @return liste de réservations.
     */
    fun getReservationsBetweenDates(start: LocalDate, end: LocalDate): List<Reservation> =
        reservationRepo.findByDateDebutBetween(start, end)

    /**
     * Valide les dates d'une réservation :
     * - la date de fin doit être strictement après la date de début,
     * - la date de début ne peut pas être dans le passé.
     *
     * @param debut date de début.
     * @param fin date de fin.
     * @throws BadRequestException si validation échoue.
     */
    private fun validateReservationDates(debut: LocalDate, fin: LocalDate) {
        if (fin.isBefore(debut) || fin.isEqual(debut)) {
            throw BadRequestException("La date de fin doit être après la date de début")
        }
        if (debut.isBefore(LocalDate.now())) {
            throw BadRequestException("La date de début ne peut pas être dans le passé")
        }
    }
}
