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
import cz.uhk.zemanpe2.semproject.event.monthFinanceOverview.MonthFinanceOverviewRequestEvent;
import cz.uhk.zemanpe2.semproject.event.monthFinanceOverview.MonthFinanceOverviewResponseEvent;

import java.util.Date;

public class ListActivity2 extends AppCompatActivity {

    private Bus bus;
    private String accessToken, refreshToken;
    private Long expiresIn;
    private TextView tWStartBalance, tWEndBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);

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

        tWStartBalance = findViewById(R.id.tWStartBalance);
        tWEndBalance = findViewById(R.id.tWEndBalance);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            accessToken = extras.getString("access_token");
            refreshToken = extras.getString("refresh_token");
            expiresIn = extras.getLong("expires_in");
        }

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
        tWStartBalance.setText(String.valueOf(event.getStartBalance()));
        tWEndBalance.setText(String.valueOf(event.getEndBalance()));
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