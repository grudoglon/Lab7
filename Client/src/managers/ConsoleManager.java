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


        Double width_of_kernel = readWithParseMinMax("Enter width_of_kernel (Double, от нуля до 2 ): ", new BigDecimal(0), new BigDecimal(2), false).doubleValue();

        Number pop = readWithParseMinMax("Enter amount (int, [1; 200]): ", new BigDecimal(1),  new BigDecimal(200), true);
        int amount = pop == null ? (int) 0L : pop.intValue();

        Double length_of_kernel = readWithParseMinMax("Enter length_of_kernel (Double):[8;16] ",  new BigDecimal(8) ,new BigDecimal(16),false ).doubleValue();
        Double weight = readWithParseMinMax("Enter weight (Double, [1;15]): ", new BigDecimal(1), new BigDecimal(15), false).doubleValue();
        while (true) {
            try {
                exists = parseBoolean(readWithMessage("It is exists? (true/false): ", false));
                break;
            }catch(NumberFormatException ex){
                writeln("True or false?");
                if(isScript) throw new ExecutionException("Cast error");
            }
        }




        return new Pen(-1, name, LocalDate.now(), width_of_kernel, amount, length_of_kernel, weight, exists);
    }




    public Pen getRemoveLowerPen(){
        return new Pen(-1, "somename", LocalDate.now(), readWithParseMinMax("Enter width_of_kernel (Double, от нуля до 2 ): ", new BigDecimal(0), new BigDecimal(2), false).doubleValue(), 1, 11d, 11, true );
    }



    public static boolean parseBoolean(String s) {
        if ("true".equals(s.toLowerCase())) {
            return true;
        } else if ("false".equals(s.toLowerCase())) {
            return false;
        }else{
            throw new NumberFormatException("Wrong format");
        }
    }



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
