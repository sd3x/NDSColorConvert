import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;

public class ColorConvert {

    static boolean sliderLock = false;

    static Color color = new Color("19317b", "24");

    static JFrame window = new JFrame();

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

    static JButton colorPreview = new JButton();

    static JRadioButton hex255select = new JRadioButton();
    static JRadioButton rgb255select = new JRadioButton();
    static JRadioButton hex31select = new JRadioButton();
    static JRadioButton rgb31select = new JRadioButton();

    static JButton goButton = new JButton("Convert");

    public static void main(String[] args) {

        initSwingUI();
        updateUI();

        ButtonGroup selectGroup = new ButtonGroup();
        selectGroup.add(hex255select); selectGroup.add(rgb255select); selectGroup.add(hex31select); selectGroup.add(rgb31select);

        ActionListener determineSelectedField = actionEvent -> {

            boolean rgbHexIsEditable = false;
            boolean two55sAreEditable = false;
            boolean ndsHexIsEditable = false;
            boolean thirty1sAreEditable = false;

            if(hex255select.isSelected())
                rgbHexIsEditable = true;
            else if (rgb255select.isSelected())
                two55sAreEditable = true;
            else if (hex31select.isSelected())
                ndsHexIsEditable = true;
            else if (rgb31select.isSelected())
                thirty1sAreEditable = true;

            rgbHex.setEditable(rgbHexIsEditable);

            red255.setEditable(two55sAreEditable);
            green255.setEditable(two55sAreEditable);
            blue255.setEditable(two55sAreEditable);

            ndsHex.setEditable(ndsHexIsEditable);

            red31.setEditable(thirty1sAreEditable);
            green31.setEditable(thirty1sAreEditable);
            blue31.setEditable(thirty1sAreEditable);

        };

        hex255select.addActionListener(determineSelectedField);
        rgb255select.addActionListener(determineSelectedField);
        hex31select.addActionListener(determineSelectedField);
        rgb31select.addActionListener(determineSelectedField);

        goButton.addActionListener(actionEvent -> {

            if(hex255select.isSelected())
                color = new Color(rgbHex.getText(), "24");
            if(hex31select.isSelected())
                color = new Color(ndsHex.getText(), "15");

            if(rgb255select.isSelected())
                color = new Color(Integer.parseInt(red255.getText()),
                                  Integer.parseInt(green255.getText()),
                                  Integer.parseInt(blue255.getText()), "24");

            if(rgb31select.isSelected())
                color = new Color(Integer.parseInt(red31.getText()),
                        Integer.parseInt(green31.getText()),
                        Integer.parseInt(blue31.getText()), "15");

            sliderLock = true;

            updateUI();
            updateSliders();

            sliderLock = false;

        });

        ChangeListener sliderChange = changeEvent -> {
            int redLevel = redSlider.getValue();
            int blueLevel = blueSlider.getValue();
            int greenLevel = greenSlider.getValue();

            if(!sliderLock) {
                color = new Color(redLevel, greenLevel, blueLevel, "15");
                updateUI();
            }

        };

        redSlider.addChangeListener(sliderChange);
        greenSlider.addChangeListener(sliderChange);
        blueSlider.addChangeListener(sliderChange);
    }

    public static void updateUI() {
        rgbHex.setText(color.getHex255String());
        ndsHex.setText(color.getHex31String());

        String rgb31[] = color.getRGB31();
        red31.setText(rgb31[0]);
        green31.setText(rgb31[1]);
        blue31.setText(rgb31[2]);

        String rgb255[] = color.getRGB255();
        red255.setText(rgb255[0]);
        green255.setText(rgb255[1]);
        blue255.setText(rgb255[2]);

        updateSliders();

        colorPreview.setBackground(new java.awt.Color(color.getHexInt255()));

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

    public static void initSwingUI() {

        int windowWidth = 500;
        int windowHeight = 400;

        window.setSize(windowWidth,windowHeight);
        window.setLayout(null);
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
        hex255select.setBounds(textsX - 20, rgbLabelY, radioDimension, radioDimension);
        hex255select.setSelected(true);
        ndsHex.setEditable(false);
        red255.setEditable(false); green255.setEditable(false); blue255.setEditable(false);
        red31.setEditable(false); green31.setEditable(false); blue31.setEditable(false);
        rgb255select.setBounds(goX, rgbLabelY, radioDimension, radioDimension);
        hex31select.setBounds(textsX - 20, ndsLabelY + 10, radioDimension, radioDimension);
        rgb31select.setBounds(goX, ndsLabelY + 10, radioDimension, radioDimension);

        colorPreview.setBounds(textsX + 80, ndsLabelY - 120, 100, 100);
        colorPreview.setBackground(java.awt.Color.white);
        colorPreview.setEnabled(false);

        goButton.setBounds(goX, goY, buttonWidth, buttonHeight);

        window.add(colorPreview);
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

        window.add(hex255select); window.add(rgb255select);
        window.add(hex31select); window.add(rgb31select);

        window.setVisible(true);

    }

}
