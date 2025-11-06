package APiRes.Models

import jakarta.persistence.*

/**
 * Sous-classe de [Chambre] représentant une suite.
 *
 * Champs supplémentaires :
 * - suiteNom : nom de la suite,
 * - nombrePieces : nombre de pièces,
 * - jacuzzi : présence de jacuzzi.
 *
 * @constructor crée une suite en initialisant les champs hérités.
 */
@Entity
@Table(name = "chambres_suites")
class ChambreSuite(
    numero: Int,
    prix: Double,
    @Column(name = "suite_nom")
    var suiteNom: String,
    @Column(name = "nombre_pieces")
    var nombrePieces: Int = 2,
    var jacuzzi: Boolean = false
) : Chambre(numero = numero, prix = prix) {

    override var typeChambre: String = "SUITE"

    /**
     * Affiche les détails spécifiques à la suite.
     *
     * @return description textuelle détaillée de la suite.
     */
    override fun afficher(): String =
        "Suite $numero (id=$chambreId) - $suiteNom - $nombrePieces pièces - " +
                "Jacuzzi: ${if (jacuzzi) "Oui" else "Non"} - $prix DH"
}
