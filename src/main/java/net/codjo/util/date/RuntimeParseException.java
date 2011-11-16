package net.codjo.util.date;
import java.text.ParseException;
/**
 *
 */
public class RuntimeParseException extends RuntimeException {
    public RuntimeParseException(ParseException exp) {
        super(exp);
    }
}
