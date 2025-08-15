package kernel.impl.exceptions;

import kernel.interfaces.exception.ApiException;
import kernel.utils.enums.ErrorCode;

public class InternalServerException extends RuntimeException implements ApiException {
	private static final long serialVersionUID = 8698989198909481588L;
	private final ErrorCode code;
	private final Throwable cause;
	
	public InternalServerException(String message, ErrorCode code, Throwable cause) {
		super(message);
		this.code = code;
		this.cause = cause;
		if (cause != null) {
			this.initCause(cause);
		}
	}

	@Override
	public ErrorCode getStatus() {
		return code;
	}

	@Override
	public synchronized Throwable getCause() {
		return cause;
	}
	
}
