package loginpage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.AbstractTableModel;

public class Accounts extends JPanel {
    JLabel aadhaarLabel, messageLabel;
    JTextField aadhaarField;
    JButton viewAccountButton;
    JTable accountTable;
    AccountTableModel model;

    public Accounts() {
        aadhaarLabel = new JLabel("Aadhaar: ");
        messageLabel = new JLabel("");

        aadhaarField = new JTextField(20);
        viewAccountButton = new JButton("View Account");

        // Set up table model and JTable
        String[] columnNames = {"Full Name", "Email", "Phone", "Address", "Age", "Total Amount"};
        model = new AccountTableModel(columnNames);
        accountTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(accountTable);
        accountTable.setFillsViewportHeight(true);  // Ensures table fills the viewport

        // Layout
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(aadhaarLabel);
        topPanel.add(aadhaarField);
        topPanel.add(viewAccountButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);  // This ensures the table takes up most of the space
        add(messageLabel, BorderLayout.SOUTH);

        viewAccountButton.addActionListener(e -> {
            String aadhaar = aadhaarField.getText().trim();

            if (aadhaar.isEmpty()) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Aadhaar is required.");
                return;
            }

            if (viewAccountDetails(aadhaar)) {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Account details fetched successfully.");
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Aadhaar not found or error fetching details.");
            }
        });
    }

    private boolean viewAccountDetails(String aadhaar) {
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/account", "root", "")) {
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM details WHERE aadhaar = ?");
        stmt.setString(1, aadhaar);
        ResultSet rs = stmt.executeQuery();

        // Clear any previous data in the table
        model.clearData();

        // If a matching account is found, add it to the table
        if (rs.next()) {
            String fullName = rs.getString("full_name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String address = rs.getString("address");
            int age = rs.getInt("age");
            double totalAmount = rs.getDouble("TotalAmount");

            // Add the new row to the table model
            model.addRow(fullName, email, phone, address, age, totalAmount);

            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Account details fetched successfully.");
            return true;
        } else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("No account found with the given Aadhaar.");
            return false;
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        messageLabel.setForeground(Color.RED);
        messageLabel.setText("Error fetching account details.");
    }
    return false;
}


    class AccountTableModel extends AbstractTableModel {
        private String[] columnNames;
        private Object[][] data = new Object[0][0];

        public AccountTableModel(String[] columnNames) {
            this.columnNames = columnNames;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        public void addRow(String fullName, String email, String phone, String address, int age, double totalAmount) {
            Object[][] newData = new Object[data.length + 1][columnNames.length];
            System.arraycopy(data, 0, newData, 0, data.length);
            newData[data.length] = new Object[]{fullName, email, phone, address, age, totalAmount};
            data = newData;
            fireTableRowsInserted(data.length - 1, data.length - 1);
        }

        public void clearData() {
            data = new Object[0][0];
            fireTableDataChanged();
        }
    }
}
