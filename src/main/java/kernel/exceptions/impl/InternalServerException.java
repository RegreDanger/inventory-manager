package kernel.exceptions.impl;

import kernel.exceptions.interfaces.ApiException;
import kernel.utils.enums.ErrorCode;

public class InternalServerException extends RuntimeException implements ApiException {
	private static final long serialVersionUID = 8698989198909481588L;
	private ErrorCode code;
	
	public InternalServerException(String message, ErrorCode code) {
		super(message);
		this.code = code;
	}

	@Override
	public ErrorCode getStatus() {
		return code;
	}
	
}
