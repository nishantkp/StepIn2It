package com.stepin2it.data.models;

/**
 * Custom object form sending login details to http://reqres.in
 */
public class RqLogin {
    String email;
    String password;

    public RqLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
