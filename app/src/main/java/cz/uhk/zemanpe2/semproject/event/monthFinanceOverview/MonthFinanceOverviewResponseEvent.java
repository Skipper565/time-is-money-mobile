package cz.uhk.zemanpe2.semproject.event.monthFinanceOverview;

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

    @SerializedName("financeList")
    @Expose
    private List<FinancialEntity> financeList = null;

    public float getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(float startBalance) {
        this.startBalance = startBalance;
    }

    public float getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(float endBalance) {
        this.endBalance = endBalance;
    }

    public List<FinancialEntity> getFinanceList() {
        return financeList;
    }

    public void setFinanceList(List<FinancialEntity> financeList) {
        this.financeList = financeList;
    }
}
