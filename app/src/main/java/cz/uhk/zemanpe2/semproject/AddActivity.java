package cz.uhk.zemanpe2.semproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import cz.uhk.zemanpe2.semproject.event.add.AddFinancialEntityRequestEvent;
import cz.uhk.zemanpe2.semproject.event.add.AddFinancialEntityResponseEvent;
import cz.uhk.zemanpe2.semproject.event.api.ApiErrorEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddActivity extends AppCompatActivity {

    private Bus bus;
    private Spinner type;
    private EditText value, note, monthDay, date;
    private CheckBox permanent;
    private String accessToken, refreshToken;
    private Integer position;
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

        value = findViewById(R.id.eTValue);
        note = findViewById(R.id.eTNote);
        permanent = findViewById(R.id.chBPermanent);
        monthDay = findViewById(R.id.eTMonthDay);
        date = findViewById(R.id.eTDate);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            accessToken = extras.getString("access_token");
            refreshToken = extras.getString("refresh_token");
            position = extras.getInt("position");
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
    public void onCreateResponse(AddFinancialEntityResponseEvent event) {
        Intent i = new Intent(this, ListActivity.class);
        i.putExtra("access_token", accessToken);
        i.putExtra("refresh_token", refreshToken);
        i.putExtra("position", position);
        i.putExtra("expires_in", expiresIn);
        startActivity(i);
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        Toast.makeText(getApplicationContext(), getString(R.string.error_create), Toast.LENGTH_SHORT)
                .show();
    }

    public void create(View view) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        AddFinancialEntityRequestEvent addFinancialEntityRequestEvent = new AddFinancialEntityRequestEvent();
        addFinancialEntityRequestEvent.setAccessToken(accessToken);
        addFinancialEntityRequestEvent.setType(type.getSelectedItem().toString());
        addFinancialEntityRequestEvent.setValue(Float.parseFloat(value.getText().toString()));
        addFinancialEntityRequestEvent.setPermanent(permanent.isChecked());
        addFinancialEntityRequestEvent.setDate(df.parse(date.getText().toString()));
        if (!note.getText().toString().equals("")) {
            addFinancialEntityRequestEvent.setNote(note.getText().toString());
        }

        if (!monthDay.getText().toString().equals("") && permanent.isChecked()) {
            addFinancialEntityRequestEvent.setMonthDay(Integer.parseInt(monthDay.getText().toString()));
        }

        LocationManager locManager;
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                addFinancialEntityRequestEvent.setLatitude(String.valueOf(location.getLatitude()));
                addFinancialEntityRequestEvent.setLongitude(String.valueOf(location.getLongitude()));
            }
        }

        getBus().post(addFinancialEntityRequestEvent);
    }

    public void onPermanentClicked(View view) {
        if (((CheckBox) view).isChecked())
            monthDay.setVisibility(View.VISIBLE);
        else
            monthDay.setVisibility(View.GONE);
    }

    private Bus getBus() {
        if (bus == null) {
            bus = TimeIsMoneyApplication.getBus();
        }

        return bus;
    }

}
