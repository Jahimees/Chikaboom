package net.chikaboom.util.constant;

/**
 * Описание названий полей и сущностей, которые связаны с базой данных, но находятся непосредственно в коде.
 * Например поле в коде 'idServiceType' соответствует полю в БД: idservice_type.
 */
public final class EntityNamesConstant {

    private EntityNamesConstant() {
    }

    public static final String ID_SERVICE_SUBTYPE = "idServiceSubtype";
    public static final String SERVICE_SUBTYPE = "serviceSubtype";
    public static final String SERVICE_TYPE = "serviceType";
    public static final String ID_SERVICE_TYPE = "idServiceType";

}
