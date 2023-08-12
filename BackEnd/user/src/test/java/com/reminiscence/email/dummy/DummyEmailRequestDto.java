package com.reminiscence.email.dummy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DummyEmailRequestDto {
    private String email;
    public DummyEmailRequestDto() {
    }
    public DummyEmailRequestDto(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
}
