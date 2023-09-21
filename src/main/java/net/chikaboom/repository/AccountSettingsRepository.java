package net.chikaboom.repository;

import net.chikaboom.model.database.AccountSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountSettingsRepository extends JpaRepository<AccountSettings, Integer> {

}