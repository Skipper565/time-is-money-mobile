package cz.uhk.zemanpe2.semproject.event.add;

import android.support.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import cz.uhk.zemanpe2.semproject.adapter.YyyyMmDdDateTypeAdapter;

import java.util.Date;

public class AddFinancialEntityRequestEvent {

    @Expose(serialize = false)
    private String accessToken;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("value")
    @Expose
    private float value;

    @SerializedName("note")
    @Expose
    @Nullable
    private String note;

    @SerializedName("permanent")
    @Expose
    private boolean isPermanent;

    @SerializedName("monthDay")
    @Expose
    @Nullable
    private Integer monthDay;

    @SerializedName("date")
    @JsonAdapter(YyyyMmDdDateTypeAdapter.class)
    @Expose
    private Date date;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }

    public void setMonthDay(Integer monthDay) {
        this.monthDay = monthDay;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
