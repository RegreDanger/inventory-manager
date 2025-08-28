package common.kernel.exceptions.api;

import common.kernel.exceptions.base.ApiException;
import common.kernel.utils.enums.ErrorCode;

public class DuplicatedException extends ApiException {
	private static final long serialVersionUID = -5631168156195458043L;

	public DuplicatedException(String message) {
		super(message, ErrorCode.CONFLICT_ERROR, null);
	}
	
}
