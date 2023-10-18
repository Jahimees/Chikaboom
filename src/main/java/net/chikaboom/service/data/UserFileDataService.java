package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.UserFile;
import net.chikaboom.repository.UserFileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFileDataService implements DataService<UserFile> {

    private final UserFileRepository userFileRepository;

    @Override
    public Optional<UserFile> findById(int id) {
        return userFileRepository.findById(id);
    }

    @Override
    public List<UserFile> findAll() {
        return userFileRepository.findAll();
    }

    public List<UserFile> findAllByAccount(Account account) {
        return userFileRepository.findAllByAccount(account);
    }

    @Override
    public void deleteById(int id) {
        userFileRepository.deleteById(id);
    }

    @Override
    public UserFile update(UserFile userFile) {
        return userFileRepository.saveAndFlush(userFile);
    }

    @Override
    public UserFile create(UserFile userFile) {
        return userFileRepository.saveAndFlush(userFile);
    }
}
