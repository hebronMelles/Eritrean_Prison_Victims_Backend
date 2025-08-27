package Eritrean.Prison.Victims.Repository;

import Eritrean.Prison.Victims.Entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
