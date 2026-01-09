package loginpage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CreditPage extends JPanel {
    JLabel aadhaarLabel, creditLabel, messageLabel;
    JTextField aadhaarField, creditField;
    JButton creditButton;

    public CreditPage() {
        aadhaarLabel = new JLabel("Aadhaar: ");
        creditLabel = new JLabel("Credit Amount: ");
        messageLabel = new JLabel("");

        aadhaarField = new JTextField(20);
        creditField = new JTextField(20);

        creditButton = new JButton("Credit");

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(aadhaarLabel, gbc);
        gbc.gridx = 1;
        add(aadhaarField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(creditLabel, gbc);
        gbc.gridx = 1;
        add(creditField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(creditButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel, gbc);

        creditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String aadhaar = aadhaarField.getText().trim();
                String creditAmountText = creditField.getText().trim();

                if (aadhaar.isEmpty() || creditAmountText.isEmpty()) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("All fields are required.");
                    return;
                }

                try {
                    double creditAmount = Double.parseDouble(creditAmountText);
                    if (creditAmount <= 0) {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText("Credit amount must be positive.");
                        return;
                    }

                    if (processCredit(aadhaar, creditAmount)) {
                        messageLabel.setForeground(Color.GREEN);
                        messageLabel.setText("Credit successful!");
                    } else {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText("Invalid Aadhaar or update failed.");
                    }
                } catch (NumberFormatException ex) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Invalid credit amount.");
                }
            }
        });
    }

    private boolean processCredit(String aadhaar, double creditAmount) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/account", "root", "")) {
            PreparedStatement checkStmt = con.prepareStatement("SELECT * FROM register WHERE aadhaar = ?");
            checkStmt.setString(1, aadhaar);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                PreparedStatement updateStmt = con.prepareStatement(
                        "UPDATE details SET TotalAmount = TotalAmount + ? WHERE aadhaar = ?");
                updateStmt.setDouble(1, creditAmount);
                updateStmt.setString(2, aadhaar);

                int rowsAffected = updateStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
