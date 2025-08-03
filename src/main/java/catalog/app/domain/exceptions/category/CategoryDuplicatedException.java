package catalog.app.domain.exceptions.category;

import kernel.exceptions.interfaces.ApiException;
import kernel.utils.enums.ErrorCode;

public class CategoryDuplicatedException extends RuntimeException implements ApiException {
	private static final long serialVersionUID = -5631168156195458043L;
	private ErrorCode code;
	
	public CategoryDuplicatedException(String message, ErrorCode code) {
		super(message);
		this.code = code;
	}
	
	@Override
	public ErrorCode getStatus() {
		return this.code;
	}
	
}
