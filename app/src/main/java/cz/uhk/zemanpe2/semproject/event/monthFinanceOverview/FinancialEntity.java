package cz.uhk.zemanpe2.semproject.event.monthFinanceOverview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FinancialEntity {

    @SerializedName("id")
    @Expose
    protected Integer id;

    @SerializedName("date")
    @Expose
    protected Date date;

    @SerializedName("value")
    @Expose
    protected float value;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("latitude")
    @Expose
    private Float latitude;

    @SerializedName("longitude")
    @Expose
    private Float longitude;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("isPermanent")
    @Expose
    private Boolean isPermanent;

    @SerializedName("monthDay")
    @Expose
    private Integer monthDay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsPermanent() {
        return isPermanent;
    }

    public void setIsPermanent(Boolean isPermanent) {
        this.isPermanent = isPermanent;
    }

    public Integer getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(Integer monthDay) {
        this.monthDay = monthDay;
    }

}
