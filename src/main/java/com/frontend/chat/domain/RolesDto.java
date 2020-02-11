package com.frontend.chat.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RolesDto {
    private Long id;
    private String role;
    private ChatUserDto chatUserDto;
}
