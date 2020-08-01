import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Color {

    private int hexInt255 = 0;
    private int hexInt31 = 0;

    private String hex255String = "";
    private String hex31String = "";

    private int[] rgb255 = new int[3];
    private int[] rgb31 = new int[3];

    public Color(int red, int green, int blue, String bit) {
        if (bit.equals("15"))
            convertFromRGB31(red, green, blue);
        else if (bit.equals("24"))
            convertFromRGB255(red, green, blue);
    }

    public Color(String hexString, String bit) {
        if (bit.equals("15"))
            convertFromHex31(hexString);
        else if (bit.equals("24"))
            convertFromHex255(hexString);
    }


    private void convertFromRGB31(int red, int green, int blue) {
        if (isValid15bit(red) && isValid15bit(green) && isValid15bit(blue)) {

            rgb255 = new int[]{
                    (int) Math.round(Double.parseDouble(String.valueOf(red)) * 255 / 31),
                    (int) Math.round(Double.parseDouble(String.valueOf(green)) * 255 / 31),
                    (int) Math.round(Double.parseDouble(String.valueOf(blue)) * 255 / 31)
            };

            rgb31 = new int[]{
                    red,
                    green,
                    blue
            };

            setHexFromRGB();
        }
    }

    private void convertFromRGB255(int red, int green, int blue) {
        if (isValid24bit(red) && isValid24bit(green) && isValid24bit(blue)) {

            rgb255 = new int[]{
                    red,
                    green,
                    blue
            };

            rgb31 = new int[]{
                    (int) Math.round(Double.parseDouble(String.valueOf(red)) * 31 / 255),
                    (int) Math.round(Double.parseDouble(String.valueOf(green)) * 31 / 255),
                    (int) Math.round(Double.parseDouble(String.valueOf(blue)) * 31 / 255)
            };

            setHexFromRGB();
        }
    }

    private void convertFromHex31(String hexString) {

        if (isValidHex(hexString, 4)) {

            int hex = LittleEndianToInt(hexString);

            double red, green, blue;

            red = (hex & 31);
            green = (hex >> 5) & 31;
            blue = (hex >> 10) & 31;

            rgb255 = new int[]{
                    (int) Math.round(red * 255 / 31),
                    (int) Math.round(green * 255 / 31),
                    (int) Math.round(blue * 255 / 31)
            };

            rgb31 = new int[]{
                    (int) Math.round(red),
                    (int) Math.round(green),
                    (int) Math.round(blue)
            };

            setHexFromRGB();
        }
    }

    private void convertFromHex255(String hexString) {
        if (isValidHex(hexString, 6)) {

            for (int i = 0, j = 0; i < hexString.length(); i += 2, j++)
                rgb255[j] = Integer.parseInt(hexString.substring(i, i + 2), 16);


            for (int i = 0; i < rgb255.length; i++)
                rgb31[i] = (int) Math.round(Double.parseDouble(String.valueOf(rgb255[i])) * 31 / 255);

            setHexFromRGB();
        }
    }


    private void setHexFromRGB() {

        for (int value : rgb255) {
            String byteString = Integer.toHexString(value);
            if (byteString.length() == 1)
                byteString = "0" + byteString;
            hex255String += byteString;
        }

        hexInt255 = Integer.parseInt(hex255String, 16);

        for (int i = 0; i < rgb255.length; i++)
            hexInt31 += (rgb31[i]*255/31) / 8 * Math.pow(32, i);

        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(hexInt31);

        byte[] ba = bb.array();

        hex31String = toHexString(ba).substring(0, 4);
    }

    private String toHexString(byte[] bytes) {
        char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                            'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v / 16];
            hexChars[j * 2 + 1] = hexArray[v % 16];
        }
        return new String(hexChars);
    }

    public static int LittleEndianToInt(final String hex) {
        int ret = 0;
        String hexLittleEndian = "";
        if (hex.length() % 2 != 0)
            return ret;
        for (int i = hex.length() - 2; i >= 0; i -= 2) {
            hexLittleEndian += hex.substring(i, i + 2);
        }
        return Integer.parseInt(hexLittleEndian, 16);
    }

    private boolean isValid15bit(int color) {
        return color >= 0 && color <= 31;
    }

    public boolean isValid24bit(int color) {
        return color >= 0 && color <= 255;
    }

    private boolean isValidHex(String hexString, int length) {
        // Regex to check valid hexadecimal color code.
        String regex = "^([A-Fa-f0-9]{" + length + "})$";
        Pattern p = Pattern.compile(regex);

        if (hexString == null)
            return false;

        Matcher m = p.matcher(hexString);
        return m.matches();
    }

    public String[] getRGB31() {
        String rgb[] = new String[3];

        for (int i = 0; i < rgb31.length; i++)
            rgb[i] = String.valueOf(rgb31[i]);

        return rgb;
    }

    public String[] getRGB255() {
        String rgb[] = new String[3];

        for (int i = 0; i < rgb255.length; i++)
            rgb[i] = String.valueOf(rgb255[i]);

        return rgb;
    }

    public String getHex255String() {
        return hex255String;
    }

    public String getHex31String() {
        return hex31String;
    }

    public int getHexInt255() {
        return hexInt255;
    }

    public int getHexInt31() {
        return hexInt31;
    }
}
