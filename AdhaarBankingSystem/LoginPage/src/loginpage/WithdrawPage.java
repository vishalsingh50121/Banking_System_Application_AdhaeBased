package loginpage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class WithdrawPage extends JPanel {
    JLabel aadhaarLabel, withdrawLabel, messageLabel;
    JTextField aadhaarField, withdrawField;
    JButton withdrawButton;

    public WithdrawPage() {
        aadhaarLabel = new JLabel("Aadhaar: ");
        withdrawLabel = new JLabel("Withdraw Amount: ");
        messageLabel = new JLabel("");

        aadhaarField = new JTextField(20);
        withdrawField = new JTextField(20);

        withdrawButton = new JButton("Withdraw");

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
        add(withdrawLabel, gbc);
        gbc.gridx = 1;
        add(withdrawField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(withdrawButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel, gbc);

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String aadhaar = aadhaarField.getText().trim();
                String withdrawAmountText = withdrawField.getText().trim();

                if (aadhaar.isEmpty() || withdrawAmountText.isEmpty()) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("All fields are required.");
                    return;
                }

                try {
                    double withdrawAmount = Double.parseDouble(withdrawAmountText);
                    if (withdrawAmount <= 0) {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText("Withdraw amount must be positive.");
                        return;
                    }

                    if (processWithdraw(aadhaar, withdrawAmount)) {
                        messageLabel.setForeground(Color.GREEN);
                        messageLabel.setText("Withdrawal successful!");
                    } else {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText("Invalid Aadhaar or insufficient balance.");
                    }
                } catch (NumberFormatException ex) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Invalid withdraw amount.");
                }
            }
        });
    }

    private boolean processWithdraw(String aadhaar, double withdrawAmount) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/account", "root", "")) {
            PreparedStatement checkStmt = con.prepareStatement("SELECT TotalAmount FROM details WHERE aadhaar = ?");
            checkStmt.setString(1, aadhaar);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("TotalAmount");

                if (currentBalance >= withdrawAmount) {
                    double newBalance = currentBalance - withdrawAmount;

                    PreparedStatement updateStmt = con.prepareStatement(
                            "UPDATE details SET TotalAmount = ? WHERE aadhaar = ?");
                    updateStmt.setDouble(1, newBalance);
                    updateStmt.setString(2, aadhaar);

                    int rowsAffected = updateStmt.executeUpdate();
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Error processing withdrawal.");
        }
        return false;
    }
}
