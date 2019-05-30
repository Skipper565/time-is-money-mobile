package cz.uhk.zemanpe2.semproject.api;

import cz.uhk.zemanpe2.semproject.event.add.AddFinancialEntityRequestEvent;
import cz.uhk.zemanpe2.semproject.event.add.AddFinancialEntityResponseEvent;
import cz.uhk.zemanpe2.semproject.event.login.LoginResponseEvent;
import cz.uhk.zemanpe2.semproject.event.monthFinanceOverview.MonthFinanceOverviewResponseEvent;
import retrofit2.Call;
import retrofit2.http.*;

public interface TimeIsMoneyApiCalls {

    @POST("oauth/token?grant_type=password")
    public Call<LoginResponseEvent> login(@Query("username") String username, @Query("password") String password, @Header("Authorization") String authorization);

    @POST("oauth/token?grant_type=refresh_token")
    public Call<LoginResponseEvent> refresh(@Query("refresh_token") String refreshToken, @Header("Authorization") String authorization);

    @GET("api/monthFinanceOverview")
    public Call<MonthFinanceOverviewResponseEvent> monthFinanceOverview(@Query("month") String month, @Query("access_token") String accessToken);

    @Headers("Content-Type: application/json")
    @POST("api/addFinancialEntity")
    public Call<AddFinancialEntityResponseEvent> addFinancialEntity(@Body AddFinancialEntityRequestEvent addFinancialEntityRequestEvent, @Query("access_token") String accessToken);

}