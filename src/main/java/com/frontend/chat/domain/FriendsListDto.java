package com.frontend.chat.domain;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FriendsListDto {
    private Long id;
    private List<ChatUserDto> friends;
}
