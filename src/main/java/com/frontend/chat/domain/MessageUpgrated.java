package com.frontend.chat.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MessageUpgrated {
    private Long id;
    private String myMessage;
    private Long receiverId;
    private String message;
    private String sent;
    private Long conversationId;
    private boolean read;
}