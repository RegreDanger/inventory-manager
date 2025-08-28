package common.kernel.exceptions.technical;

public class CQRSModuleInstantiationException extends Exception {
    private static final long serialVersionUID = 7573722834302481796L;

    public CQRSModuleInstantiationException(Throwable cause) {
        super(cause);
    }
    
    public CQRSModuleInstantiationException(String message) {
        super(message);
    }

    public CQRSModuleInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
