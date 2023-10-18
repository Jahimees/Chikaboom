package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Integer> {

    List<UserFile> findAllByAccount(Account account);
}
