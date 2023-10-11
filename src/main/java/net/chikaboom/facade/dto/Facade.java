package net.chikaboom.facade.dto;

/**
 * Интерфейс для фасадных объектов - DTO.
 * <p>
 * Фасадные сущности предназначены для того, чтобы не работать с объектами
 * базы данных, которые помечены аннотацией {@link jakarta.persistence.Entity}, поскольку это небезопасно, ведь
 * в случае изменения объекта с аннотацией {@link jakarta.persistence.Entity} при открытой транзакции изменения
 * будут записаны в базу данных, а преобразование такого объекта в объект-фасад, позволяет производить безопасные
 * манипуляции с объектом. Для конвертации объектов, существуют конвертеры
 * {@link net.chikaboom.facade.converter.FacadeConverter}
 */
public interface Facade {
}