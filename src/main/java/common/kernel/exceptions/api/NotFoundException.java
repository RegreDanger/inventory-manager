package common.kernel.exceptions.api;

import common.kernel.exceptions.base.ApiException;
import common.kernel.utils.enums.ErrorCode;

public class NotFoundException extends ApiException {
	private static final long serialVersionUID = 1326269956187782952L;
	public NotFoundException(String model) {
		super(model + " Not Found", ErrorCode.NOT_FOUND_ERROR);
	}
}
