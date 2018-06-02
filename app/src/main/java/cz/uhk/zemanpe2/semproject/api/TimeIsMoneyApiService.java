package cz.uhk.zemanpe2.semproject.api;

import android.util.Base64;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import cz.uhk.zemanpe2.semproject.event.ApiErrorEvent;
import cz.uhk.zemanpe2.semproject.event.ApiUnauthorizedEvent;
import cz.uhk.zemanpe2.semproject.event.LoginRequestEvent;
import cz.uhk.zemanpe2.semproject.event.LoginResponseEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeIsMoneyApiService {

    private static final String CREDENTIALS = "my-trusted-client:secret";
    private TimeIsMoneyApiCalls api;
    private Bus bus;

    public TimeIsMoneyApiService(TimeIsMoneyApiCalls api, Bus bus) {
        this.api = api;
        this.bus = bus;
    }

    @Subscribe
    public void onLoginRequest(LoginRequestEvent event) {
        String authorization = "Basic " + Base64.encodeToString(CREDENTIALS.getBytes(), Base64.NO_WRAP);
        Call<LoginResponseEvent> tokenApiCall = api.login(event.getUsername(), event.getPassword(), authorization);
        tokenApiCall.enqueue(new Callback<LoginResponseEvent>() {
            @Override
            public void onResponse(Call<LoginResponseEvent> call, Response<LoginResponseEvent> response) {
                LoginResponseEvent token = response.body();
                if (response.code() == 401) {
                    bus.post(new ApiUnauthorizedEvent());
                } else if (token == null) {
                    bus.post(new ApiErrorEvent("Token is null"));
                } else {
                    bus.post(token);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseEvent> call, Throwable throwable) {
                bus.post(new ApiErrorEvent(throwable.getMessage()));
            }
        });
    }
}
