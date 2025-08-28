package common.kernel.utils.enums;

public enum UserMessage {
    BAD_REQUEST_MESSAGE("Bad request"),
    INTERNAL_SERVER_ERROR_MESSAGE("Internal Server Error"),
    DUPLICATED_MESSAGE("This item has already been added");

    private final String message;

    private UserMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }

}
