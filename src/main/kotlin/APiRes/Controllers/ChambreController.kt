package APiRes.Controllers

import APiRes.DTO.ChambreDTO
import APiRes.DTO.ChambreSuiteDTO
import APiRes.Models.Chambre
import APiRes.Models.ChambreSuite
import APiRes.Service.ChambreService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Contrôleur REST pour la gestion des chambres.
 *
 * Fournit les endpoints pour :
 * - récupérer la liste paginée de chambres,
 * - récupérer une chambre par son id,
 * - créer / mettre à jour / supprimer des chambres (et suites),
 * - rechercher par type ou prix maximum.
 *
 * Les méthodes renvoient des `ResponseEntity` afin de contrôler les codes HTTP.
 *
 * @property chambreService service métier manipulant les entités Chambre.
 */
@RestController
@RequestMapping("/api/chambres")
class ChambreController(private val chambreService: ChambreService) {

    /**
     * Récupère une page de chambres triée.
     *
     * @param page index de la page (par défaut 0).
     * @param size taille de la page (par défaut 10).
     * @param sortBy propriété utilisée pour le tri (par défaut "chambreId").
     * @return `ResponseEntity` contenant une page (`Page<Chambre>`) et code 200.
     */
    @GetMapping
    fun getAllChambres(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "chambreId") sortBy: String
    ): ResponseEntity<Page<Chambre>> {
        val pageable = PageRequest.of(page, size, Sort.by(sortBy))
        return ResponseEntity.ok(chambreService.getAllChambres(pageable))
    }

    /**
     * Récupère une chambre par son identifiant.
     *
     * @param id identifiant de la chambre.
     * @return `ResponseEntity` avec la chambre si trouvée (200), sinon lève une exception gérée ailleurs.
     */
    @GetMapping("/{id}")
    fun getChambreById(@PathVariable id: Int): ResponseEntity<Chambre> {
        return ResponseEntity.ok(chambreService.getChambreById(id))
    }

    /**
     * Crée une nouvelle chambre standard à partir d'un DTO.
     *
     * Valide le DTO via `@Valid`.
     *
     * @param dto données de la chambre (numéro, prix, typeChambre).
     * @return `ResponseEntity` contenant la chambre créée et le status 201 CREATED.
     * @throws BadRequestException si les validations métier (prix, numéro) échouent.
     */
    @PostMapping
    fun addChambre(@Valid @RequestBody dto: ChambreDTO): ResponseEntity<Chambre> {
        val chambre = Chambre(
            numero = dto.numero,
            prix = dto.prix,
            typeChambre = dto.typeChambre
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(chambreService.addChambre(chambre))
    }

    /**
     * Crée une suite (sous-classe `ChambreSuite`) à partir d'un DTO spécifique.
     *
     * @param dto données de la suite (suiteNom, nombrePieces, jacuzzi, etc.).
     * @return `ResponseEntity` contenant la suite créée et le status 201 CREATED.
     * @throws BadRequestException si validations métier échouent.
     */
    @PostMapping("/suite")
    fun addSuite(@Valid @RequestBody dto: ChambreSuiteDTO): ResponseEntity<Chambre> {
        val suite = ChambreSuite(
            numero = dto.numero,
            prix = dto.prix,
            suiteNom = dto.suiteNom,
            nombrePieces = dto.nombrePieces,
            jacuzzi = dto.jacuzzi
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(chambreService.addSuite(suite))
    }

    /**
     * Met à jour une chambre existante.
     *
     * @param id identifiant de la chambre à mettre à jour.
     * @param dto DTO contenant les nouvelles valeurs.
     * @return `ResponseEntity` contenant la chambre mise à jour (200).
     * @throws ResourceNotFoundException si l'id n'existe pas.
     * @throws BadRequestException si validations métier échouent.
     */
    @PutMapping("/{id}")
    fun updateChambre(
        @PathVariable id: Int,
        @Valid @RequestBody dto: ChambreDTO
    ): ResponseEntity<Chambre> {
        val updated = Chambre(
            numero = dto.numero,
            prix = dto.prix,
            typeChambre = dto.typeChambre
        )
        return ResponseEntity.ok(chambreService.updateChambre(id, updated))
    }

    /**
     * Supprime une chambre par son identifiant.
     *
     * @param id identifiant de la chambre à supprimer.
     * @return `ResponseEntity<Void>` avec code 204 No Content si suppression réussie.
     * @throws ResourceNotFoundException si l'id n'existe pas.
     */
    @DeleteMapping("/{id}")
    fun deleteChambre(@PathVariable id: Int): ResponseEntity<Void> {
        chambreService.deleteChambre(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Recherche les chambres par type.
     *
     * @param type type de chambre (ex: "SIMPLE", "SUITE").
     * @return `ResponseEntity` contenant la liste des chambres correspondant au type (200).
     */
    @GetMapping("/type/{type}")
    fun findByType(@PathVariable type: String): ResponseEntity<List<Chambre>> {
        return ResponseEntity.ok(chambreService.findByType(type))
    }

    /**
     * Recherche les chambres dont le prix est inférieur ou égal au montant fourni.
     *
     * @param maxPrix prix maximum (Double).
     * @return `ResponseEntity` avec la liste des chambres (200).
     */
    @GetMapping("/prix-max/{maxPrix}")
    fun findByMaxPrix(@PathVariable maxPrix: Double): ResponseEntity<List<Chambre>> {
        return ResponseEntity.ok(chambreService.findByMaxPrix(maxPrix))
    }
}
