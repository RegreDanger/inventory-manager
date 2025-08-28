package common.platform.config.javalin.adapters.mappers;

import common.kernel.exceptions.api.InternalServerException;
import common.kernel.mappers.ErrorCodeMappeable;
import common.kernel.utils.enums.ErrorCode;
import io.javalin.http.HttpStatus;

public class JavalinErrorCodeMapper implements ErrorCodeMappeable<HttpStatus> {

	@Override
	public HttpStatus convert(ErrorCode code) {
		return switch(code) {
			case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
			case CONFLICT_ERROR -> HttpStatus.CONFLICT;
			case NOT_FOUND_ERROR -> HttpStatus.NOT_FOUND;
			case BAD_REQUEST_ERROR -> HttpStatus.BAD_REQUEST;
			default -> throw new InternalServerException(new IllegalArgumentException("Unexpected value: " + code));
		};
	}
	
}
