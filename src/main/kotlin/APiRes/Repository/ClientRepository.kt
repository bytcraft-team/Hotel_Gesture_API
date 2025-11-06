package APiRes.Repository

import APiRes.Models.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository JPA pour l'entité [Client].
 *
 * Fournit une méthode de recherche par nom.
 */
@Repository
interface ClientRepository : JpaRepository<Client, Int> {
    /**
     * Récupère les clients dont le nom correspond.
     *
     * @param nom nom recherché.
     * @return liste des clients trouvés.
     */
    fun findByNom(nom: String): List<Client>
}
