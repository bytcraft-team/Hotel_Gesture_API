package APiRes.Models

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import java.time.LocalDate

/**
 * Entité représentant un client.
 *
 * L'entité est conçue pour être héritée par ClientVIP.
 *
 * @property clientId id auto-généré.
 * @property nom nom de famille.
 * @property prenom prénom.
 * @property email adresse e-mail.
 * @property telephone numéro de téléphone.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "clients")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "clientId")
open class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    var clientId: Int? = null,
    var nom: String = "",
    var prenom: String = "",
    var email: String = "",
    var telephone: String = ""
) {
    /**
     * Réservations associées au client.
     */
    @OneToMany(mappedBy = "client", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var reservations: MutableList<Reservation> = mutableListOf()

    /**
     * Crée une réservation côté entité (méthode utilitaire).
     *
     * @param chambre chambre réservée.
     * @param dateDebut date début réservation.
     * @param dateFin date fin réservation.
     * @return la reservation créée et ajoutée à la liste du client.
     */
    open fun reserver(chambre: Chambre, dateDebut: LocalDate, dateFin: LocalDate): Reservation {
        val reservation = Reservation(
            dateDebut = dateDebut,
            dateFin = dateFin,
            client = this,
            chambre = chambre,
            employe = null
        )
        reservations.add(reservation)
        return reservation
    }

    /**
     * Représentation textuelle du client.
     *
     * @return string décrivant le client.
     */
    open fun afficher(): String =
        "$prenom $nom (id=$clientId) - $email - $telephone"
}
