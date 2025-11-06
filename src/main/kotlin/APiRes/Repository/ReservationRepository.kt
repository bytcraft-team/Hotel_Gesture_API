package APiRes.Repository

import APiRes.Models.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
 * Repository JPA pour l'entité [Reservation].
 *
 * Méthodes custom :
 * - findByClient_ClientId : réservations par client id,
 * - findByChambre_ChambreId : réservations par chambre id,
 * - findByDateDebutBetween : réservations entre deux dates,
 * - findByStatut : réservations par statut.
 */
@Repository
interface ReservationRepository : JpaRepository<Reservation, Int> {
    fun findByClient_ClientId(clientId: Int): List<Reservation>
    fun findByChambre_ChambreId(chambreId: Int): List<Reservation>
    fun findByDateDebutBetween(start: LocalDate, end: LocalDate): List<Reservation>
    fun findByStatut(statut: String): List<Reservation>
}
