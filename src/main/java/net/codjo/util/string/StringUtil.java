package net.codjo.util.string;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Ensemble de méthodes de traitement sur les String.
 *
 * <p></p>
 *
 * @author $Author: marcona $
 * @version $Revision: 1.3 $
 */
public class StringUtil {
    /**
     * Bloque la creation d'instances de StringUtil
     */
    private StringUtil() {
    }


    /**
     * Enleve toutes les occurences d'un caractere d'une String.
     *
     * <p> Cette méthode permet, par exemple, d'effacer toutes les occurences du caractère espace d'une
     * String. </p>
     *
     * @param str   La String a épurer.
     * @param aChar Le caractère a effacer.
     *
     * @return La String épuré.
     */
    public static String removeAllCharOccurrence(String str, char aChar) {
        StringBuilder tmp = new StringBuilder(str);
        int index = 0;
        while (index < tmp.length()) {
            if (tmp.charAt(index) == aChar) {
                tmp.deleteCharAt(index);
            }
            else {
                index++;
            }
        }
        return tmp.toString();
    }


    public static String sqlToJavaName(String columnName) {
        columnName = columnName.toLowerCase();

        StringTokenizer tokenizer = new StringTokenizer(columnName, "_");
        StringBuilder buffer = new StringBuilder(tokenizer.nextToken());

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String firstChar = token.substring(0, 1).toUpperCase();
            buffer.append(firstChar).append(token.substring(1, token.length()));
        }

        return buffer.toString();
    }


    public static String javaToSqlName(String propertyName) {
        String sqlName = toSqlUpper(propertyName);
        if (sqlName.length() > 28) {
            throw new IllegalArgumentException("La colonne pour le champ " + propertyName
                                               + " dépasse 28 caractères !");
        }
        return sqlName;
    }


    public static boolean isNotNull(String string) {
        return !isNull(string);
    }


    public static boolean isNull(String string) {
        return string == null || "null".equals(string);
    }


    public static boolean isNullOrEmpty(String string) {
        return string == null || "".equals(string);
    }


    public static boolean isEmpty(String string) {
        return "null".equals(string) || "".equals(string);
    }


    public static List<String> findAll(String regex, String input) {
        List<String> occurenceList = new ArrayList<String>();
        Matcher nameMatcher = Pattern.compile(regex).matcher(input);
        while (nameMatcher.find()) {
            occurenceList.add(nameMatcher.group(1));
        }
        return occurenceList;
    }


    private static String toSqlUpper(String propertyName) {
        StringBuilder buffer = new StringBuilder();

        if (isAlreadyInSQLSyntax(propertyName)) {
            return propertyName;
        }

        for (int i = 0; i < propertyName.length(); i++) {
            if (Character.isUpperCase(propertyName.charAt(i))) {
                buffer.append('_');
            }
            buffer.append(propertyName.charAt(i));
        }

        return buffer.toString().toUpperCase();
    }


    private static boolean isAlreadyInSQLSyntax(String propertyName) {
        for (int i = 0; i < propertyName.length(); i++) {
            char currentChar = propertyName.charAt(i);
            if (!Character.isUpperCase(currentChar) && currentChar != '_'
                && !Character.isDigit(currentChar)) {
                return false;
            }
        }
        return true;
    }
}
