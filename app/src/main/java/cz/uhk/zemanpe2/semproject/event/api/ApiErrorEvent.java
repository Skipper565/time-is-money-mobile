package cz.uhk.zemanpe2.semproject.event.api;

public class ApiErrorEvent {

    private String message;

    public ApiErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
