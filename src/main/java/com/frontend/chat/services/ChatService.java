package com.frontend.chat.services;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.domain.MessageDto;
import com.frontend.chat.domain.MessageUpgrated;
import com.frontend.chat.domain.RolesDto;
import com.frontend.chat.mapper.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

public class ChatService {
    private final static String CHAT = "https://floating-sea-70446.herokuapp.com/v1";
    private RestTemplate restTemplate = new RestTemplate();
    private static ChatService chatService = null;
    public static ChatUserDto CURRENT_USER;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);
    private Mapper mapper = Mapper.getInstance();

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
        return mapper.mapStringDataOfUser(restTemplate.postForObject(uri, chatUserDto, ChatUserDto.class));
    }

    public void updateUser(ChatUserDto chatUserDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + CURRENT_USER.getId()).build().encode().toUri();
        restTemplate.put(uri, chatUserDto);
    }

    public List<ChatUserDto> getFriendsList(ChatUserDto chatUserDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + chatUserDto.getId() + "/friends").build().encode().toUri();
        try {
            ChatUserDto[] users = restTemplate.getForObject(uri, ChatUserDto[].class);
            LOGGER.info("Got friends from DB");
            return mapper.mapListOfChatUsers(asList(ofNullable(users).orElse(new ChatUserDto[0])));
        } catch (RestClientException e) {
            e.getMessage();
            LOGGER.info("Couldn't get friends from DB");
            return new ArrayList<>();
        }
    }

    public void addFriendToFriendsList(ChatUserDto chatUserDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + CURRENT_USER.getId() + "/friends")
                .queryParam("user2Id", chatUserDto.getId()).build().encode().toUri();
        try {
            restTemplate.put(uri, ChatUserDto[].class);
            LOGGER.info("" + chatUserDto.getName() + " was added to " + CURRENT_USER.getName() + " friends list.");
        } catch (Throwable e) {
            LOGGER.warn("Couldn't add friend");
            e.getMessage();
        }
    }

    public ChatUserDto login(String mail, String password) {
        ChatUserDto user = ChatUserDto.builder().mail(mail).password(password).build();
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/login")
                .queryParam("mail", mail)
                .queryParam("password", password).build().encode().toUri();
        return restTemplate.postForObject(uri, user, ChatUserDto.class);

    }

    public List<ChatUserDto> searchUser(String name) {
        URI url = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + CURRENT_USER.getId() + "/search")
                .queryParam("name", name).build().encode().toUri();
        try {
            ChatUserDto[] table = restTemplate.getForObject(url, ChatUserDto[].class);
            return mapper.mapListOfChatUsers(asList(table));
        } catch (Throwable e) {
            LOGGER.info("There is no such user");
            e.getMessage();
            return new ArrayList<>();
        }
    }

    public ChatUserDto findCurrentUser() {
        URI url = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + CURRENT_USER.getId()).build().encode().toUri();
        try {
            ChatUserDto user = restTemplate.getForObject(url, ChatUserDto.class);
            return mapper.mapStringDataOfUser(user);
        } catch (Throwable e) {
            LOGGER.info("There is no such user");
            e.getMessage();
            return new ChatUserDto();
        }
    }

    public List<MessageUpgrated> getConversation(ChatUserDto chatUserDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + CURRENT_USER.getId().toString() + "/" + chatUserDto.getId()).build().encode().toUri();
        try {
            return mapper.mapMessageToStringsList(asList(Objects.requireNonNull(restTemplate.getForObject(url, MessageDto[].class))),ChatService.CURRENT_USER);
        } catch (Throwable e) {
            LOGGER.warn("Couldn't get messages from: " + chatUserDto.toString());
            e.getMessage();
            return new ArrayList<>();
        }
    }

    public String sendMessage(String message, ChatUserDto to) {
        URI url = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + CURRENT_USER.getId() + "/" + to.getId())
                .queryParam("message", message).build().encode().toUri();
        return restTemplate.postForObject(url, message, String.class);
    }

    public void deleteFromFriendsList(ChatUserDto value) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + CURRENT_USER.getId() + "/deleteFriend")
                .queryParam("user2Id", value.getId()).build().encode().toUri();
        restTemplate.put(uri, ChatUserDto.class);
        LOGGER.info("User: " + value.toString() + " deleted from friends list");
    }

    @Scheduled(fixedDelay = 10800000)
    public void logout() {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/logout").build().encode().toUri();
        restTemplate.put(uri, CURRENT_USER);
    }

    public RolesDto getRole(ChatUserDto chatUserDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/role")
                .queryParam("userId", chatUserDto.getId()).build().encode().toUri();
        return restTemplate.getForObject(uri, RolesDto.class);
    }

    public void deleteMessage(MessageUpgrated clickedMessage) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/" + CURRENT_USER.getId())
                .queryParam("messageId", clickedMessage.getId()).build().encode().toUri();
        restTemplate.delete(uri);
    }

    public List<ChatUserDto> getAllUsers() {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/admin/allUsers").build().encode().toUri();
            return asList(Objects.requireNonNull(restTemplate.getForObject(uri, ChatUserDto[].class)));
    }

    public void setRoleAsAdmin(ChatUserDto chatUserDto, String role) {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/chat/admin")
                .queryParam("userId", chatUserDto.getId())
                .queryParam("role", role).build().encode().toUri();
        restTemplate.put(uri, chatUserDto);
    }
}
