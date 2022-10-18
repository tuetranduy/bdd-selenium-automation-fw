package org.unsplash.exercise.dtos;

import lombok.Getter;
import lombok.Setter;

public class UserDto {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    public String getUserFullName() {
        return this.firstName + " " + this.lastName;
    }
}
