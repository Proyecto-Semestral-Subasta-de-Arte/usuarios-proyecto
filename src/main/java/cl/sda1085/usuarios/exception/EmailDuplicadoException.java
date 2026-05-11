package cl.sda1085.usuarios.exception;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException (String email) {
        super("El email" + email + "ya se encuentra registrado en el sistema.");
    }
}
