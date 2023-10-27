package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы Comment
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Производит поиск всех комментариев, оставленных мастеру
     *
     * @param accountMaster аккаунт мастера, которому оставляли комментарии
     * @return коллекцию комментариев
     */
    List<Comment> findAllByAccountMaster(Account accountMaster);

    /**
     * Проверяет существование комментария по аккаунту клиента и аккаунту мастера
     *
     * @param accountClient аккаунт человека, оставившего комментарий
     * @param accountMaster аккаунт мастера, кому написали комментарий
     * @return true - если комментарий существует, false - в противном случае
     */
    boolean existsByAccountClientAndAccountMaster(Account accountClient, Account accountMaster);
}
