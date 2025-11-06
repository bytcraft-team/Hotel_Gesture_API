package APiRes.Models

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Entité représentant une réservation.
 *
 * Champs : id, dateDebut, dateFin, statut, client, chambre, employe (optionnel).
 *
 * Contient les comportements métier :
 * - confirmer,
 * - annuler,
 * - calculerMontant,
 * - afficher.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "reservations")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "reservationId")
open class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    var reservationId: Int? = null,

    @Column(name = "date_debut")
    var dateDebut: LocalDate,

    @Column(name = "date_fin")
    var dateFin: LocalDate,

    var statut: String = "EN_ATTENTE",

    @ManyToOne
    @JoinColumn(name = "client_id")
    var client: Client,

    @ManyToOne
    @JoinColumn(name = "chambre_id")
    var chambre: Chambre,

    @ManyToOne
    @JoinColumn(name = "employe_id")
    var employe: Employee? = null
) {
    /**
     * Confirme la réservation (associe l'employé si fourni).
     *
     * @param by employé ayant confirmé (nullable).
     */
    open fun confirmer(by: Employee?) {
        if (by != null) {
            employe = by
            statut = "CONFIRMEE"
            println(" Réservation $reservationId confirmée par ${by.nom}")
        } else {
            statut = "CONFIRMEE"
            println(" Réservation $reservationId confirmée automatiquement")
        }
    }

    /**
     * Annule la réservation.
     *
     * @param by employé ayant annulé (nullable).
     */
    open fun annuler(by: Employee?) {
        employe = by
        statut = "ANNULEE"
        println(" Réservation $reservationId annulée par ${by?.nom ?: "système"}")
    }

    /**
     * Calcule le montant total de la réservation selon le prix de la chambre et le nombre de jours.
     *
     * @return montant total (Double).
     */
    open fun calculerMontant(): Double {
        val jours = ChronoUnit.DAYS.between(dateDebut, dateFin).coerceAtLeast(1)
        return chambre.prix * jours
    }

    /**
     * Représentation textuelle de la réservation.
     *
     * @return description complète incluant le montant calculé.
     */
    open fun afficher(): String =
        "Réservation $reservationId [$statut] : ${client.prenom} ${client.nom} -> " +
                "Chambre ${chambre.numero} du $dateDebut au $dateFin - ${calculerMontant()} DH"
}
