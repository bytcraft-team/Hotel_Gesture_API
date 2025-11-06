package APiRes.Exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

/**
 * Représente la structure d'une réponse d'erreur standardisée renvoyée par l'API.
 *
 * @property timestamp Date et heure de l'erreur.
 * @property status Code HTTP de l'erreur.
 * @property error Type de l'erreur (ex: "Bad Request", "Not Found").
 * @property message Message d'erreur principal.
 * @property path Chemin de la requête concernée (optionnel).
 * @property errors Liste des erreurs détaillées (optionnelle).
 */
data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String? = null,
    val errors: List<String>? = null
)

/**
 * Exception levée lorsqu'une ressource est introuvable (404).
 *
 * @constructor Crée une nouvelle instance de [ResourceNotFoundException].
 */
class ResourceNotFoundException(message: String) : RuntimeException(message)
/**
 * Exception levée lorsqu'une requête est invalide (400).
 */
class BadRequestException(message: String) : RuntimeException(message)
/**
 * Exception levée en cas de conflit de données (409).
 */
class ConflictException(message: String) : RuntimeException(message)

/**
 * Gestionnaire global des exceptions pour centraliser le traitement des erreurs.
 *
 * Cette classe intercepte les exceptions à travers toutes les couches de l'application
 * et renvoie une réponse uniforme au client.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * Gère les erreurs de type [ResourceNotFoundException] (404).
     */
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Not Found",
            message = ex.message ?: "Resource not found"
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    }

    /**
     * Gère les erreurs de type [BadRequestException] (400).
     */
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Invalid request"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    /**
     * Gère les erreurs de type [ConflictException] (409).
     */
    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Conflict",
            message = ex.message ?: "Resource conflict"
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error)
    }

    /**
     * Gère les erreurs de validation des entrées utilisateur (400).
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Failed",
            message = "Invalid input data",
            errors = errors
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    /**
     * Gère les erreurs inattendues (500).
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = ex.message ?: "An unexpected error occurred"
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }

    /**
     * Gère les erreurs d’arguments illégaux (400).
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Invalid argument"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }
}