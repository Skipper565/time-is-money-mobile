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
    private Date date;

    @SerializedName("value")
    @Expose
    private float value;

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

    public Date getDate() {
        return date;
    }

    public float getValue() {
        return value;
    }

    public String getNote() {
        return note;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getType() {
        return type;
    }
}
