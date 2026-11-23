import java.time.LocalDate;

public abstract class User {
    private Integer userID;
    private String name;
    private String email;
    private String username;
    private String password;
    private LocalDate birthDate;
    private boolean isActive;

    public User(Integer userID, String name, String email, String password, String username, LocalDate birthDate, boolean isActive) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
        this.birthDate = birthDate;
        this.isActive = isActive;
    }

    public boolean login(String username, String password) {
        return username.equals(this.username) && password.equals(this.password);
    }
}
