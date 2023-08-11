package com.reminiscence.email.dummy;

public class DummyEmailCheckRequestDto {

    private String email;
    private String token;

    public DummyEmailCheckRequestDto(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
