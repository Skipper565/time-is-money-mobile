package cz.uhk.zemanpe2.semproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import cz.uhk.zemanpe2.semproject.event.add.AddRequestEvent;
import cz.uhk.zemanpe2.semproject.event.add.AddResponseEvent;
import cz.uhk.zemanpe2.semproject.event.api.ApiErrorEvent;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private Bus bus;
    private Spinner type;
    private EditText value, note, monthDay, date;
    private CheckBox permanent;
    private String accessToken, refreshToken;
    private Long expiresIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        String[] arraySpinner = new String[] {
                "cost", "revenue"
        };
        type = findViewById(R.id.sType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, arraySpinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        type = findViewById(R.id.sType);
        value = findViewById(R.id.eTValue);
        note = findViewById(R.id.eTNote);
        permanent = findViewById(R.id.chBPermanent);
        monthDay = findViewById(R.id.eTMonthDay);
        date = findViewById(R.id.eTDate);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            accessToken = extras.getString("access_token");
            refreshToken = extras.getString("refresh_token");
            expiresIn = extras.getLong("expires_in");
        }
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
    public void onCreateResponse(AddResponseEvent event) {
        Intent i = new Intent(this, ListActivity.class);
        i.putExtra("access_token", accessToken);
        i.putExtra("refresh_token", refreshToken);
        i.putExtra("expires_in", expiresIn);
        startActivity(i);
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        Toast.makeText(getApplicationContext(), getString(R.string.error_create), Toast.LENGTH_SHORT)
                .show();
    }

    public void create(View view) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        AddRequestEvent addRequestEvent = new AddRequestEvent();
        addRequestEvent.setAccessToken(accessToken);
        addRequestEvent.setType(type.toString());
        addRequestEvent.setValue(Float.parseFloat(value.getText().toString()));
        addRequestEvent.setNote(note.getText().toString());
        addRequestEvent.setPermanent(permanent.isChecked());
        addRequestEvent.setMonthDay(Integer.parseInt(monthDay.getText().toString()));
        addRequestEvent.setDate(df.parse(date.getText().toString()));
        getBus().post(addRequestEvent);
    }

    private Bus getBus() {
        if (bus == null) {
            bus = TimeIsMoneyApplication.getBus();
        }

        return bus;
    }

}
