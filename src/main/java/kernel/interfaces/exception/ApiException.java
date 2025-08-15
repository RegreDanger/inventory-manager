package kernel.interfaces.exception;

import kernel.utils.enums.ErrorCode;

public interface ApiException {
	String getMessage();
	ErrorCode getStatus();
}
