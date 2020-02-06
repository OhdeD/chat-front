package com.frontend.chat.services;

import com.frontend.chat.domain.ChatUserDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

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


    public ChatUserDto newUser(ChatUserDto chatUserDto) throws JSONException {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/v1/chat/new").build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        Gson gson = new Gson();
//        String userJson = gson.toJson(chatUserDto);

        JSONObject userJson = new JSONObject();
        userJson.put("name", chatUserDto.getName());
        userJson.put("surname", chatUserDto.getSurname());
        userJson.put("mail", chatUserDto.getMail());
        userJson.put("password", chatUserDto.getPassword());
        userJson.put("city", chatUserDto.getCity());

        HttpEntity<String> request = new HttpEntity<String>(userJson.toString(), headers);

        return restTemplate.postForObject(uri, request, ChatUserDto.class);
    }

    public void updateUser(ChatUserDto chatUserDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/v1/chat/{userId}").build().encode().toUri();
        restTemplate.put(uri, chatUserDto);
    }

    public List<ChatUserDto> getFriendsList(String param) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/v1/chat/" + param + "/friends").build().encode().toUri();
        try{
            ChatUserDto[] chatUserDtos = restTemplate.getForObject(uri, ChatUserDto[].class);
            return asList(chatUserDtos);
        }catch (RestClientException e ){
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

    public void login() {
    URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/login").build().encode().toUri();

    }
}
