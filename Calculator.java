import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {

    private JTextField display;
    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    public Calculator() {
        setTitle("Java Swing Calculator");
        setSize(350, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Display
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 30));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(display, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttons = {
                "C", "DEL", "%", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "±", "0", ".", "="
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.addActionListener(this);

            if (text.equals("/") || text.equals("*") ||
                text.equals("-") || text.equals("+") ||
                text.equals("=")) {
                button.setBackground(new Color(255, 165, 0));
                button.setForeground(Color.WHITE);
            } else if (text.equals("C")) {
                button.setBackground(Color.RED);
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(230, 230, 230));
            }

            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Numbers
        if (command.matches("[0-9]")) {
            if (startNewNumber || display.getText().equals("0")) {
                display.setText(command);
                startNewNumber = false;
            } else {
                display.setText(display.getText() + command);
            }
        }

        // Decimal
        else if (command.equals(".")) {
            if (startNewNumber) {
                display.setText("0.");
                startNewNumber = false;
            } else if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        }

        // Clear
        else if (command.equals("C")) {
            clearCalculator();
        }

        // Backspace
        else if (command.equals("DEL")) {
            String text = display.getText();

            if (text.length() > 1) {
                display.setText(text.substring(0, text.length() - 1));
            } else {
                display.setText("0");
                startNewNumber = true;
            }
        }

        // Positive / Negative
        else if (command.equals("±")) {
            try {
                double number = Double.parseDouble(display.getText());
                number = -number;
                display.setText(formatNumber(number));
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        }

        // Percentage
        else if (command.equals("%")) {
            try {
                double number = Double.parseDouble(display.getText());
                number = number / 100;
                display.setText(formatNumber(number));
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        }

        // Operators
        else if (command.equals("+") || command.equals("-") ||
                 command.equals("*") || command.equals("/")) {

            try {
                firstNumber = Double.parseDouble(display.getText());
                operator = command;
                startNewNumber = true;
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        }

        // Equal
        else if (command.equals("=")) {
            calculateResult();
        }
    }

    private void calculateResult() {
        try {
            double secondNumber = Double.parseDouble(display.getText());
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;

                case "-":
                    result = firstNumber - secondNumber;
                    break;

                case "*":
                    result = firstNumber * secondNumber;
                    break;

                case "/":
                    if (secondNumber == 0) {
                        display.setText("Cannot divide by 0");
                        startNewNumber = true;
                        return;
                    }
                    result = firstNumber / secondNumber;
                    break;

                default:
                    return;
            }

            display.setText(formatNumber(result));
            startNewNumber = true;
            operator = "";

        } catch (NumberFormatException ex) {
            display.setText("Error");
            startNewNumber = true;
        }
    }

    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.valueOf((long) number);
        }
        return String.valueOf(number);
    }

    private void clearCalculator() {
        display.setText("0");
        firstNumber = 0;
        operator = "";
        startNewNumber = true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}
