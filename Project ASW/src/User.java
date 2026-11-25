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
        return this.isActive
                && username != null
                && password != null
                && username.equals(this.username)
                && password.equals(this.password);
    }

    public void logout() {
        System.out.println(this.username + " has been logged out");
    }

    public void updateProfile(String username, String password) {
        if (username != null && !username.isBlank()) {
            this.username = username;
        }
        if (password != null && !password.isBlank()) {
            this.password = password;
        }
        Database.getInstance().updateUser(this);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (oldPassword == null || newPassword == null || newPassword.isBlank()) {
            return false;
        }
        if (!oldPassword.equals(this.password)) {
            return false;
        }
        this.password = newPassword;
        Database.getInstance().updateUser(this);
        return true;
    }

    public void deactivateAccount() {
        this.isActive = false;
        Database.getInstance().updateUser(this);
    }

    public void activateAccount() {
        this.isActive = true;
        Database.getInstance().updateUser(this);
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
