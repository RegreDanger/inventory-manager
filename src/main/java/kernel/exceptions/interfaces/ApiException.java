package kernel.exceptions.interfaces;

import kernel.utils.enums.ErrorCode;

public interface ApiException {
	String getMessage();
	ErrorCode getStatus();
}
