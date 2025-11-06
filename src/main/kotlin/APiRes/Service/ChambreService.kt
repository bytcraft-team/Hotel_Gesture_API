package APiRes.Service

import APiRes.Exception.ResourceNotFoundException
import APiRes.Exception.BadRequestException
import APiRes.Models.Chambre
import APiRes.Models.ChambreSuite
import APiRes.Repository.ChambreRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Service métier pour l'entité [Chambre].
 *
 * Contient la logique de validation et d'accès aux méthodes du repository.
 *
 * @property chambreRepo repository JPA pour Chambre.
 */
@Service
class ChambreService(private val chambreRepo: ChambreRepository) {

    /**
     * Récupère une page de chambres.
     *
     * @param pageable configuration de pagination.
     * @return page de chambres.
     */
    fun getAllChambres(pageable: Pageable): Page<Chambre> =
        chambreRepo.findAll(pageable)

    /**
     * Récupère une chambre par id.
     *
     * @param id identifiant de la chambre.
     * @return chambre trouvée.
     * @throws ResourceNotFoundException si non trouvée.
     */
    fun getChambreById(id: Int): Chambre =
        chambreRepo.findById(id)
            .orElseThrow { ResourceNotFoundException("Chambre avec l'ID $id introuvable") }

    /**
     * Ajoute une nouvelle chambre après validations métier.
     *
     * @param chambre entité Chambre à ajouter.
     * @return chambre sauvegardée.
     * @throws BadRequestException si prix négatif ou numéro invalide.
     */
    fun addChambre(chambre: Chambre): Chambre {
        if (chambre.prix < 0) throw BadRequestException("Le prix ne peut pas être négatif")
        if (chambre.numero <= 0) throw BadRequestException("Le numéro de chambre doit être positif")
        return chambreRepo.save(chambre)
    }

    /**
     * Ajoute une suite (sous-classe ChambreSuite) après validations.
     *
     * @param suite objet ChambreSuite à sauvegarder.
     * @return suite sauvegardée.
     * @throws BadRequestException si validations échouent.
     */
    fun addSuite(suite: ChambreSuite): Chambre {
        if (suite.prix < 0) throw BadRequestException("Le prix ne peut pas être négatif")
        if (suite.nombrePieces <= 0) throw BadRequestException("Le nombre de pièces doit être positif")
        return chambreRepo.save(suite)
    }

    /**
     * Met à jour une chambre existante.
     *
     * Les champs modifiables : numero, typeChambre, prix.
     *
     * @param id identifiant de la chambre à mettre à jour.
     * @param updated entité contenant les nouvelles valeurs.
     * @return chambre mise à jour.
     * @throws ResourceNotFoundException si l'id n'existe pas.
     * @throws BadRequestException si validations échouent.
     */
    fun updateChambre(id: Int, updated: Chambre): Chambre {
        val existing = chambreRepo.findById(id)
            .orElseThrow { ResourceNotFoundException("Chambre avec l'ID $id introuvable") }

        if (updated.prix < 0) throw BadRequestException("Le prix ne peut pas être négatif")
        if (updated.numero <= 0) throw BadRequestException("Le numéro de chambre doit être positif")

        existing.numero = updated.numero
        existing.typeChambre = updated.typeChambre
        existing.prix = updated.prix
        return chambreRepo.save(existing)
    }

    /**
     * Supprime une chambre par id.
     *
     * @param id identifiant de la chambre à supprimer.
     * @throws ResourceNotFoundException si l'id n'existe pas.
     */
    fun deleteChambre(id: Int) {
        if (!chambreRepo.existsById(id)) {
            throw ResourceNotFoundException("Chambre avec l'ID $id introuvable")
        }
        chambreRepo.deleteById(id)
    }

    /**
     * Recherche par type.
     *
     * @param type type de chambre.
     * @return liste des chambres.
     */
    fun findByType(type: String): List<Chambre> = chambreRepo.findByTypeChambre(type)

    /**
     * Recherche par prix maximum.
     *
     * @param maxPrix prix maximum.
     * @return liste des chambres correspondant.
     */
    fun findByMaxPrix(maxPrix: Double): List<Chambre> = chambreRepo.findByPrixLessThanEqual(maxPrix)
}
