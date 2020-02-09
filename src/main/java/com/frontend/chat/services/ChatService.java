package com.frontend.chat.services;

import com.frontend.chat.domain.ChatUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

public class ChatService {
    private final static String CHAT = "http://localhost:8083/v1";
    private RestTemplate restTemplate = new RestTemplate();
    private static ChatService chatService = null;
    public static ChatUserDto CURRENT_USER;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private ChatService() {
    }

    public static ChatService getInstance() {
        if (chatService == null) {
            synchronized (ChatService.class) {
                if (chatService == null) {
                    chatService = new ChatService();
                }
            }
        }
        return chatService;
    }

    public ChatUserDto newUser(ChatUserDto chatUserDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/new").build().encode().toUri();
        return restTemplate.postForObject(uri, chatUserDto, ChatUserDto.class);
    }

    public void updateUser(ChatUserDto chatUserDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/{userId}").build().encode().toUri();
        restTemplate.put(uri, chatUserDto);
    }

    public List<ChatUserDto> getFriendsList(String param) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + param + "/friends").build().encode().toUri();
        try {
            ChatUserDto[] users = restTemplate.getForObject(uri, ChatUserDto[].class);
            return asList(ofNullable(users).orElse(new ChatUserDto[0]));
        } catch (RestClientException e) {
            e.getMessage();
            return new ArrayList<>();
        }
    }

    public void addFriendToFriendsList(ChatUserDto chatUserDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + CURRENT_USER.getId() + "/friends")
                .queryParam("user2Id", chatUserDto.getId()).build().encode().toUri();
        try {
            restTemplate.put(uri, ChatUserDto[].class);
            LOGGER.info("" + chatUserDto.getName() + " was added to " + CURRENT_USER.getName() + " friends list.");
        }catch (Throwable e){
            e.getMessage();
        }
    }

    public ChatUserDto login(String mail, String password) {
        ChatUserDto user = ChatUserDto.builder().mail(mail).password(password).build();
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/login")
                .queryParam("mail", mail)
                .queryParam("password", password).build().encode().toUri();
        CURRENT_USER = restTemplate.postForObject(uri, user, ChatUserDto.class);
        return CURRENT_USER;
    }

    public List<ChatUserDto> searchUser(String name) {
        URI url = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + CURRENT_USER.getId() + "/search")
                .queryParam("name", name).build().encode().toUri();
        try {
            ChatUserDto[] table = restTemplate.getForObject(url, ChatUserDto[].class);
            return asList(table);
        } catch (Throwable e){
            LOGGER.info("There is no such user");
            e.getMessage();
            return new ArrayList<>();
        }
    }
}
