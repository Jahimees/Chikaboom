package net.chikaboom.constant;

public class FieldConstant {
    //ACCOUNT TABLE
    public static final String ID_ACCOUNT = "idAccount";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";
    public static final String REGISTRATION_DATE = "registrationDate";

    //CLIENT TABLE
    public static final String ID_CLIENT = "idClient";
    //+idAccount

    //COMMENT TABLE
    public static final String ID_COMMENT = "idComment";
    public static final String MESSAGE = "message";
    public static final String IS_CLIENT_MESSAGE = "isClientMessage";
    public static final String RATE = "rate";
    //+idMaster
    //+idClient

    //MASTER TABLE
    public static final String ID_MASTER = "idMaster";
    public static final String ADDRESS = "address";
    public static final String ABOUT = "about";
    //+idAccount

    //SERVICE TABLE
    public static final String ID_SERVICE = "idService";
    public static final String DESCRIPTION = "description";
    public static final String COST = "cost";
    //+idMaster
    //+idServiceType
    //+name

    //SERVICE TYPE TABLE
    public static final String ID_SERVICE_TYPE = "idServiceType";
    public static final String TYPE_NAME = "typeName";
    public static final String IMAGE = "image";
    public static final String COMMENT = "comment";

}
