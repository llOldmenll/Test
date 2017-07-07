package com.oldmen.testexercise.container;



public class PostRetrofitResponse {

    /**
     * success : true
     * token : <token>
     */

    private boolean success;
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
