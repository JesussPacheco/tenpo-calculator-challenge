package com.jesuspacheco.tenpo.infrastructure.constant;

/**
 * API endpoint and HTTP method constants.
 */
public final class ApiConstants {

    public static final String API_VERSION = "/api/v1";
    public static final String CALCULATOR_BASE_PATH = API_VERSION + "/calculator";
    public static final String CALCULATOR_ENDPOINT = CALCULATOR_BASE_PATH + "/sum";
    public static final String HISTORY_BASE_PATH = API_VERSION + "/history";
    public static final String HTTP_METHOD_POST = "POST";

    private ApiConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
}
