package com.oldmen.testexercise;

public class UserContainer {

    private String userLogin;
    private String userSkipRegistrMode;
    private String userToken;

    public static class Builder {
        private String userLogin;
        private String userSkipMode;
        private String userToken;

        public Builder login(String userLogin) {
            this.userLogin = userLogin;
            return this;
        }

        public Builder password(String userPassword) {
            this.userSkipMode = userPassword;
            return this;
        }

        public Builder token(String userToken) {
            this.userToken = userToken;
            return this;
        }

        public UserContainer build() {
            UserContainer user = new UserContainer();
            user.userLogin = this.userLogin;
            user.userSkipRegistrMode = this.userSkipMode;
            user.userToken = this.userToken;
            return user;
        }

    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserSkipRegistrMode() {
        return userSkipRegistrMode;
    }

    public void setUserSkipRegistrMode(String userSkipRegistrMode) {
        this.userSkipRegistrMode = userSkipRegistrMode;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}