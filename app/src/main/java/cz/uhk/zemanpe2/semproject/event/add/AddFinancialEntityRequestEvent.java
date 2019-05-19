package cz.uhk.zemanpe2.semproject.event.add;

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
    private String note;

    @SerializedName("permanent")
    @Expose
    private boolean isPermanent;

    @SerializedName("monthDay")
    @Expose
    private int monthDay;

    @SerializedName("date")
    @JsonAdapter(YyyyMmDdDateTypeAdapter.class)
    @Expose
    private Date date;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }

    public int getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(int monthDay) {
        this.monthDay = monthDay;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
