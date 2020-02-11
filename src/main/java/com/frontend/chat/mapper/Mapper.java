package com.frontend.chat.mapper;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.domain.FriendsListDto;
import com.frontend.chat.domain.MessageDto;
import com.frontend.chat.domain.MessageUpgrated;
import com.frontend.chat.services.ChatService;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    private static Mapper mapper = null;
    private ChatService chatService = ChatService.getInstance();

    private Mapper() {
    }

    public static Mapper getInstance() {
        if (mapper == null) {
            synchronized (Mapper.class) {
                if (mapper == null) {
                    mapper = new Mapper();
                }
            }
        }
        return mapper;
    }

    public ChatUserDto mapStringDataOfUser(ChatUserDto chatUserDto) {
        return ChatUserDto.builder()
                .id(chatUserDto.getId())
                .name(mapLetters(chatUserDto.getName()))
                .surname(mapLetters(chatUserDto.getSurname()))
                .mail(chatUserDto.getMail())
                .password(chatUserDto.getPassword())
                .city(mapLetters(chatUserDto.getCity()))
                .logged(chatUserDto.isLogged())
                .friendsListDto(new FriendsListDto(chatUserDto.getFriendsListDto().getId(), chatUserDto.getFriendsListDto().getFriends().stream()
                        .map(a -> ChatUserDto.builder()
                                .id(a.getId())
                                .name(mapLetters(a.getName()))
                                .surname(mapLetters(a.getSurname()))
                                .mail(a.getMail())
                                .password(a.getPassword())
                                .city(mapLetters(a.getCity()))
                                .logged(a.isLogged()).build())
                        .collect(Collectors.toList()))).build();
    }

    public String mapLetters(String s) {
        String firstLetter = s.substring(0, 1);
        String restOfName = s.substring(1).toLowerCase();
        return "" + firstLetter + restOfName;
    }

    public List<ChatUserDto> mapListOfChatUsers(List<ChatUserDto> a) {
        return a.stream()
                .map(this::mapStringDataOfUser)
                .collect(Collectors.toList());
    }

    public MessageUpgrated mapMessageToStrings(MessageDto messageDto) {
        String m = "";
        if (messageDto.getSenderId().equals(ChatService.CURRENT_USER.getId())) {
            m = "*";
        } else {
            m = messageDto.getSenderId().toString();
        }
        String day = messageDto.getSendingDate().toString().substring(0,10);
        String hour = messageDto.getSendingDate().toString().substring(11,19);

        MessageUpgrated upgratedMessage = MessageUpgrated.builder()
                .id(messageDto.getId())
                .message(messageDto.getMessage())
                .conversationId(messageDto.getConversationId())
                .read(messageDto.isRead())
                .receiverId(messageDto.getReceiverId())
                .myMessage(m)
                .sent(day + " " + hour)
                .build();
        return upgratedMessage;
    }
}
