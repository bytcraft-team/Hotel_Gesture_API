package APiRes.Repository

import APiRes.Models.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository JPA pour l'entité [Employee].
 *
 * Fournit des recherches par nom et par poste.
 */
@Repository
interface EmployeeRepository : JpaRepository<Employee, Int> {
    /**
     * Trouve les employés par nom.
     *
     * @param nom nom recherché.
     * @return liste des employés.
     */
    fun findByNom(nom: String): List<Employee>

    /**
     * Trouve les employés par poste.
     *
     * @param poste poste recherché.
     * @return liste des employés.
     */
    fun findByPoste(poste: String): List<Employee>
}
