package common.kernel.mappers;

import common.kernel.utils.enums.ErrorCode;

public interface ErrorCodeMappeable<T> {
	public T convert(ErrorCode code);
}
