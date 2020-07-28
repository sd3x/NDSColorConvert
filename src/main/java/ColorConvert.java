import javax.swing.*;
import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorConvert {

    static boolean sliderLock = false;

    static JTextField red255 = new JTextField();
    static JTextField green255 = new JTextField();
    static JTextField blue255 = new JTextField();

    static JTextField red31 = new JTextField();
    static JTextField green31 = new JTextField();
    static JTextField blue31 = new JTextField();

    static JSlider redSlider = new JSlider(SwingConstants.VERTICAL, 0, 31, 0);
    static JSlider greenSlider = new JSlider(SwingConstants.VERTICAL, 0, 31, 0);
    static JSlider blueSlider = new JSlider(SwingConstants.VERTICAL, 0, 31, 0);

    static JTextField rgbHex = new JTextField();
    static JTextField ndsHex = new JTextField();

    static JButton color = new JButton();


    public static void main(String[] args) {

        int windowWidth = 500;
        int windowHeight = 400;

        JFrame window = new JFrame();
        window.setSize(windowWidth,windowHeight);
        window.setLayout(null);//using no layout managers
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);



        int buttonWidth = windowWidth/5;
        int buttonHeight = windowHeight/20;

        int goX = (windowWidth - buttonWidth)/2;
        int goY = windowHeight - 100;


        int textsWidth = 75;
        int textsHeight = 20;

        int textsX = (goX - textsWidth) / 2;
        int rgbHexY = goY - 50;
        int rgbLabelY = rgbHexY - 25;

        int ndsHexY = rgbHexY - 50;
        int ndsLabelY = ndsHexY - 25 - textsHeight;

        int sliderWidth = 20;
        int sliderHeight = windowHeight/2;
        int sliderX = windowWidth - 40;
        int sliderY = (windowHeight - rgbHexY)/2;

        redSlider.setBounds(sliderX - 80, sliderY, sliderWidth, sliderHeight);
        greenSlider.setBounds(sliderX - 40, sliderY, sliderWidth, sliderHeight);
        blueSlider.setBounds(sliderX, sliderY, sliderWidth, sliderHeight);


        rgbHex.setBounds(textsX, rgbHexY, textsWidth, textsHeight);
        ndsHex.setBounds(textsX, ndsHexY, textsWidth, textsHeight);

        JLabel rgbHexLabel = new JLabel("24 bit RGB hex");
        rgbHexLabel.setBounds(textsX, rgbLabelY, textsWidth + 50, textsHeight);

        JLabel ndsHexLabel = new JLabel("<html>15 bit NDS/GBA hex<br/>(Little Endian)</html>");

        ndsHexLabel.setBounds(textsX, ndsLabelY, textsWidth + 100, 2*textsHeight);

        int two55width = 35;

        JLabel rgb255label = new JLabel("24 bit color R,G,B");
        rgb255label.setBounds(goX + 20, rgbLabelY, textsWidth + 50, textsHeight);

        JLabel rgb31label = new JLabel("15 bit color R,G,B");
        rgb31label.setBounds(goX + 20, ndsLabelY + 10, textsWidth + 50, textsHeight);

        red255.setBounds(goX + 20, rgbHexY, two55width, textsHeight);
        green255.setBounds(goX + 60, rgbHexY, two55width, textsHeight);
        blue255.setBounds(goX + 100, rgbHexY, two55width, textsHeight);

        red31.setBounds(goX + 20, ndsHexY, two55width, textsHeight);
        green31.setBounds(goX + 60, ndsHexY, two55width, textsHeight);
        blue31.setBounds(goX + 100, ndsHexY, two55width, textsHeight);




        int radioDimension = 20;

        JRadioButton hex255 = new JRadioButton();
        JRadioButton rgb255 = new JRadioButton();
        JRadioButton hex31 = new JRadioButton();
        JRadioButton rgb31 = new JRadioButton();

        ButtonGroup bg = new ButtonGroup();
        bg.add(hex255); bg.add(rgb255); bg.add(hex31); bg.add(rgb31);

        hex255.addActionListener(actionEvent -> {
            if(hex255.isSelected()) {
                rgbHex.setEditable(true);

                red255.setEditable(false);
                green255.setEditable(false);
                blue255.setEditable(false);

                ndsHex.setEditable(false);

                red31.setEditable(false);
                green31.setEditable(false);
                blue31.setEditable(false);
            }
        });

        hex31.addActionListener(actionEvent -> {
            rgbHex.setEditable(false);

            red255.setEditable(false);
            green255.setEditable(false);
            blue255.setEditable(false);

            ndsHex.setEditable(true);

            red31.setEditable(false);
            green31.setEditable(false);
            blue31.setEditable(false);

        });

        rgb255.addActionListener(actionEvent -> {
            if(rgb255.isSelected()) {
                rgbHex.setEditable(false);

                red255.setEditable(true);
                green255.setEditable(true);
                blue255.setEditable(true);

                ndsHex.setEditable(false);

                red31.setEditable(false);
                green31.setEditable(false);
                blue31.setEditable(false);
            }
        });

        rgb31.addActionListener(actionEvent -> {
            rgbHex.setEditable(false);

            red255.setEditable(false);
            green255.setEditable(false);
            blue255.setEditable(false);

            ndsHex.setEditable(false);

            red31.setEditable(true);
            green31.setEditable(true);
            blue31.setEditable(true);
        });

        hex255.setBounds(textsX - 20, rgbLabelY, radioDimension, radioDimension);
        hex255.setSelected(true);
        ndsHex.setEditable(false);
        red255.setEditable(false); green255.setEditable(false); blue255.setEditable(false);
        red31.setEditable(false); green31.setEditable(false); blue31.setEditable(false);
        rgb255.setBounds(goX, rgbLabelY, radioDimension, radioDimension);
        hex31.setBounds(textsX - 20, ndsLabelY + 10, radioDimension, radioDimension);
        rgb31.setBounds(goX, ndsLabelY + 10, radioDimension, radioDimension);

        color.setBounds(textsX + 80, ndsLabelY - 120, 100, 100);
        color.setBackground(Color.white);
        color.setEnabled(false);




        JButton goButton = new JButton("Convert");
        goButton.setBounds(goX, goY, buttonWidth, buttonHeight);
        goButton.addActionListener(actionEvent -> {
            if(hex255.isSelected())
                updateFromHex255();
            if(rgb255.isSelected())
                updateFrom255();
            if(rgb31.isSelected())
                updateFrom31();
            if(hex31.isSelected())
                updateFromNdsHex();

            updateColor();
            updateSliders();
            sliderLock = false;

        });

        redSlider.addChangeListener(changeEvent -> {
            int redLevel = redSlider.getValue();
            red31.setText(String.valueOf(redLevel));

            if(!sliderLock)
                updateFrom31();

            updateColor();
        });

        greenSlider.addChangeListener(changeEvent -> {
            int greenLevel = greenSlider.getValue();
            green31.setText(String.valueOf(greenLevel));

            if(!sliderLock)
                updateFrom31();

            updateColor();
        });

        blueSlider.addChangeListener(changeEvent -> {
            int blueLevel = blueSlider.getValue();
            blue31.setText(String.valueOf(blueLevel));
            if(!sliderLock)
                updateFrom31();

            sliderLock = false;
            updateColor();
        });


        window.add(color);
        window.add(goButton);
        window.add(rgbHex);
        window.add(ndsHex);
        window.add(rgbHexLabel);
        window.add(ndsHexLabel);
        window.add(rgb255label);
        window.add(rgb31label);
        window.add(red255); window.add(green255); window.add(blue255);
        window.add(red31); window.add(green31); window.add(blue31);
        window.add(redSlider); window.add(greenSlider); window.add(blueSlider);

        window.add(hex255); window.add(rgb255);
        window.add(hex31); window.add(rgb31);

        updateSliders();
        updateFrom31();
        updateColor();

        window.setVisible(true);//making the frame visible
    }

    public static void update255(String hexString) {
        if(isValidHex(hexString)) {
            //System.out.println("valid hex");
            int[] two55s = hexTo255(hexString);
            red255.setText(String.valueOf(two55s[0]));
            green255.setText(String.valueOf(two55s[1]));
            blue255.setText(String.valueOf(two55s[2]));
        }
    }

    public static boolean isValidHex(String hexString) {
        // Regex to check valid hexadecimal color code.
        String regex = "^([A-Fa-f0-9]{6})$";
        Pattern p = Pattern.compile(regex);

        if (hexString == null)
            return false;

        Matcher m = p.matcher(hexString);
        return m.matches();
    }

    public static boolean isValid24bit(String color) {

        if(color == null || color.isEmpty())
            return false;
        int colorInt = Integer.parseInt(color);
        return colorInt >= 0 && colorInt <= 255;
    }

    public static boolean isValid15bit(String color) {

        if(color == null || color.isEmpty())
            return false;
        int colorInt = Integer.parseInt(color);
        return colorInt >= 0 && colorInt <= 31;
    }

    public static int[] hexTo255(String hexString) {
        int[] two55s = new int[3];
        for(int i = 0, j = 0; i < hexString.length(); i+=2, j++)
            two55s[j] = Integer.parseInt(hexString.substring(i, i+2), 16);
        return two55s;
    }

    public static void update31(int[] colors) {
        red31.setText(String.valueOf(colors[0]));
        green31.setText(String.valueOf(colors[1]));
        blue31.setText(String.valueOf(colors[2]));
    }

    public static int[] two55to31() {
        if(isValid24bit(red255.getText()) && isValid24bit(green255.getText()) && isValid24bit(blue255.getText())) {
            return new int[]{(int) Math.round(Double.parseDouble(red255.getText()) * 31/255), (int) Math.round(Double.parseDouble(green255.getText()) * 31/255),
                    (int) Math.round(Double.parseDouble(blue255.getText()) * 31/255)};
        }
        return null;
    }

    public static int[] thirty1to255() {
        if(isValid15bit(red31.getText()) && isValid24bit(green31.getText()) && isValid24bit(blue31.getText())) {
            return new int[]{(int) Math.round(Double.parseDouble(red31.getText()) * 255/31), (int) Math.round(Double.parseDouble(green31.getText()) * 255/31),
                    (int) Math.round(Double.parseDouble(blue31.getText()) * 255/31)};
        }
        return null;
    }

    public static void update255Hex(String red, String green, String blue) {

        //System.out.println("writing to hex255");

        if(isValid24bit(red) && isValid24bit(green) && isValid24bit(blue)) {
            red = Integer.toHexString(Integer.parseInt(red));
            green = Integer.toHexString(Integer.parseInt(green));
            blue = Integer.toHexString(Integer.parseInt(blue));

            if(red.length() == 1)
                red = "0" + red;
            if(green.length() == 1)
                green = "0" + green;
            if(blue.length() == 1)
                blue = "0" + blue;

            String numberAsString = red + green + blue;

            rgbHex.setText(numberAsString);
        }
    }

    public static void updateNdsHex(String hex255String) {
        if(isValidHex(hex255String)) {

            int[] hex255bytes = hexTo255(hex255String);

            int color = (hex255bytes[2]/8*1024 + hex255bytes[1]/8*32 + hex255bytes[0]/8);

            // System.out.println(color);

            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putInt(color);

            byte[] ba = bb.array();

            ndsHex.setText(toHexString(ba).substring(0, 4));

        }
    }

    public static String toHexString(byte[] bytes) {
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v/16];
            hexChars[j*2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }

    public static int LEtoInt(final String hex) {
        int ret = 0;
        String hexLittleEndian = "";
        if (hex.length() % 2 != 0)
            return ret;
        for (int i = hex.length() - 2; i >= 0; i -= 2) {
            hexLittleEndian += hex.substring(i, i + 2);
        }
        return Integer.parseInt(hexLittleEndian, 16);
    }

    public static int[] ndsHexTo255(String LEHex) {

        int color31 = LEtoInt(LEHex);

        double red, green, blue;

        red = (color31 & 31);
        green = (color31 >> 5) & 31;
        blue = (color31 >> 10) & 31;

        return new int[]{(int) Math.round(red * 255 / 31),(int) Math.round(green * 255/31), (int) Math.round(blue * 255/31)};

    }

    public static void updateFromHex255() {
        sliderLock = true;
        update255(rgbHex.getText());
        update31(two55to31());
        updateNdsHex(rgbHex.getText());
    }

    public static void updateFrom255() {
        sliderLock = true;
        update255Hex(red255.getText(), green255.getText(), blue255.getText());
        update31(two55to31());
        updateNdsHex(rgbHex.getText());
    }

    public static void updateFrom31() {
        System.out.println("updating from 31");
        int[] colors = thirty1to255();
        update255Hex(String.valueOf(colors[0]), String.valueOf(colors[1]), String.valueOf(colors[2]));
        update255(rgbHex.getText());
        updateNdsHex(rgbHex.getText());
    }

    public static void updateFromNdsHex() {
        int[] colors255 = ndsHexTo255(ndsHex.getText());
        update255Hex(String.valueOf(colors255[0]), String.valueOf(colors255[1]), String.valueOf(colors255[2]));
        update255(rgbHex.getText());
        update31(two55to31());
    }

    public static void updateColor() {
        if(isValidHex(rgbHex.getText())) {
            //System.out.println("valid hex");
            int[] two55s = thirty1to255();
            Color c = new Color(two55s[0], two55s[1], two55s[2]);
            color.setBackground(c);
        }
    }

    public static void updateSliders() {
        int redLevel;
        int greenLevel;
        int blueLevel;

        if(red31.getText().isEmpty() || red31.getText() == null)
        { redLevel = 0; red31.setText("0"); }
        else
            redLevel = Integer.parseInt(red31.getText());

        if(green31.getText().isEmpty() || green31.getText() == null)
        { greenLevel = 0; green31.setText("0"); }
        else
            greenLevel = Integer.parseInt(green31.getText());

        if(blue31.getText().isEmpty() || blue31.getText() == null)
        { blueLevel = 0; blue31.setText("0"); }
        else
            blueLevel = Integer.parseInt(blue31.getText());

        redSlider.setValue(redLevel);
        greenSlider.setValue(greenLevel);
        blueSlider.setValue(blueLevel);

    }
}
