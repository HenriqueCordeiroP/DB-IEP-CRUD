package Utils;

public class Conversion {
	public static String parseStringOrNull(String value) {
        return value != null && !value.isEmpty() ? value : null;
    }
}
