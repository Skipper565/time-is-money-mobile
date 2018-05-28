package cz.uhk.zemanpe2.semproject.api;

import android.content.Context;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TimeIsMoneyClient extends OAuthBaseClient {
    public static final BaseApi REST_API_INSTANCE = TimeIsMoneyApi.instance();
    public static final String REST_URL = "https://time-is-mony.herokuapp.com/";
    public static final String REST_CONSUMER_KEY = "SOME_KEY_HERE";
    public static final String REST_CONSUMER_SECRET = "SOME_SECRET_HERE";
    public static final String REST_CALLBACK_URL = "https://time-is-mony.herokuapp.com/";

    public TimeIsMoneyClient(Context context) {
        super(context, REST_API_INSTANCE, REST_URL,
                REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    // ENDPOINTS BELOW

    public void getList(int month, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("api/monthFinanceOverview");
        RequestParams params = new RequestParams();
        params.put("month", String.valueOf(month));
        client.get(apiUrl, params, handler);
    }
}