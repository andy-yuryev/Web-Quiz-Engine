package engine.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDto {

    private long id;

    @NotNull(message = "Email is required")
    @Email(message = "Email is invalid",
            regexp = "^[\\w-.]{4,}@[\\w-]+\\.[\\w-.]+$")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 5, message = "Password must contain at least five characters")
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
