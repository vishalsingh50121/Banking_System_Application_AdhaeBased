package loginpage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPage extends JFrame implements ActionListener {

    JLabel l1, l2, message;
    JTextField t1;
    JPasswordField t2;
    JButton b1;
    JRadioButton rb1, rb2;
    ButtonGroup bg;
    JCheckBox showPassword;

    LoginPage() {
        rb1 = new JRadioButton("Login");
        rb2 = new JRadioButton("Register");
        bg = new ButtonGroup();
        bg.add(rb1);
        bg.add(rb2);

        rb1.setSelected(true);

        l1 = new JLabel("Aadhaar: ");
        t1 = new JTextField(20);

        l2 = new JLabel("Password: ");
        t2 = new JPasswordField(20);

        b1 = new JButton("Login");

        showPassword = new JCheckBox("Show Password");
        showPassword.addActionListener(this);

        message = new JLabel("");
        message.setForeground(Color.RED); // Set error message color

        Container cp = getContentPane();
        cp.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        cp.add(rb1, gbc);

        gbc.gridx = 1;
        cp.add(rb2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        cp.add(l1, gbc);

        gbc.gridx = 1;
        cp.add(t1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        cp.add(l2, gbc);

        gbc.gridx = 1;
        cp.add(t2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        cp.add(showPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        cp.add(b1, gbc);

        gbc.gridy = 5;
        cp.add(message, gbc);

        setTitle("Login Page");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        b1.addActionListener(this);
        rb2.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                t2.setEchoChar((char) 0);
            } else {
                t2.setEchoChar('*');
            }
        } else if (ae.getSource() == b1) {
            if (rb1.isSelected()) {
                String aadhaar = t1.getText().trim();
                String password = new String(t2.getPassword()).trim();

                if (!aadhaar.isEmpty() && !password.isEmpty()) {
                    if (validateLogin(aadhaar, password)) {
                        message.setForeground(Color.GREEN); // Success message color
                        message.setText("Login successful");
                        message.revalidate();
                        new HomePage(); // Open the Home page if login is successful
                        dispose();
                    } else {
                        message.setForeground(Color.RED); // Error message color
                        message.setText("Invalid Aadhaar or password");
                        message.revalidate();
                    }
                } else {
                    message.setForeground(Color.RED);
                    message.setText("Please fill all fields");
                    message.revalidate();
                }
            }
        } else if (ae.getSource() == rb2) {
            new RegisterPage();
            dispose();
        }
    }

    private boolean validateLogin(String aadhaar, String password) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/account", "root", "");
             PreparedStatement pst = con.prepareStatement("SELECT * FROM register WHERE aadhaar = ? AND password = ?")) {
            pst.setString(1, aadhaar);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            message.setForeground(Color.RED);
            message.setText("Database error, please try again later");
            message.revalidate();
        }
        return false;
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
