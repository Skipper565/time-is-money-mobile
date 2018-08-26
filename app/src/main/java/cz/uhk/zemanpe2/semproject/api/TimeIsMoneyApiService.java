package cz.uhk.zemanpe2.semproject.api;

import android.util.Base64;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import cz.uhk.zemanpe2.semproject.event.*;
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
    public void onMonthFinanceOverviewRequest(MonthFinanceOverviewRequestEvent event) {
        String month = event.getDate().toString();
        Call<MonthFinanceOverviewResponseEvent> monthFinanceApiCall =
                api.monthFinanceOverview(month, event.getAccessToken());
        monthFinanceApiCall.enqueue(new Callback<MonthFinanceOverviewResponseEvent>() {
            @Override
            public void onResponse(Call<MonthFinanceOverviewResponseEvent> call, Response<MonthFinanceOverviewResponseEvent> response) {
                MonthFinanceOverviewResponseEvent monthFinanceOverview = response.body();
                if (monthFinanceOverview == null) {
                    bus.post(new ApiErrorEvent("Month finance overview is null"));
                } else {
                    bus.post(monthFinanceOverview);
                }
            }

            @Override
            public void onFailure(Call<MonthFinanceOverviewResponseEvent> call, Throwable throwable) {
                bus.post(new ApiErrorEvent(throwable.getMessage()));
            }
        });
    }
}
