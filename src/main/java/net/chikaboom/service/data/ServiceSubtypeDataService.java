package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.ServiceSubtype;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ServiceSubtypeDataService implements DataService<ServiceSubtype> {

//    TODO in progress
    @Override
    public Optional<ServiceSubtype> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<ServiceSubtype> findAll() {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public ServiceSubtype update(ServiceSubtype o) {
        return null;
    }

    @Override
    public ServiceSubtype create(ServiceSubtype account) {
        return null;
    }
}
