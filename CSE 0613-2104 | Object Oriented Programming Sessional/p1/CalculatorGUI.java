import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorGUI extends JFrame implements ActionListener {

    private JTextField display;

    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    private JButton[] numberButtons = new JButton[10];
    private JButton addButton;
    private JButton subtractButton;
    private JButton multiplyButton;
    private JButton divideButton;
    private JButton equalButton;
    private JButton decimalButton;
    private JButton clearButton;
    private JButton kgButton;
    private JButton tempButton;

    public CalculatorGUI() {

        setTitle("Java Swing Calculator");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new GridLayout(4, 4, 5, 5));

        for (int i = 0; i <= 9; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].setFont(new Font("Arial", Font.BOLD, 20));
            numberButtons[i].addActionListener(this);
        }

        addButton = new JButton("+");
        subtractButton = new JButton("-");
        multiplyButton = new JButton("*");
        divideButton = new JButton("/");
        equalButton = new JButton("=");
        decimalButton = new JButton(".");
        clearButton = new JButton("C");

        JButton[] operators = {
                addButton,
                subtractButton,
                multiplyButton,
                divideButton,
                equalButton,
                decimalButton,
                clearButton
        };

        for (JButton button : operators) {
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.addActionListener(this);
        }

        calculatorPanel.add(numberButtons[7]);
        calculatorPanel.add(numberButtons[8]);
        calculatorPanel.add(numberButtons[9]);
        calculatorPanel.add(divideButton);

        calculatorPanel.add(numberButtons[4]);
        calculatorPanel.add(numberButtons[5]);
        calculatorPanel.add(numberButtons[6]);
        calculatorPanel.add(multiplyButton);

        calculatorPanel.add(numberButtons[1]);
        calculatorPanel.add(numberButtons[2]);
        calculatorPanel.add(numberButtons[3]);
        calculatorPanel.add(subtractButton);

        calculatorPanel.add(clearButton);
        calculatorPanel.add(numberButtons[0]);
        calculatorPanel.add(equalButton);
        calculatorPanel.add(addButton);

        add(calculatorPanel, BorderLayout.CENTER);

        JPanel conversionPanel = new JPanel();
        conversionPanel.setLayout(new GridLayout(1, 2, 5, 5));

        kgButton = new JButton("kg → lb");
        tempButton = new JButton("°C → °F");

        kgButton.setFont(new Font("Arial", Font.BOLD, 18));
        tempButton.setFont(new Font("Arial", Font.BOLD, 18));

        kgButton.addActionListener(this);
        tempButton.addActionListener(this);

        conversionPanel.add(kgButton);
        conversionPanel.add(tempButton);

        add(conversionPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        return a / b;
    }

    public double kgToLb(double kg) {
        return kg * 2.20462;
    }

    public double celsiusToFahrenheit(double c) {
        return (c * 9 / 5) + 32;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        for (int i = 0; i <= 9; i++) {

            if (source == numberButtons[i]) {

                if (startNewNumber) {
                    display.setText("" + i);
                    startNewNumber = false;
                } else {
                    display.setText(display.getText() + i);
                }

                return;
            }
        }

        if (source == decimalButton) {

            if (startNewNumber) {
                display.setText("0.");
                startNewNumber = false;
            } else if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }

            return;
        }

        if (source == clearButton) {

            display.setText("");
            firstNumber = 0;
            operator = "";
            startNewNumber = true;

            return;
        }

        if (source == addButton ||
                source == subtractButton ||
                source == multiplyButton ||
                source == divideButton) {

            if (display.getText().isEmpty()) {
                return;
            }

            firstNumber = Double.parseDouble(display.getText());

            if (source == addButton) {
                operator = "+";
            } else if (source == subtractButton) {
                operator = "-";
            } else if (source == multiplyButton) {
                operator = "*";
            } else {
                operator = "/";
            }

            startNewNumber = true;

            return;
        }

        if (source == equalButton) {

            if (display.getText().isEmpty()) {
                return;
            }

            double secondNumber = Double.parseDouble(display.getText());
            double result = 0;

            switch (operator) {

                case "+":
                    result = add(firstNumber, secondNumber);
                    break;

                case "-":
                    result = subtract(firstNumber, secondNumber);
                    break;

                case "*":
                    result = multiply(firstNumber, secondNumber);
                    break;

                case "/":

                    if (secondNumber == 0) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Cannot divide by zero!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);

                        display.setText("");
                        startNewNumber = true;
                        return;
                    }

                    result = divide(firstNumber, secondNumber);
                    break;

                default:
                    return;
            }

            display.setText(String.valueOf(result));
            startNewNumber = true;
            return;
        }

        if (source == kgButton) {

            if (display.getText().isEmpty()) {
                return;
            }

            double value = Double.parseDouble(display.getText());
            double result = kgToLb(value);

            display.setText(String.format("%.4f", result));
            startNewNumber = true;

            return;
        }

        if (source == tempButton) {

            if (display.getText().isEmpty()) {
                return;
            }

            double value = Double.parseDouble(display.getText());
            double result = celsiusToFahrenheit(value);

            display.setText(String.format("%.2f", result));
            startNewNumber = true;

            return;
        }

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalculatorGUI();
            }
        });

    }

}