package infra.shared.mappers.javalin;

import io.javalin.http.HttpStatus;
import kernel.mappers.ErrorCodeToHttpStatusMappeable;
import kernel.utils.enums.ErrorCode;

public class ErrorCodeHttpMapperJavalinAdapter implements ErrorCodeToHttpStatusMappeable<HttpStatus> {

	@Override
	public HttpStatus convert(ErrorCode code) {
		return switch(code) {
			case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
			case CONFLICT_ERROR -> HttpStatus.CONFLICT;
			case NOT_FOUND_ERROR -> HttpStatus.NOT_FOUND;
			case BAD_REQUEST_ERROR -> HttpStatus.BAD_REQUEST;
			default -> throw new IllegalArgumentException("Unexpected value: " + code);
		};
	}
	
}
