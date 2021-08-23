package pl.pbrodziak.education.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String name;
    private String lastName;
    @NotBlank(message = "Email required")
    private String email;
    @NotBlank(message = "role required")
    private String userRole;
    @NotBlank(message = "password required")
    private String password;

    public UserDto() {
    }
}
