package cz.uhk.zemanpe2.semproject.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MonthFinanceOverviewResponseEvent {

    @SerializedName("startBalance")
    @Expose
    private float startBalance;

    @SerializedName("endBalance")
    @Expose
    private float endBalance;

    //private List<FinancialEntity> financeList;

    public float getStartBalance() {
        return startBalance;
    }

    public float getEndBalance() {
        return endBalance;
    }

    /*public List<FinancialEntity> getFinanceList() {
        return financeList;
    }*/

}
