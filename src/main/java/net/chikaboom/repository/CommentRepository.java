package net.chikaboom.repository;

import net.chikaboom.model.database.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Comment
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, String> {
}
