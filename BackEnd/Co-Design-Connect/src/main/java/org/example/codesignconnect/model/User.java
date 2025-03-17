package org.example.codesignconnect.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
