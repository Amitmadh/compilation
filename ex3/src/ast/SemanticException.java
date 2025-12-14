package ast;

/** A checked exception used by semantic analysis to report errors.
 *  Use {@link #getMessage()} to retrieve the error description.
 */
public class SemanticException extends Exception {
    public SemanticException() { super(); }
    public SemanticException(String message) { super(message); }
    public SemanticException(String message, Throwable cause) { super(message, cause); }
}
