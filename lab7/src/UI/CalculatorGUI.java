package src.UI;

import src.ErrorHandling.ErrorHandler.InterpreterException;
import src.ParserDir.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorGUI extends Frame implements ActionListener {
    private TextField display;
    private Button[] numButtons;
    private Button addButton, subButton, mulButton, divButton, equButton, clrButton, powButton;
    private Button sinButton, cosButton, tanButton, ctgButton, openParenButton, closeParenButton;

    private Parser parser;

    public CalculatorGUI() {
        // Frame settings
        setLayout(new BorderLayout());
        setSize(400, 600);
        setTitle("Calculator");

        // Initialize display
        display = new TextField();
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        // Initialize number buttons
        numButtons = new Button[10];
        for (int i = 0; i < 10; i++) {
            numButtons[i] = new Button(String.valueOf(i));
            numButtons[i].addActionListener(this); // Add ActionListener to each number button
        }

        // Initialize operator buttons
        addButton = new Button("+");
        subButton = new Button("-");
        mulButton = new Button("*");
        divButton = new Button("/");
        equButton = new Button("=");
        clrButton = new Button("C");
        powButton = new Button("^");

        addButton.addActionListener(this);
        subButton.addActionListener(this);
        mulButton.addActionListener(this);
        divButton.addActionListener(this);
        equButton.addActionListener(this);
        clrButton.addActionListener(this);
        powButton.addActionListener(this);

        // Initialize trigonometric function buttons
        sinButton = new Button("Sin");
        cosButton = new Button("Cos");
        tanButton = new Button("Tan");
        ctgButton = new Button("Ctg");

        sinButton.addActionListener(this);
        cosButton.addActionListener(this);
        tanButton.addActionListener(this);
        ctgButton.addActionListener(this);

        // Initialize parentheses buttons
        openParenButton = new Button("(");
        closeParenButton = new Button(")");

        openParenButton.addActionListener(this);
        closeParenButton.addActionListener(this);

        // Layout for buttons
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(6, 4)); // Adjusted grid layout to accommodate more buttons

        // Add buttons to the panel
        panel.add(numButtons[1]);
        panel.add(numButtons[2]);
        panel.add(numButtons[3]);
        panel.add(addButton);
        panel.add(numButtons[4]);
        panel.add(numButtons[5]);
        panel.add(numButtons[6]);
        panel.add(subButton);
        panel.add(numButtons[7]);
        panel.add(numButtons[8]);
        panel.add(numButtons[9]);
        panel.add(mulButton);
        panel.add(clrButton);
        panel.add(numButtons[0]);
        panel.add(equButton);
        panel.add(divButton);

        // Add trigonometric function buttons
        panel.add(sinButton);
        panel.add(cosButton);
        panel.add(tanButton);
        panel.add(ctgButton);

        // Add parentheses buttons
        panel.add(openParenButton);
        panel.add(closeParenButton);
        
        panel.add(powButton);

        add(panel, BorderLayout.CENTER);

        // Window closing event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();  // Close the window
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        // Clear the display
        if (command.equals("C")) {
            display.setText("");
        } else if (command.equals("=")) {
            parser = new Parser();
            // Perform calculation
            try {
                double result = parser.evaluate(display.getText().toCharArray());
                display.setText(display.getText() + "=" + Double.toString(result));
            } catch (InterpreterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // Append the command to the display
            display.setText(display.getText() + command);
        }
    }

    public static void main(String[] args) {
        new CalculatorGUI();
    }
}
