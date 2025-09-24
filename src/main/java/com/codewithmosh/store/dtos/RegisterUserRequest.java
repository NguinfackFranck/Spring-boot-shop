package com.codewithmosh.store.dtos;

import com.codewithmosh.store.validation.LowerCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters ")
    public String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid Email")
    @LowerCase(message = "Email must be in Lowercase")
    public String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6,max = 25,message = "Password must be between 6 and 25 characters")
    public String password;
}
