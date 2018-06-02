package cz.uhk.zemanpe2.semproject.api;

import cz.uhk.zemanpe2.semproject.event.LoginResponseEvent;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TimeIsMoneyApiCalls {

    @POST("oauth/token?grant_type=password")
    public Call<LoginResponseEvent> login(@Query("username") String username, @Query("password") String password, @Header("Authorization") String authorization);

    @POST("oauth/token?grant_type=refresh_token")
    public Call<LoginResponseEvent> refresh(@Query("refresh_token") String refreshToken, @Header("Authorization") String authorization);
}