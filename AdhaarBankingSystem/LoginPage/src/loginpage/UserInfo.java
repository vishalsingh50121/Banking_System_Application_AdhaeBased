package loginpage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

public class UserInfo extends JPanel {
    private JPanel contentPanel;
    private UserInfoTableModel model;
    private JTable userInfoTable;

    public UserInfo() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        String[] columnNames = {"Aadhaar", "Name", "Email", "Phone", "Address", "Age"};
        
        model = new UserInfoTableModel(columnNames);
        userInfoTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(userInfoTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Adjust column widths
        TableColumnModel columnModel = userInfoTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);  // Aadhaar
        columnModel.getColumn(1).setPreferredWidth(200);  // Name
        columnModel.getColumn(2).setPreferredWidth(150);  // Email
        columnModel.getColumn(3).setPreferredWidth(100);  // Phone
        columnModel.getColumn(4).setPreferredWidth(300);  // Address
        columnModel.getColumn(5).setPreferredWidth(50);  // Age

        fetchUserInfoData();

        // No JFrame code here anymore, as this will be part of the main frame
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    private void fetchUserInfoData() {
        String url = "jdbc:mysql://localhost:3306/account";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM register")) {

            model.clearData();

            while (rs.next()) {
                String aadhaar = rs.getString("aadhaar");
                String name = rs.getString("full_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int age = rs.getInt("age");

                model.addRow(aadhaar, name, email, phone, address, age);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class UserInfoTableModel extends AbstractTableModel {
        private String[] columnNames;
        private Object[][] data = new Object[0][0];

        public UserInfoTableModel(String[] columnNames) {
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

        public void addRow(String aadhaar, String name, String email, String phone, String address, int age) {
            Object[][] newData = new Object[data.length + 1][columnNames.length];
            System.arraycopy(data, 0, newData, 0, data.length);
            newData[data.length] = new Object[]{aadhaar, name, email, phone, address, age};
            data = newData;
            fireTableRowsInserted(data.length - 1, data.length - 1);
        }

        public void clearData() {
            data = new Object[0][0];
            fireTableDataChanged();
        }
    }
}
