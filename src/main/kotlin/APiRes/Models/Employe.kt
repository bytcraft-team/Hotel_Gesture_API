package APiRes.Models

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

/**
 * Entité représentant un employé.
 *
 * Champs : id, nom, poste, salaire.
 */
@Entity
@Table(name = "employees")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "employeId")
open class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employe_id")
    var employeId: Int? = null,

    var nom: String = "",
    var poste: String = "",
    var salaire: Double = 0.0
) {
    /**
     * Réservations gérées par l'employé.
     */
    @OneToMany(mappedBy = "employe", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var reservations: MutableList<Reservation> = mutableListOf()

    /**
     * Méthode utilitaire simulant le travail.
     *
     * @return description courte.
     */
    open fun travailler(): String = "$nom (id=$employeId) travaille au poste de $poste."

    /**
     * Augmente le salaire si le montant est positif.
     *
     * @param montant montant à ajouter au salaire.
     */
    fun augmenterSalaire(montant: Double) {
        if (montant > 0) {
            salaire += montant
            println(" Salaire de $nom augmenté de $montant DH. Nouveau salaire : $salaire DH")
        } else {
            println("Le montant doit être positif.")
        }
    }

    /**
     * Change le poste si la valeur n'est pas vide.
     *
     * @param nouveauPoste nouveau poste à assigner.
     */
    fun changerPoste(nouveauPoste: String) {
        if (nouveauPoste.isNotBlank()) {
            val ancienPoste = poste
            poste = nouveauPoste
            println(" $nom a changé de poste : $ancienPoste → $nouveauPoste")
        } else {
            println(" Le nouveau poste ne peut pas être vide.")
        }
    }
}
