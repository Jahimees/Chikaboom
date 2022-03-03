package net.chikaboom.model;

import java.util.HashMap;

/**
 * Базовый объект для проекта.
 * <p>
 * Отличительной особенностью такого объекта является его расширяемость и гибкость.
 * <p>
 * Требуется доработка.
 */
public class ExpandableObject {

    /**
     * Коллекция "полей", которые содержит оюъект
     */
    private final HashMap<String, Object> fields;

    public ExpandableObject() {
        fields = new HashMap<>();
    }

    /**
     * Возвращает значение конкретного поля
     *
     * @param name имя поля
     * @return значение поля
     */
    public Object getField(String name) {
        return fields.get(name);
    }

    /**
     * Создает новое поле, либо устанавливает новое значение старому полю
     *
     * @param name  имя поля
     * @param value значение поля
     */
    public void setField(String name, Object value) {
        fields.put(name, value);
    }
}
