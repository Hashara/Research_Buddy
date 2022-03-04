package com.example.researchbuddy.model;

import com.example.researchbuddy.model.type.Role;

public class UserModel {
    private Role role;
    private String firstName;
    private String lastName;

    public UserModel(String firstName, String lastName, Role role) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public UserModel() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "role=" + role +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
