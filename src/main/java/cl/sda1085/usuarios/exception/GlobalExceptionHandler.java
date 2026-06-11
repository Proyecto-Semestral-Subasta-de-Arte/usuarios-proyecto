package cl.sda1085.usuarios.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {

    //Error de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex){
        log.warn("Error de validación detectado: {} errores encontrados", ex.getBindingResult().getErrorCount());

        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("TIMESTAMP", LocalDateTime.now());
        respuesta.put("ESTADO", HttpStatus.BAD_REQUEST.value());
        respuesta.put("ERROR", "Error de validación");
        respuesta.put("detalles", errores);

        return ResponseEntity.badRequest().body(respuesta);
    }

    //Error de negocio
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex){
        log.error("Ocurrió un error de negocio: {}", ex.getMessage());

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("TIMESTAMP", LocalDateTime.now());
        error.put("ESTADO", HttpStatus.BAD_REQUEST.value());
        error.put("ERROR", "Error de negocio");
        error.put("MENSAJE", ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    //Email duplicado
    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleEmailDuplicado(EmailDuplicadoException ex) {
        log.error("Conflicto de registro: {}", ex.getMessage());

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("TIMESTAMP", LocalDateTime.now());
        error.put("ESTADO", HttpStatus.CONFLICT.value());
        error.put("ERROR", "Email no disponible.");
        error.put("MENSAJE", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    //Usuario no encontrado
    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        log.warn("Búsqueda fallida: {}", ex.getMessage());

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("TIMESTAMP", LocalDateTime.now());
        error.put("ESTADO", HttpStatus.NOT_FOUND.value());
        error.put("ERROR", "Usuario no encontrado.");
        error.put("MENSAJE", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
