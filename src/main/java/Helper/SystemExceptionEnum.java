package Helper;

public enum SystemExceptionEnum {

    COMPANY_NOT_FOUND("Couldn't find company"),
    COUPON_NOT_FOUND("Couldn't find coupon"),
    CUSTOMER_NOT_FOUND("Couldn't find found"),
    INVALID_DATE("End date must proceed start date, and be in the future"),
    INVALID_AMOUNT("Coupon amount must be greater than 0"),
    EMAIL_ALREADY_USED("Email already in use"),
    CANNOT_UPDATE_COMPANY_NAME("cannot update a company's name"),
    EMAIL_OR_PASSWORD_WRONG("email or password are wrong!"),
    CANNOT_UPDATE_ID("cannot update company ID!"),
    CANNOT_DELETE_ANOTHER("cannot delete other company's coupon"),
    COUPON_OUT_OF_STOCK("coupon out of stock"),
    ALREADY_OWNS("customer already owns this coupon"),
    EXPIRED_COUPONS("coupon has expired!");

    private final String message;

    SystemExceptionEnum(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }
}
