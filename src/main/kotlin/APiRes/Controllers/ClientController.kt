package APiRes.Controllers

import APiRes.DTO.ClientDTO
import APiRes.DTO.ClientVIPDTO
import APiRes.Models.Client
import APiRes.Models.ClientVIP
import APiRes.Service.ClientService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Contrôleur REST pour la gestion des clients.
 *
 * Endpoints disponibles :
 * - liste paginée de clients,
 * - CRUD : créer, lire, mettre à jour, supprimer,
 * - création spécifique de client VIP,
 * - recherche par nom.
 *
 * @property clientService service métier pour Client.
 */
@RestController
@RequestMapping("/api/clients")
class ClientController(private val clientService: ClientService) {

    /**
     * Récupère une page de clients.
     *
     * @param page index de la page (0 par défaut).
     * @param size taille de page (10 par défaut).
     * @param sortBy champ pour le tri (par défaut "clientId").
     * @return `ResponseEntity` avec `Page<Client>`.
     */
    @GetMapping
    fun getAllClients(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "clientId") sortBy: String
    ): ResponseEntity<Page<Client>> {
        val pageable = PageRequest.of(page, size, Sort.by(sortBy))
        return ResponseEntity.ok(clientService.getAllClients(pageable))
    }

    /**
     * Récupère un client par id.
     *
     * @param id identifiant du client.
     * @return `ResponseEntity<Client>` (200) si trouvé.
     */
    @GetMapping("/{id}")
    fun getClientById(@PathVariable id: Int): ResponseEntity<Client> {
        return ResponseEntity.ok(clientService.getClientById(id))
    }

    /**
     * Crée un client standard à partir d'un DTO.
     *
     * @param dto DTO du client.
     * @return `ResponseEntity` avec le client créé (201).
     * @throws BadRequestException si validations (nom, email) échouent.
     */
    @PostMapping
    fun createClient(@Valid @RequestBody dto: ClientDTO): ResponseEntity<Client> {
        val client = Client(
            nom = dto.nom,
            prenom = dto.prenom,
            email = dto.email,
            telephone = dto.telephone
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.addClient(client))
    }

    /**
     * Crée un client VIP.
     *
     * @param dto DTO contenant la remise et autres infos.
     * @return `ResponseEntity` contenant le client VIP créé (201).
     * @throws BadRequestException si la remise n'est pas valide.
     */
    @PostMapping("/vip")
    fun createClientVIP(@Valid @RequestBody dto: ClientVIPDTO): ResponseEntity<Client> {
        val clientVIP = ClientVIP(
            nom = dto.nom,
            prenom = dto.prenom,
            email = dto.email,
            telephone = dto.telephone,
            remise = dto.remise
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.addClientVIP(clientVIP))
    }

    /**
     * Met à jour un client existant.
     *
     * @param id identifiant du client à mettre à jour.
     * @param dto DTO contenant les nouvelles valeurs.
     * @return `ResponseEntity` avec le client mis à jour (200).
     */
    @PutMapping("/{id}")
    fun updateClient(
        @PathVariable id: Int,
        @Valid @RequestBody dto: ClientDTO
    ): ResponseEntity<Client> {
        val updated = Client(
            nom = dto.nom,
            prenom = dto.prenom,
            email = dto.email,
            telephone = dto.telephone
        )
        return ResponseEntity.ok(clientService.updateClient(id, updated))
    }

    /**
     * Supprime un client.
     *
     * @param id identifiant du client à supprimer.
     * @return `ResponseEntity<Void>` 204 No Content si ok.
     */
    @DeleteMapping("/{id}")
    fun deleteClient(@PathVariable id: Int): ResponseEntity<Void> {
        clientService.deleteClient(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Recherche les clients par nom.
     *
     * @param nom nom recherché.
     * @return `ResponseEntity` contenant la liste des clients correspondants (200).
     */
    @GetMapping("/search/nom/{nom}")
    fun findByNom(@PathVariable nom: String): ResponseEntity<List<Client>> {
        return ResponseEntity.ok(clientService.findByNom(nom))
    }
}
