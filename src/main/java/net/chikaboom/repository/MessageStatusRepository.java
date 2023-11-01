package net.chikaboom.repository;

import net.chikaboom.model.database.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageStatusRepository extends JpaRepository<MessageStatus, Integer> {
}
