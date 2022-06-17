package net.chikaboom.repository;

import net.chikaboom.model.database.SocialNetwork;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы SocialNetwork
 */
@Repository
public interface SocialNetworkRepository extends CrudRepository<SocialNetwork, Integer> {
}
