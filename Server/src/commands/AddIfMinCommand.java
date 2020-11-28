package commands;

import database.Credentials;
import database.DatabaseController;
import models.Pen;
import managers.CollectionManager;
import managers.ConsoleManager;

public class AddIfMinCommand extends AbstractCommand {

    public AddIfMinCommand(){
        cmdName = "add_if_min";
        description = "добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getPen();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        Object login = databaseController.login(credentials);
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);
        Boolean res = collectionManager.addIfMin((Pen) inputData);

        if(res && credentials.username != null && credentials.password != null && login instanceof Credentials)
            consoleManager.writeln("Элемент был добавлен");
        else
            consoleManager.writeln("Элемент не был добавлен. Элемент оказался больше минимального");

        inputData = null;

        return null;
    }
}
