package APiRes.Models

import jakarta.persistence.*
import java.time.LocalDate

/**
 * Client VIP, hérite de [Client].
 *
 * Ajoute le champ `remise` et override de la méthode `reserver` pour renvoyer une `ReservationOnline`.
 *
 * @property remise remise appliquée (valeur entre 0 et 1).
 */
@Entity
@Table(name = "clients_vip")
class ClientVIP(
    nom: String = "",
    prenom: String = "",
    email: String = "",
    telephone: String = "",
    var remise: Double = 0.15
) : Client(
    nom = nom,
    prenom = prenom,
    email = email,
    telephone = telephone
) {

    /**
     * Crée une réservation en ligne avec la remise du VIP.
     *
     * @return ReservationOnline ajoutée aux réservations du client.
     */
    override fun reserver(chambre: Chambre, dateDebut: LocalDate, dateFin: LocalDate): ReservationOnline {
        val reservation = ReservationOnline(
            dateDebut = dateDebut,
            dateFin = dateFin,
            client = this,
            chambre = chambre,
            employe = null,
            plateforme = "SiteWeb",
            remise = remise
        )
        reservations.add(reservation)
        return reservation
    }

    /**
     * Affichage incluant l'information VIP et pourcentage de remise.
     *
     * @return string décrivant le client VIP.
     */
    override fun afficher(): String =
        "${super.afficher()} - VIP (${(remise * 100).toInt()}%)"
}
