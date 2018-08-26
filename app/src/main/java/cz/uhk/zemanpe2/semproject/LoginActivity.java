package cz.uhk.zemanpe2.semproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import cz.uhk.zemanpe2.semproject.event.ApiUnauthorizedEvent;
import cz.uhk.zemanpe2.semproject.event.LoginRequestEvent;
import cz.uhk.zemanpe2.semproject.event.LoginResponseEvent;

public class LoginActivity extends AppCompatActivity {

    private Bus bus;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.eTUsername);
        password = findViewById(R.id.eTPassword);
    }

    @Override
    public void onResume() {
        super.onResume();

        getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getBus().unregister(this);
    }

    @Subscribe
    public void onLoginResponse(LoginResponseEvent event) {
        Intent i = new Intent(this, ListActivity.class);
        i.putExtra("access_token", event.getAccessToken());
        i.putExtra("refresh_token", event.getRefreshToken());
        i.putExtra("expires_in", event.getExpiresIn());
        startActivity(i);
    }

    @Subscribe
    public void onApiUnauthorized(ApiUnauthorizedEvent event) {
        Toast.makeText(getApplicationContext(), getString(R.string.error_unauthorized), Toast.LENGTH_SHORT)
                .show();
    }

    public void login(View view) {
        LoginRequestEvent loginRequestEvent =
                new LoginRequestEvent(username.getText().toString(), password.getText().toString());
        getBus().post(loginRequestEvent);
    }

    private Bus getBus() {
        if (bus == null) {
            bus = TimeIsMoneyApplication.getBus();
        }

        return bus;
    }
}
