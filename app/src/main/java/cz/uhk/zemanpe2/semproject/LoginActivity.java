package cz.uhk.zemanpe2.semproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.codepath.oauth.OAuthLoginActivity;
import cz.uhk.zemanpe2.semproject.api.TimeIsMoneyClient;

public class LoginActivity extends OAuthLoginActivity<TimeIsMoneyClient> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onLoginSuccess() {
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    public void login(View view) {
        getClient().connect();
    }
}