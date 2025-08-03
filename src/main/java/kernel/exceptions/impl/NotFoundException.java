package kernel.exceptions.impl;

import kernel.exceptions.interfaces.ApiException;
import kernel.utils.enums.ErrorCode;

public class NotFoundException extends RuntimeException implements ApiException {
	private static final long serialVersionUID = 1326269956187782952L;
	private ErrorCode code;
	public NotFoundException(String model, ErrorCode code) {
		super(model + " Not Found");
		this.code = code;;
	}
	
	public ErrorCode getStatus() {
		return this.code;
	}
	
}
