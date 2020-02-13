package com.frontend.chat.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChatUserDto {
    private  Long id;
    private String name;
    private String surname;
    private String mail;
    private String password;
    private String city;
    private boolean logged;
    private Long friendsListIdDto;

    @Override
    public String toString() {
        return "ChatUserDto{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
