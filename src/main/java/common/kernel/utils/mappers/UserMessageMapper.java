package common.kernel.utils.mappers;

import common.kernel.exceptions.api.BadRequestException;
import common.kernel.exceptions.api.DuplicatedException;
import common.kernel.exceptions.api.InternalServerException;
import common.kernel.exceptions.base.ApiException;
import common.kernel.utils.enums.UserMessage;

public class UserMessageMapper {

    private UserMessageMapper() {}

    public static String getMessage(ApiException e) {
        return switch(e) {
            case InternalServerException i -> UserMessage.INTERNAL_SERVER_ERROR_MESSAGE.toString();
            case BadRequestException b -> UserMessage.BAD_REQUEST_MESSAGE.toString();
            case DuplicatedException d -> UserMessage.DUPLICATED_MESSAGE.toString();
			default -> throw new InternalServerException(new IllegalArgumentException("Unexpected value: " + e));
        };
            
    }
}
