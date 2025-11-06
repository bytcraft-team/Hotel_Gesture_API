package APiRes.Service

import APiRes.Exception.ResourceNotFoundException
import APiRes.Exception.BadRequestException
import APiRes.Models.Client
import APiRes.Models.ClientVIP
import APiRes.Repository.ClientRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Service métier pour l'entité [Client].
 *
 * Contient les validations et appels au repository.
 *
 * @property clientRepo repository JPA pour Client.
 */
@Service
class ClientService(private val clientRepo: ClientRepository) {

    /**
     * Récupère une page de clients.
     *
     * @param pageable configuration de pagination.
     * @return page de clients.
     */
    fun getAllClients(pageable: Pageable): Page<Client> =
        clientRepo.findAll(pageable)

    /**
     * Récupère un client par id.
     *
     * @param id identifiant du client.
     * @return client trouvé.
     * @throws ResourceNotFoundException si non trouvé.
     */
    fun getClientById(id: Int): Client =
        clientRepo.findById(id)
            .orElseThrow { ResourceNotFoundException("Client avec l'ID $id introuvable") }

    /**
     * Ajoute un client après validations simples.
     *
     * @param client entité Client.
     * @return client sauvegardé.
     * @throws BadRequestException si nom ou email vides.
     */
    fun addClient(client: Client): Client {
        if (client.nom.isBlank()) throw BadRequestException("Le nom ne peut pas être vide")
        if (client.email.isBlank()) throw BadRequestException("L'email ne peut pas être vide")
        return clientRepo.save(client)
    }

    /**
     * Ajoute un client VIP après validation de la remise.
     *
     * @param clientVIP entité ClientVIP.
     * @return client VIP sauvegardé.
     * @throws BadRequestException si remise hors [0,1].
     */
    fun addClientVIP(clientVIP: ClientVIP): Client {
        if (clientVIP.remise < 0 || clientVIP.remise > 1) {
            throw BadRequestException("La remise doit être entre 0 et 1")
        }
        return clientRepo.save(clientVIP)
    }

    /**
     * Met à jour un client existant.
     *
     * @param id identifiant du client à mettre à jour.
     * @param updated entité contenant les nouvelles valeurs.
     * @return client mis à jour.
     * @throws ResourceNotFoundException si client introuvable.
     * @throws BadRequestException si nom ou email vides.
     */
    fun updateClient(id: Int, updated: Client): Client {
        val existing = clientRepo.findById(id)
            .orElseThrow { ResourceNotFoundException("Client avec l'ID $id introuvable") }

        if (updated.nom.isBlank()) throw BadRequestException("Le nom ne peut pas être vide")
        if (updated.email.isBlank()) throw BadRequestException("L'email ne peut pas être vide")

        existing.nom = updated.nom
        existing.prenom = updated.prenom
        existing.email = updated.email
        existing.telephone = updated.telephone
        return clientRepo.save(existing)
    }

    /**
     * Supprime un client.
     *
     * @param id identifiant du client.
     * @throws ResourceNotFoundException si client introuvable.
     */
    fun deleteClient(id: Int) {
        if (!clientRepo.existsById(id)) {
            throw ResourceNotFoundException("Client avec l'ID $id introuvable")
        }
        clientRepo.deleteById(id)
    }

    /**
     * Recherche les clients par nom.
     *
     * @param nom nom recherché.
     * @return liste de clients correspondants.
     */
    fun findByNom(nom: String): List<Client> = clientRepo.findByNom(nom)
}
