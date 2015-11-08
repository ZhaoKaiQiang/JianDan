package com.socks.jiandan.model;

public class Vote {

    public static final String URL_VOTE = "http://jandan.net/index" +
            ".php?acv_ajax=true&option=%s&ID=%s";

    public static final String XX = "0";
    public static final String OO = "1";

    public static final String RESULT_XX_SUCCESS = "-1";
    public static final String RESULT_OO_SUCCESS = "1";
    public static final String RESULT_HAVE_VOTED = "0";

    private String result;
    private String message;

    public Vote() {
    }

    public Vote(String result, String message) {
        this.result = result;
        this.message = message;
    }

    public static String getXXUrl(String id) {
        return String.format(URL_VOTE, XX, id);
    }

    public static String getOOUrl(String id) {
        return String.format(URL_VOTE, OO, id);
    }

    public static Vote getInstance(String response) {
        String[] results = response.split("\\|");
        return new Vote(results[2], results[1]);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
