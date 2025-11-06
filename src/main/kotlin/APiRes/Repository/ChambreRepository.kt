package APiRes.Repository

import APiRes.Models.Chambre
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository JPA pour l'entité [Chambre].
 *
 * Fournit des méthodes de requête personnalisées :
 * - findByTypeChambre : récupérer les chambres par type,
 * - findByPrixLessThanEqual : récupérer les chambres avec prix <= valeur donnée.
 */
@Repository
interface ChambreRepository : JpaRepository<Chambre, Int> {
    /**
     * Récupère toutes les chambres dont le type correspond au paramètre.
     *
     * @param typeChambre type recherché (ex: "SIMPLE", "SUITE").
     * @return liste des chambres correspondant.
     */
    fun findByTypeChambre(typeChambre: String): List<Chambre>

    /**
     * Récupère toutes les chambres dont le prix est inférieur ou égal à maxPrix.
     *
     * @param maxPrix prix maximum.
     * @return liste des chambres correspondantes.
     */
    fun findByPrixLessThanEqual(maxPrix: Double): List<Chambre>
}
