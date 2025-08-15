package kernel.impl.exceptions;

import kernel.interfaces.exception.ApiException;
import kernel.utils.enums.ErrorCode;

public class NotFoundException extends RuntimeException implements ApiException {
	private static final long serialVersionUID = 1326269956187782952L;
	private final ErrorCode code;
	public NotFoundException(String model, ErrorCode code) {
		super(model + " Not Found");
		this.code = code;
	}
	
	public ErrorCode getStatus() {
		return this.code;
	}
	
}
