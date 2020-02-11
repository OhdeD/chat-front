package com.frontend.chat.domain;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MessageDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String message;
    private LocalDateTime sendingDate;
    private Long conversationId;
    private boolean read;
}