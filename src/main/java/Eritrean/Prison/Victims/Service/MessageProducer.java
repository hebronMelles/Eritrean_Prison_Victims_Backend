package Eritrean.Prison.Victims.Service;

import Eritrean.Prison.Victims.Entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;
    private final ChatMessageService chatMessageService;

    @Autowired
    public MessageProducer(KafkaTemplate<String, ChatMessage> kafkaTemplate, ChatMessageService chatMessageService) {
        this.kafkaTemplate = kafkaTemplate;
        this.chatMessageService = chatMessageService;
    }

    public void sendMessage(Object principal, ChatMessage chatMessage) {
        kafkaTemplate.send("user-messages", chatMessage.getRecipientId(), chatMessage);
        chatMessageService.saveMessage(principal, chatMessage);

    }
}

