package tech.petrepopescu.phoenix.exception;

public class CompilationException extends RuntimeException {
    public CompilationException(String errorMessage) {
        super(errorMessage);
    }
}
