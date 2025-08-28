package common.kernel.utils.enums;

public enum ErrorCode {

	INTERNAL_SERVER_ERROR(500),
	CONFLICT_ERROR(409),
	NOT_FOUND_ERROR(404),
	BAD_REQUEST_ERROR(400);
	
	private final int code;
	
	ErrorCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
	
}
