package cz.uhk.zemanpe2.semproject.event.login;

public class LoginRequestEvent {

    private String username;
    private String password;

    public LoginRequestEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
