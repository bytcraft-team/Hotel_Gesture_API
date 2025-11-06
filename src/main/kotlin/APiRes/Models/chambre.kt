package APiRes.Models

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

/**
 * Entité représentant une chambre.
 *
 * Utilise l'héritage JPA (JOINED) pour permettre des sous-classes (ex: ChambreSuite).
 *
 * @property chambreId identifiant auto-généré.
 * @property numero numéro de chambre.
 * @property prix prix par nuit (Double).
 * @property typeChambre type (SIMPLE par défaut). Marqué `open` pour override dans les sous-classes.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "chambres")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "chambreId")
open class Chambre(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chambre_id")
    var chambreId: Int? = null,

    var numero: Int = 0,
    var prix: Double = 0.0,

    @Column(name = "type_chambre")
    open var typeChambre: String = "SIMPLE"
) {
    /**
     * Liste des réservations liées à la chambre.
     *
     * @note fetch = LAZY pour éviter de charger automatiquement les réservations.
     */
    @OneToMany(mappedBy = "chambre", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var reservations: MutableList<Reservation> = mutableListOf()

    /**
     * Méthode utilitaire pour afficher la chambre (string format).
     *
     * Peut être overridée par les sous-classes.
     *
     * @return description textuelle de la chambre.
     */
    open fun afficher(): String =
        "Chambre $numero (id=$chambreId) - $typeChambre - $prix DH"
}
