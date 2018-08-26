package cz.uhk.zemanpe2.semproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import cz.uhk.zemanpe2.semproject.event.MonthFinanceOverviewRequestEvent;
import cz.uhk.zemanpe2.semproject.event.MonthFinanceOverviewResponseEvent;

import java.util.Date;

public class ListActivity extends AppCompatActivity {

    private Bus bus;
    private String accessToken, refreshToken;
    private Long expiresIn;
    private TextView tWAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tWAccessToken = findViewById(R.id.tWAccessToken);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            accessToken = extras.getString("access_token");
            refreshToken = extras.getString("refresh_token");
            expiresIn = extras.getLong("expires_in");
        }

        tWAccessToken.setText(accessToken);
        MonthFinanceOverviewRequestEvent monthFinanceOverviewRequestEvent =
                new MonthFinanceOverviewRequestEvent(new Date(), accessToken);
        getBus().post(monthFinanceOverviewRequestEvent);
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
    public void onMonthFinanceOverviewResponse(MonthFinanceOverviewResponseEvent event) {
        tWAccessToken.setText(String.valueOf(event.getStartBalance()));
    }

    public void changeMonth(View view) {
        MonthFinanceOverviewRequestEvent monthFinanceOverviewRequestEvent =
                new MonthFinanceOverviewRequestEvent(new Date(), accessToken);
        getBus().post(monthFinanceOverviewRequestEvent);
    }

    private Bus getBus() {
        if (bus == null) {
            bus = TimeIsMoneyApplication.getBus();
        }

        return bus;
    }

}