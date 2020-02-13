package com.frontend.chat.mapper;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.domain.MessageDto;
import com.frontend.chat.domain.MessageUpgrated;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    private static Mapper mapper = null;

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
                .friendsListIdDto(chatUserDto.getFriendsListIdDto()).build();
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
    public MessageUpgrated mapMessageToStrings(MessageDto messageDto, ChatUserDto chatUserDto) {
        String m = "";
        if (messageDto.getSenderId().equals(chatUserDto.getId()))   m = "*";
        String day = messageDto.getSendingDate().toString().substring(0,10);
        String hour = messageDto.getSendingDate().toString().substring(11,19);
        return MessageUpgrated.builder()
                .id(messageDto.getId())
                .message(messageDto.getMessage())
                .conversationId(messageDto.getConversationId())
                .read(messageDto.isRead())
                .receiverId(messageDto.getReceiverId())
                .myMessage(m)
                .sent(day + " " + hour)
                .build();
    }

    public List<MessageUpgrated> mapMessageToStringsList(List<MessageDto> list, ChatUserDto chatUserDto) {
        return list.stream()
                .map(a -> mapMessageToStrings(a,chatUserDto))
                .collect(Collectors.toList());
    }
}
