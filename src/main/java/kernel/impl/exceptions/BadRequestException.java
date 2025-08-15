package kernel.impl.exceptions;

import kernel.interfaces.exception.ApiException;
import kernel.utils.enums.ErrorCode;

public class BadRequestException extends RuntimeException implements ApiException {
	private static final long serialVersionUID = 8698989198909481588L;
	private final ErrorCode code;
	
	public BadRequestException(String message, ErrorCode code) {
		super(message);
		this.code = code;
	}

	@Override
	public ErrorCode getStatus() {
		return code;
	}
	
}
