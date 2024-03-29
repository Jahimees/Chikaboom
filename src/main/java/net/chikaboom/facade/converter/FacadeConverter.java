package net.chikaboom.facade.converter;

/**
 * Интерфейс-маркер. Предназначен для обозначения классов для конвертации объектов из базы данных в объекты
 * для работы с ними. Конвертацию необходимо проводить в сервисах при получении объекта из базы или непосредственно
 * перед тем, как его сохранить туда. См. документацию про фасадные объекты: {@link net.chikaboom.facade.dto.Facade}
 */
public interface FacadeConverter {
}
