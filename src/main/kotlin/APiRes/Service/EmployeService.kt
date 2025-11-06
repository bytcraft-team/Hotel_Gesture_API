package APiRes.Service

import APiRes.Exception.ResourceNotFoundException
import APiRes.Exception.BadRequestException
import APiRes.Models.Employee
import APiRes.Repository.EmployeeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Service métier pour l'entité [Employee].
 *
 * Contient validations et accès au repository.
 *
 * @property employeeRepo repository JPA pour Employee.
 */
@Service
class EmployeeService(private val employeeRepo: EmployeeRepository) {

    /**
     * Récupère une page d'employés.
     *
     * @param pageable configuration de pagination.
     * @return page d'employés.
     */
    fun getAllEmployees(pageable: Pageable): Page<Employee> =
        employeeRepo.findAll(pageable)

    /**
     * Récupère un employé par id.
     *
     * @param id identifiant de l'employé.
     * @return employee trouvé.
     * @throws ResourceNotFoundException si non trouvé.
     */
    fun getEmployeeById(id: Int): Employee =
        employeeRepo.findById(id)
            .orElseThrow { ResourceNotFoundException("Employé avec l'ID $id introuvable") }

    /**
     * Ajoute un employé après validations.
     *
     * @param employee entité Employee.
     * @return employee sauvegardé.
     * @throws BadRequestException si nom vide ou salaire négatif.
     */
    fun addEmployee(employee: Employee): Employee {
        if (employee.nom.isBlank()) throw BadRequestException("Le nom ne peut pas être vide")
        if (employee.salaire < 0) throw BadRequestException("Le salaire ne peut pas être négatif")
        return employeeRepo.save(employee)
    }

    /**
     * Met à jour un employé.
     *
     * @param id identifiant de l'employé à modifier.
     * @param updated données mises à jour.
     * @return employé mis à jour.
     * @throws ResourceNotFoundException si introuvable.
     * @throws BadRequestException si validations échouent.
     */
    fun updateEmployee(id: Int, updated: Employee): Employee {
        val existing = employeeRepo.findById(id)
            .orElseThrow { ResourceNotFoundException("Employé avec l'ID $id introuvable") }

        if (updated.nom.isBlank()) throw BadRequestException("Le nom ne peut pas être vide")
        if (updated.salaire < 0) throw BadRequestException("Le salaire ne peut pas être négatif")

        existing.nom = updated.nom
        existing.poste = updated.poste
        existing.salaire = updated.salaire
        return employeeRepo.save(existing)
    }

    /**
     * Supprime un employé.
     *
     * @param id id de l'employé à supprimer.
     * @throws ResourceNotFoundException si introuvable.
     */
    fun deleteEmployee(id: Int) {
        if (!employeeRepo.existsById(id)) {
            throw ResourceNotFoundException("Employé avec l'ID $id introuvable")
        }
        employeeRepo.deleteById(id)
    }

    /**
     * Recherche par nom.
     *
     * @param nom nom recherché.
     * @return liste des employés trouvés.
     */
    fun findByNom(nom: String): List<Employee> = employeeRepo.findByNom(nom)

    /**
     * Recherche par poste.
     *
     * @param poste poste recherché.
     * @return liste des employés trouvés.
     */
    fun findByPoste(poste: String): List<Employee> = employeeRepo.findByPoste(poste)
}
