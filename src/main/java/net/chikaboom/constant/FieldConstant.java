package net.chikaboom.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    //WORK TABLE
    public static final String ID_WORK = "idWork";

    public static final List<String> ACCOUNT_FIELDS = Arrays.asList(ID_ACCOUNT, NAME, SURNAME, LOGIN,
            PASSWORD, PHONE, REGISTRATION_DATE);
    public static final List<String> CLIENT_FIELDS = Arrays.asList(ID_CLIENT, ID_ACCOUNT);
    public static final List<String> COMMENT_FIELDS = Arrays.asList(ID_COMMENT, ID_CLIENT, MESSAGE,
            IS_CLIENT_MESSAGE, RATE);
    public static final List<String> MASTER_FIELDS = Arrays.asList(ID_MASTER, ID_ACCOUNT, ADDRESS);
    public static final List<String> SERVICE_FIELDS = Arrays.asList(ID_SERVICE,ID_MASTER,
            ID_SERVICE_TYPE, NAME, DESCRIPTION, COST);
    public static final List<String> SERVICE_TYPE_FIELDS = Arrays.asList(ID_SERVICE_TYPE, NAME);
    public static final List<String> WORK_FIELDS = Arrays.asList(ID_WORK, ID_MASTER, IMAGE, COMMENT);


}
