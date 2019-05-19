package cz.uhk.zemanpe2.semproject.event.add;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;

public class AddFinancialEntityResponseEvent {

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("errors")
    @Expose
    private Array errors;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Array getErrors() {
        return errors;
    }

    public void setErrors(Array errors) {
        this.errors = errors;
    }
}
