package loginpage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JFrame implements ActionListener {
    JMenuBar mb;
    JMenu accountsMenu, creditMenu, withdrawMenu, userInfoMenu;
    JMenuItem userInfoMenuItem, creditMenuItem, withdrawMenuItem, accountsMenuItem;
    JPanel contentPanel;

    // Declare the panel objects without instantiating them in the constructor
    private UserInfo userInfoPanel;
    private CreditPage creditPagePanel;
    private WithdrawPage withdrawPagePanel;
    private Accounts accountsPanel;

    public HomePage() {
        mb = new JMenuBar();
        accountsMenu = new JMenu("Accounts");
        creditMenu = new JMenu("Credit");
        withdrawMenu = new JMenu("Withdraw");
        userInfoMenu = new JMenu("User Information");

        userInfoMenuItem = new JMenuItem("User Information");
        creditMenuItem = new JMenuItem("Credit");
        withdrawMenuItem = new JMenuItem("Withdraw");
        accountsMenuItem = new JMenuItem("Accounts");

        // Set action commands for menu items
        userInfoMenuItem.setActionCommand("UserInfo");
        creditMenuItem.setActionCommand("CreditPage");
        withdrawMenuItem.setActionCommand("WithdrawPage");
        accountsMenuItem.setActionCommand("Accounts");

        // Add menu items to respective menus
        userInfoMenu.add(userInfoMenuItem);
        creditMenu.add(creditMenuItem);
        withdrawMenu.add(withdrawMenuItem);
        accountsMenu.add(accountsMenuItem);

        // Add menus to the menu bar
        mb.add(accountsMenu);
        mb.add(creditMenu);
        mb.add(withdrawMenu);
        mb.add(userInfoMenu);

        // Add action listeners for each menu item
        userInfoMenuItem.addActionListener(this);
        creditMenuItem.addActionListener(this);
        withdrawMenuItem.addActionListener(this);
        accountsMenuItem.addActionListener(this);

        // Set the menu bar for the frame
        setJMenuBar(mb);

        // Initialize content panel and set its layout to CardLayout
        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Set up frame properties
        setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        contentPanel.removeAll();  // Remove any previous content

        String command = e.getActionCommand();

        // Instantiate the panels when the corresponding menu item is clicked
        if (command.equals("UserInfo")) {
            if (userInfoPanel == null) {
                userInfoPanel = new UserInfo();
            }
            contentPanel.add(userInfoPanel, "UserInfo");
        } else if (command.equals("CreditPage")) {
            if (creditPagePanel == null) {
                creditPagePanel = new CreditPage();
            }
            contentPanel.add(creditPagePanel, "CreditPage");
        } else if (command.equals("WithdrawPage")) {
            if (withdrawPagePanel == null) {
                withdrawPagePanel = new WithdrawPage();
            }
            contentPanel.add(withdrawPagePanel, "WithdrawPage");
        } else if (command.equals("Accounts")) {
            if (accountsPanel == null) {
                accountsPanel = new Accounts();
            }
            contentPanel.add(accountsPanel, "Accounts");
        }

        // Switch to the newly added panel
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, command);

        // Revalidate and repaint the content panel to ensure the changes are reflected
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        new HomePage();
    }
}
