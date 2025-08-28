package common.kernel.exceptions.api;

import common.kernel.exceptions.base.ApiException;
import common.kernel.utils.enums.ErrorCode;

public class BadRequestException extends ApiException {
	private static final long serialVersionUID = 8698989198909481588L;
	
	public BadRequestException(String message) {
		super(message, ErrorCode.BAD_REQUEST_ERROR);
	}
	
}
