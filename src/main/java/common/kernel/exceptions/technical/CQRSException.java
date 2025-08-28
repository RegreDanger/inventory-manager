package common.kernel.exceptions.technical;

import java.lang.reflect.Method;

public class CQRSException extends CQRSModuleInstantiationException {
    
    public CQRSException(Throwable cause) {
        super(cause);
    }
    
    public CQRSException(String message) {
        super(message);
    }

    public CQRSException(String message, Throwable cause) {
        super(message, cause);
    }

    public CQRSException(Method method, Throwable cause) {
        super("Failed to register command or query from method: " + method.getName(), cause);
    }
}
