package managers;

import database.Credentials;
import lombok.extern.log4j.Log4j2;
import models.Pen;



import exceptions.ExecutionException;
import exceptions.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import utils.NumUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Консольный менеджер
 */
@Slf4j
public class ConsoleManager {

    private Scanner scanner;
    private boolean isScript;
    private Writer writer;
    private Reader reader;

    public ConsoleManager(Reader reader, Writer writer, boolean isScript)
    {
        this.reader = reader;
        this.writer = writer;
        scanner = new Scanner(reader);
        this.isScript = isScript;
    }

    public void writeln(String message) {
        write(message + "\n");
    }

    public void write(String message) {
        try {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            System.out.println("Input error. {}");
        }
    }

    public boolean getIsScript(){ return isScript; }

    public String read() {
        return scanner.nextLine();
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public Credentials getCredentials(){
        String username = readWithMessage("Login: ", false);
        String password = readWithMessage("Password: ", false);

        return new Credentials(-1, username, password);
    }

    /**
     * получает введенные данные объектом
     * @return
     */
    public Pen getPen(){
        boolean exists = false;

        String name = readWithMessage("Enter pen name: ", false);


        Double width_of_kernel = readWithParseMinMax("Enter width_of_kernel (Double, от нуля и больше): ", new BigDecimal(0), NumUtil.DOUBLE_MAX, false).doubleValue();

        Number pop = readWithParseMinMax("Enter amount (int, [0; MAX_INTEGER]): ", new BigDecimal(0), NumUtil.INTEGER_MAX, true);
        int amount = pop == null ? (int) 0L : pop.intValue();

        int length_of_kernel = readWithParse("Enter length_of_kernel (int): ", false).intValue();
        Double weight = readWithParseMinMax("Enter weight (Double, [-13;15]): ", new BigDecimal(-13), new BigDecimal(15), false).doubleValue();
        while (true) {
            try {
                exists = parseBoolean(readWithMessage("It is exists? (true/false): ", false));
                break;
            }catch(NumberFormatException ex){
                writeln("True or false?");
                if(isScript) throw new ExecutionException("Cast error");
            }
        }




        return new Pen(-1L, name, LocalDate.now(), width_of_kernel, amount, length_of_kernel, weight, exists);
    }





    /**
     * парсит boolean значение из сотроки
     * @param s
     * @return
     */
    public static boolean parseBoolean(String s) {
        if ("true".equals(s.toLowerCase())) {
            return true;
        } else if ("false".equals(s.toLowerCase())) {
            return false;
        }else{
            throw new NumberFormatException("Wrong format");
        }
    }


    /**
     * выводит сообщение с вводом от пользователя
     * @param message
     * @param canNull
     * @return
     */
    public String readWithMessage(String message, boolean canNull) {
        String output = "";

        do {
            if (output == null) {
                writeln("Try again");
            }

            if(!isScript) {
                writeln(message);
            }

            output = scanner.nextLine();
            output = output.isEmpty() ? null : output;
        }while (!isScript && !canNull && output == null);
        if(isScript && output == null)
            throw new InvalidValueException("Not-null input");

        return output;
    }

    public Number readWithParse(String msg, boolean canNull){
        Number out = null;

        while (true){
            try{
                String num = readWithMessage(msg, canNull);
                if(num == null && canNull) break;

                NumberFormat format = NumberFormat.getInstance();
                ParsePosition pos = new ParsePosition(0);
                out = format.parse(num, pos);
                if (pos.getIndex() != num.length() || pos.getErrorIndex() != -1) throw new NumberFormatException("Неверный тип данных");

                break;
            }catch (NumberFormatException ex){
                writeln(ex.getMessage());
            }
        }

        return out;
    }

    public Number readWithParseMinMax(String msg, BigDecimal min, BigDecimal max, boolean canNull){
        Number out = null;

        do {
            out = readWithParse(msg, canNull);
            if(out == null && canNull)
                break;
        }while (!NumUtil.isInRange(out, min, max));

        return out;
    }
}
