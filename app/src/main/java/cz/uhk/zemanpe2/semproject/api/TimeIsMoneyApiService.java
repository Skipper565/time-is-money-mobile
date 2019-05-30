package cz.uhk.zemanpe2.semproject.api;

import android.util.Base64;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import cz.uhk.zemanpe2.semproject.event.add.AddFinancialEntityRequestEvent;
import cz.uhk.zemanpe2.semproject.event.add.AddFinancialEntityResponseEvent;
import cz.uhk.zemanpe2.semproject.event.api.ApiErrorEvent;
import cz.uhk.zemanpe2.semproject.event.api.ApiUnauthorizedEvent;
import cz.uhk.zemanpe2.semproject.event.login.LoginRequestEvent;
import cz.uhk.zemanpe2.semproject.event.login.LoginResponseEvent;
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

    @Subscribe
    public void onAddFinancialEntityRequest(AddFinancialEntityRequestEvent event) {
        Call<AddFinancialEntityResponseEvent> addApiCall = api.addFinancialEntity(event, event.getAccessToken());
        addApiCall.enqueue(new Callback<AddFinancialEntityResponseEvent>() {
            @Override
            public void onResponse(Call<AddFinancialEntityResponseEvent> call, Response<AddFinancialEntityResponseEvent> response) {
                if (response.code() == 201) {
                    bus.post(new AddFinancialEntityResponseEvent());
                } else {
                    bus.post(new ApiErrorEvent(response.body().getErrors().toString()));
                }
            }

            @Override
            public void onFailure(Call<AddFinancialEntityResponseEvent> call, Throwable throwable) {
                bus.post(new ApiErrorEvent(throwable.getMessage()));
            }
        });
    }

}
