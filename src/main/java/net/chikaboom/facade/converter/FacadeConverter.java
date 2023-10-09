package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.Facade;
import net.chikaboom.model.database.BaseEntity;

public interface FacadeConverter<F extends Facade, M extends BaseEntity> {

    F convertToDto(M model);
    M convertToModel(F facade);
}
