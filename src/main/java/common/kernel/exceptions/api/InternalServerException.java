package common.kernel.exceptions.api;

import common.kernel.exceptions.base.ApiException;
import common.kernel.utils.enums.ErrorCode;

public class InternalServerException extends ApiException {
	private static final long serialVersionUID = 8698989198909481588L;
	
	public InternalServerException() {
        super("Internal server error", ErrorCode.INTERNAL_SERVER_ERROR, null);
    }

    public InternalServerException(Throwable cause) {
        super("Internal server error", ErrorCode.INTERNAL_SERVER_ERROR, cause);
    }

	public InternalServerException(String message, Throwable cause) {
        super(message, ErrorCode.INTERNAL_SERVER_ERROR, cause);
    }
	
}
