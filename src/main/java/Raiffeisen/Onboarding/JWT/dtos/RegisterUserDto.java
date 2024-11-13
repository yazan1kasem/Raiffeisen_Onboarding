package Raiffeisen.Onboarding.JWT.dtos;

public class RegisterUserDto {
    private String username;

    private String passwort;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}
