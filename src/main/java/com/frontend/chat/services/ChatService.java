package com.frontend.chat.services;

import com.frontend.chat.domain.ChatUserDto;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

public class ChatService {
    private final static String CHAT = "http://localhost:8083";
    private RestTemplate restTemplate = new RestTemplate();
    private static ChatService chatService = null;

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
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/v1/chat/new").build().encode().toUri();
        return restTemplate.postForObject(uri, chatUserDto, ChatUserDto.class);
    }

    public void updateUser(ChatUserDto chatUserDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/v1/chat/{userId}").build().encode().toUri();
        restTemplate.put(uri, chatUserDto);
    }

    public List<ChatUserDto> getFriendsList(String param) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/v1/chat/" + param + "/friends").build().encode().toUri();
        try {
            ChatUserDto[] users = restTemplate.getForObject(uri, ChatUserDto[].class);
            return asList(ofNullable(users).orElse(new ChatUserDto[0]));
        } catch (RestClientException e) {
            e.getMessage();
            return new ArrayList<>();
        }
    }

    public List<ChatUserDto> addFriendToFriendsList(Long user2Id) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/v1/chat/{userId}/friends")
                .queryParam("user2Id", user2Id).build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(asList(MediaType.APPLICATION_JSON));
        HttpEntity<Long> entity = new HttpEntity<Long>(user2Id, headers);

        return (List<ChatUserDto>) restTemplate.exchange(uri, HttpMethod.PUT, entity, Long.class);
    }

    public String getCurrentUsersmail() {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/v1/currentUser").build().encode().toUri();
        return restTemplate.getForObject(uri, String.class);
    }

    public ChatUserDto getCurrentUser(String mail) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/v1/chat/search")
                .queryParam("mail", mail).build().encode().toUri();
        return restTemplate.getForObject(uri, ChatUserDto.class);
    }

    public Long getUsersId() {
        String mail = getCurrentUsersmail();
        ChatUserDto user = getCurrentUser(mail);
        return user.getId();
    }

    public void login(String mail, String password) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/login").build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("username",mail);

        map.add("password",password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity( uri, request , String.class );
    }
}
