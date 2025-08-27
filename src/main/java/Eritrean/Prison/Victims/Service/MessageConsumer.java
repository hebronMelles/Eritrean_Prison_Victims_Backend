package Eritrean.Prison.Victims.Service;

import Eritrean.Prison.Victims.Entity.ChatMessage;
import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Repository.ChatMessageRepository;
import Eritrean.Prison.Victims.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {


    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageConsumer(
            ChatMessageRepository chatMessageRepository,
            UserRepository userRepository) {

        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "user-messages", groupId = "admas-chat")
    public void consume(ChatMessage message) {
        // Assuming message.getRecipientId() is email
        User recipient = userRepository.findById(message.getRecipientId()).orElse(null);
        if (recipient != null) {
            message.setUser(recipient); // Assign user directly
            chatMessageRepository.save(message); // Save message
        }
    }

}

