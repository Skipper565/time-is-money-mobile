package cz.uhk.zemanpe2.semproject.event;

public class ApiErrorEvent {

    private String message;

    public ApiErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
