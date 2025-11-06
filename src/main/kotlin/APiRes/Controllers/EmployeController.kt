package APiRes.Controllers

import APiRes.DTO.EmployeeDTO
import APiRes.Models.Employee
import APiRes.Service.EmployeeService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Contrôleur REST pour la gestion des employés.
 *
 * Fournit endpoints pour :
 * - liste paginée,
 * - CRUD (ajout, modification, suppression),
 * - recherche par nom ou poste.
 *
 * @property employeeService service métier pour Employee.
 */
@RestController
@RequestMapping("/api/employees")
class EmployeeController(private val employeeService: EmployeeService) {

    /**
     * Récupère une page d'employés.
     *
     * @param page index de page (0 par défaut).
     * @param size taille de page (10 par défaut).
     * @param sortBy champ de tri (par défaut "employeId").
     * @return `ResponseEntity<Page<Employee>>`.
     */
    @GetMapping
    fun getAllEmployees(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "employeId") sortBy: String
    ): ResponseEntity<Page<Employee>> {
        val pageable = PageRequest.of(page, size, Sort.by(sortBy))
        return ResponseEntity.ok(employeeService.getAllEmployees(pageable))
    }

    /**
     * Récupère un employé par id.
     *
     * @param id identifiant de l'employé.
     * @return `ResponseEntity<Employee>` (200).
     */
    @GetMapping("/{id}")
    fun getEmployeeById(@PathVariable id: Int): ResponseEntity<Employee> {
        return ResponseEntity.ok(employeeService.getEmployeeById(id))
    }

    /**
     * Crée un employé.
     *
     * @param dto DTO employé.
     * @return `ResponseEntity` avec l'employé créé (201).
     * @throws BadRequestException si nom vide ou salaire négatif.
     */
    @PostMapping
    fun addEmployee(@Valid @RequestBody dto: EmployeeDTO): ResponseEntity<Employee> {
        val employee = Employee(
            nom = dto.nom,
            poste = dto.poste,
            salaire = dto.salaire
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.addEmployee(employee))
    }

    /**
     * Met à jour un employé.
     *
     * @param id id de l'employé à mettre à jour.
     * @param dto DTO contenant les valeurs mises à jour.
     * @return `ResponseEntity<Employee>` 200.
     */
    @PutMapping("/{id}")
    fun updateEmployee(
        @PathVariable id: Int,
        @Valid @RequestBody dto: EmployeeDTO
    ): ResponseEntity<Employee> {
        val updated = Employee(
            nom = dto.nom,
            poste = dto.poste,
            salaire = dto.salaire
        )
        return ResponseEntity.ok(employeeService.updateEmployee(id, updated))
    }

    /**
     * Supprime un employé.
     *
     * @param id identifiant de l'employé.
     * @return 204 No Content si suppression réussie.
     */
    @DeleteMapping("/{id}")
    fun deleteEmployee(@PathVariable id: Int): ResponseEntity<Void> {
        employeeService.deleteEmployee(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Recherche par nom.
     *
     * @param nom nom recherché.
     * @return liste des employés correspondant.
     */
    @GetMapping("/search/nom/{nom}")
    fun findByNom(@PathVariable nom: String): ResponseEntity<List<Employee>> {
        return ResponseEntity.ok(employeeService.findByNom(nom))
    }

    /**
     * Recherche par poste.
     *
     * @param poste poste recherché.
     * @return liste des employés correspondant.
     */
    @GetMapping("/search/poste/{poste}")
    fun findByPoste(@PathVariable poste: String): ResponseEntity<List<Employee>> {
        return ResponseEntity.ok(employeeService.findByPoste(poste))
    }
}
