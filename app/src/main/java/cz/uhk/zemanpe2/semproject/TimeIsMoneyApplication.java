package cz.uhk.zemanpe2.semproject;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import cz.uhk.zemanpe2.semproject.api.TimeIsMoneyApiCalls;
import cz.uhk.zemanpe2.semproject.api.TimeIsMoneyApiService;
import cz.uhk.zemanpe2.semproject.event.api.ApiErrorEvent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TimeIsMoneyApplication extends Application {

    private static final String API_URL = "https://time-is-mony.herokuapp.com/";
    private static final Bus bus = new Bus();

    @Override
    public void onCreate() {
        super.onCreate();

        TimeIsMoneyApiService service = new TimeIsMoneyApiService(buildApi(), bus);
        bus.register(service);

        bus.register(this);
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        Toast.makeText(getApplicationContext(), getText(R.string.error_service_unavailable), Toast.LENGTH_SHORT)
                .show();
        Log.e("API_Error", event.getMessage());
    }

    static Bus getBus() {
        return bus;
    }

    private TimeIsMoneyApiCalls buildApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TimeIsMoneyApiCalls.class);
    }
}