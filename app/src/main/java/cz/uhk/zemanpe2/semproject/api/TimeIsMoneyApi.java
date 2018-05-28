package cz.uhk.zemanpe2.semproject.api;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class TimeIsMoneyApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://time-is-mony.herokuapp.com/api/?access_token=%s";

    protected TimeIsMoneyApi() {
    }

    private static class InstanceHolder {
        private static final TimeIsMoneyApi INSTANCE = new TimeIsMoneyApi();
    }

    public static TimeIsMoneyApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint(){
        return "https://time-is-mony.herokuapp.com/oauth/token?grant_type=password";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://time-is-mony.herokuapp.com/oauth/token?grant_type=refresh_token";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}