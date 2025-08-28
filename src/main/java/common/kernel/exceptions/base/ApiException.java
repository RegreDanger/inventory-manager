package common.kernel.exceptions.base;

import common.kernel.utils.enums.ErrorCode;

public class ApiException extends RuntimeException {
	private final ErrorCode code;

	protected ApiException(String message, ErrorCode code, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	protected ApiException(String message, ErrorCode code) {
		this(message, code, null);
	}

	public ErrorCode getErrorCode() {
    	return this.code;
	}
}
