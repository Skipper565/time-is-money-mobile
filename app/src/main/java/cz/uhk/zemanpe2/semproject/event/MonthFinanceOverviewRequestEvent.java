package cz.uhk.zemanpe2.semproject.event;

import java.util.Date;

public class MonthFinanceOverviewRequestEvent {

    private Date date;
    private String accessToken;

    public MonthFinanceOverviewRequestEvent(Date date, String accessToken) {
        this.date = date;
        this.accessToken = accessToken;
    }

    public Date getDate() {
        return date;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
