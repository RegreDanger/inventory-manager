package kernel.mappers;

import kernel.utils.enums.ErrorCode;

public interface ErrorCodeToHttpStatusMappeable<T> {
	public T convert(ErrorCode code);
}
