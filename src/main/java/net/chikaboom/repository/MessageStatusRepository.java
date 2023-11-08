package net.chikaboom.repository;

import net.chikaboom.model.database.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageStatusRepository extends JpaRepository<MessageStatus, Integer> {

    Optional<MessageStatus> findByName(String name);
}
