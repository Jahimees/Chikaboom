package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.Facade;
import net.chikaboom.model.database.BaseEntity;

/**
 * Предназначен для конвертации объектов из базы данных в объекты для отправки на клиент или наоборот
 *
 * @param <F> тип фасадного объекта для отправки на клиент
 * @param <M> тип объекта базы данных - модель
 */
public interface FacadeConverter<F extends Facade, M extends BaseEntity> {

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    F convertToDto(M model);

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    M convertToModel(F facade);
}
