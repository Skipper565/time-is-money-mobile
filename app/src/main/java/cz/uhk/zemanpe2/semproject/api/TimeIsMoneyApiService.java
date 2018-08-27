package cz.uhk.zemanpe2.semproject.api;

import android.util.Base64;
import android.util.Log;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import cz.uhk.zemanpe2.semproject.event.add.AddRequestEvent;
import cz.uhk.zemanpe2.semproject.event.add.AddResponseEvent;
import cz.uhk.zemanpe2.semproject.event.api.ApiErrorEvent;
import cz.uhk.zemanpe2.semproject.event.api.ApiUnauthorizedEvent;
import cz.uhk.zemanpe2.semproject.event.login.LoginRequestEvent;
import cz.uhk.zemanpe2.semproject.event.login.LoginResponseEvent;
import cz.uhk.zemanpe2.semproject.event.monthFinanceOverview.MonthFinanceOverviewRequestEvent;
import cz.uhk.zemanpe2.semproject.event.monthFinanceOverview.MonthFinanceOverviewResponseEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
        DateFormat df = new SimpleDateFormat("MM-yyyy", Locale.ENGLISH);
        String month = df.format(event.getDate());
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

    @Subscribe
    public void onAddRequest(AddRequestEvent event) {
        Call<AddResponseEvent> addApiCall = api.add(event, event.getAccessToken());
        addApiCall.enqueue(new Callback<AddResponseEvent>() {
            @Override
            public void onResponse(Call<AddResponseEvent> call, Response<AddResponseEvent> response) {
                bus.post(new AddResponseEvent());
            }

            @Override
            public void onFailure(Call<AddResponseEvent> call, Throwable throwable) {
                bus.post(new ApiErrorEvent(throwable.getMessage()));
            }
        });
    }

}
