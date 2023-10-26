package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByAccountMaster(Account accountMaster);

    boolean existsByAccountClientAndAccountMaster(Account accountClient, Account accountMaster);
}
