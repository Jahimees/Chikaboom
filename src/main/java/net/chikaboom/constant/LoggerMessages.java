package net.chikaboom.constant;

//Переименуй LoggerMessages в LoggerMessageConstant.
//        Причины:
//        Класс - название в единственном числе
//        В названии должно быть главное-существительное (в данном случае оно было, но Constant является более определяющим что ли)
public class LoggerMessages {

    //C ILLEGAL_COMMAND не совсем корректно. "Недопустимый" слишком абстрактное названия для описания "Существует".
    // + не совсем понял почему Command exists. Если команда существует, то это же хорошо?
    // Я так понимаю задумка в том, что если приходит неизвестная команда,
    // то вылетает сообщение с ошибкой типа "Command is not exists".
    // Соответственно и название переменной должно быть COMMAND_IS_NOT_EXISTS.
    // Даже если оно будет таким длинным, то ничего страшного.
    public static String ILLEGAL_COMMAND = "Command exists!";
    //Слишком неразвернутое название константы особенно с учетом названия класса "LoggerMessages".
    public static String GOT = " command got.";   //Лучше назвать COMMAND_GOT.
}

//Если ты пишешь в верхнем регистре, подразуемевается, что это константы. Соответственно нужно добавить final
