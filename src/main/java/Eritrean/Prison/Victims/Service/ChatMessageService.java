package Eritrean.Prison.Victims.Service;

import Eritrean.Prison.Victims.Entity.ChatMessage;
import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Repository.ChatMessageRepository;
import Eritrean.Prison.Victims.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserService userService, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public ChatMessage saveMessage(Object principal, ChatMessage message) {
        User user = userService.getCurrentUser(principal);
        message.setTimestamp(LocalDateTime.now());
        message.setUser(user);
        chatMessageRepository.save(message);
        return message;
    }

    public List<ChatMessage> getAllMessages(Object principal) {
        User user = userService.getCurrentUser(principal);
        return user.getChatMessageList();
    }

    public ChatMessage getMessageById(Long id, Object principal) {
        User user = userService.getCurrentUser(principal);
        ChatMessage message = chatMessageRepository.findById(id)
                .filter(msg -> msg.getUser().getId().equals(user.getId()))
                .orElse(null);
        return message;
    }

    public void deleteChatMessage(Object principal, Long id) {
        User user = userService.getCurrentUser(principal);
        ChatMessage message = chatMessageRepository.findById(id)
                .filter(msg -> msg.getUser().getId().equals(user.getId()))
                .orElse(null);
        chatMessageRepository.delete(message);
        user.getChatMessageList().remove(message);
        userRepository.save(user);

    }

}
