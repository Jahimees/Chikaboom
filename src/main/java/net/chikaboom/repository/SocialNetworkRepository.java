package net.chikaboom.repository;

import net.chikaboom.model.database.SocialNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы SocialNetwork
 */
@Repository
public interface SocialNetworkRepository extends JpaRepository<SocialNetwork, Integer> {
}
