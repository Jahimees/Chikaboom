package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.ServiceType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ServiceTypeDataService implements DataService<ServiceType> {
//    TODO in progress
    @Override
    public Optional<ServiceType> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<ServiceType> findAll() {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public ServiceType update(ServiceType o) {
        return null;
    }

    @Override
    public ServiceType create(ServiceType account) {
        return null;
    }
}
