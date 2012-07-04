package net.codjo.util.network;
import java.util.regex.Pattern;
/**
 *
 */
public class IpValidator {

    private IpValidator() {
    }


    private static final String IPADDRESS_PATTERN =
          "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";


    public static boolean isValid(String ipAsString) {
        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        return pattern.matcher(ipAsString).matches();
    }
}
