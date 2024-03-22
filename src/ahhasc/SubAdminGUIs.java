package ahhasc;

import com.libs.DateLabelFormatter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class SubAdminGUIs {

    public SubAdminGUIs() {
    }

    public JPanel getAccountPanel() {
        getContent getAccContent = new getContent(ACCOUNT);
        return getAccContent.returnPanel();
    }

    public JPanel getAppointmentPanel() {
        getContent getAppContent = new getContent(APPOINTMENT);
        return getAppContent.returnPanel();
    }

    public void GoBackDashboard() {
        LoginGUI.AdminGUI.ReturnDashboard();
    }

    public String[][] GetDatasForTablePassAppointment() {
        ArrayList<Appointment> AdminPastAppointment = AHHASC.Admin.getAllPastAppointment();
        int AppointmentSize = AdminPastAppointment.size();

        String[][] Datas = new String[AppointmentSize][10]; // [CustName, TechName, AppCata, Req Data, AppIsue, FinDate, Cost, TechFeed, CustFeed, ID]

        for (int i = 0; i < AppointmentSize; i++) {
            Appointment appointment = AdminPastAppointment.get(i);

            Datas[i][0] = appointment.getCustomerName();
            Datas[i][1] = appointment.getTechnicianName();
            Datas[i][2] = appointment.getApplianceCatagory();
            Datas[i][3] = appointment.getRequestedDate().toString();
            Datas[i][4] = appointment.getApplianceIssue();
            Datas[i][5] = appointment.getFinishDate().toString();
            Datas[i][6] = String.valueOf(appointment.getCost());
            Datas[i][7] = appointment.getTechnicianFeedback();
            Datas[i][8] = appointment.getCustomerFeedback();
            Datas[i][9] = String.valueOf(appointment.getAppointmentID());
        }

        return Datas;
    }

    static JPanel RequestedPanel = new JPanel();

    static Font DefaultFont = new Font("Serif", Font.BOLD, 50);
    static Color ButtonColor = new Color(242, 242, 242);
    static Font ButtonFont = new Font("Serif", Font.PLAIN, 20);
    static Font TextFont = new Font("Serif", Font.PLAIN, 25);
    static Font LabelFont = new Font("Serif", Font.BOLD, 20);
    static Insets FieldInsets = new Insets(0, 5, 0, 0);

    public static final int ACCOUNT = 0;
    public static final int APPOINTMENT = 1;

    static int[] GetTodayDate = {LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()};
}

class getContent extends SubAdminGUIs {

    JPanel tempPanel = new JPanel();

    public getContent(int i) {
        switch (i) {
            case ACCOUNT:
                ManageAccount MA = new ManageAccount();
                tempPanel = MA.returnPanel();
                break;
            case APPOINTMENT:
                ManageAppointment MaApp = new ManageAppointment();
                tempPanel = MaApp.returnPanel();
                break;
        }
    }

    private class ManageAccount {

        public ManageAccount() {
            tempPanel = new JPanel();
            gl = new GroupLayout(tempPanel);
            tempPanel.setLayout(gl);

            InitCompManageAccount();
            LayoutManageAccount();

            BackButton.addActionListener((ActionEvent e) -> {
                GoBackDashboard();
            });

            AddAccount.addActionListener((ActionEvent e) -> {
                AccountActionPressed(e);
            });

            EditAccount.addActionListener((ActionEvent e) -> {
                AccountActionPressed(e);
            });

            DeleteAccount.addActionListener((ActionEvent e) -> {
                AccountActionPressed(e);
            });

        }

        public JPanel returnPanel() {
            return tempPanel;
        }

        private void AccountActionPressed(ActionEvent e) {
            JButton obj = (JButton) e.getSource();
            String ButtonName = obj.getText();

            createSubFrame CreateSF = new createSubFrame("Create Account", ButtonName);
        }

        private class createSubFrame {

            public createSubFrame(String FrameName, String btnName) {
                PopUpBox = new JFrame();
                PopUpBox.setTitle(FrameName);
                PopUpBox.setSize(600, 300);
                PopUpBox.setLocation(400, 300);
                PopUpBox.setResizable(false);
                PopUpBox.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                PopUpBox.setLayout(new BorderLayout());

                JPanel PopUpBoxTempPanel = new JPanel();
                PopUpBoxTempPanel.setLayout(new GridLayout(4, 2));

                InitCompPopUpBox();

                CancelButton.addActionListener((ActionEvent ae) -> {
                    closePopUpBox(true);
                });

                PopUpBoxTempPanel.add(Name);
                PopUpBoxTempPanel.add(NameField);
                PopUpBoxTempPanel.add(Password);
                PopUpBoxTempPanel.add(PasswordField);
                PopUpBoxTempPanel.add(Role);
                PopUpBoxTempPanel.add(RolesBox);
                PopUpBoxTempPanel.add(CancelButton);
                PopUpBoxTempPanel.add(ProceedButton);

                PopUpBox.getContentPane().add(new JPanel(), BorderLayout.NORTH);
                PopUpBox.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
                PopUpBox.getContentPane().add(new JPanel(), BorderLayout.WEST);
                PopUpBox.getContentPane().add(new JPanel(), BorderLayout.EAST);
                PopUpBox.getContentPane().add(PopUpBoxTempPanel, BorderLayout.CENTER);

                try {
                    switch (btnName) {
                        case "Add Account":
                            System.out.println("print add acc");
                            ProceedButton.setText("Add to List");
                            ProceedButton.addActionListener((ActionEvent e) -> {
                                addAccountToList();
                            });
                            break;

                        case "Edit Account":
                            ProceedButton.setText("Update List");

                            String NameEA = null,
                             PasswordEA = null,
                             RoleEA = null;
                            int[] rowEA = AllAccounts.getSelectedRows();
                            for (int i = 0; i < rowEA.length; i++) {
                                NameEA = (String) AllAccounts.getValueAt(rowEA[i], 0);
                                PasswordEA = (String) AllAccounts.getValueAt(rowEA[i], 1);
                                RoleEA = (String) AllAccounts.getValueAt(rowEA[i], 2);
                            }

                            if (NameEA == null || PasswordEA == null || RoleEA == null) {
                                JOptionPane.showMessageDialog(null, "No Row Selected");
                            }

                            NameField.setText(NameEA);
                            PasswordField.setText(PasswordEA);
                            switch (RoleEA) {
                                case "Admin":
                                    RolesBox.setSelectedIndex(0);
                                    break;
                                case "Technician":
                                    RolesBox.setSelectedIndex(1);
                                    break;
                                case "Customer":
                                    RolesBox.setSelectedIndex(2);
                                    break;
                            }

                            ProceedButton.addActionListener((ActionEvent e) -> {
                                updateAccountToList();
                            });
                            break;

                        case "Delete Account":
                            ProceedButton.setText("Delete Account");
                            String NameDA = null,
                             PasswordDA = null,
                             RoleDA = null;
                            int[] rowDA = AllAccounts.getSelectedRows();
                            for (int i = 0; i < rowDA.length; i++) {
                                NameDA = (String) AllAccounts.getValueAt(rowDA[i], 0);
                                PasswordDA = (String) AllAccounts.getValueAt(rowDA[i], 1);
                                RoleDA = (String) AllAccounts.getValueAt(rowDA[i], 2);
                            }

                            if (NameDA == null || PasswordDA == null || RoleDA == null) {
                                JOptionPane.showMessageDialog(null, "No Row Selected");
                                throw new Exception();
                            }

                            NameField.setText(NameDA);
                            PasswordField.setText(PasswordDA);
                            switch (RoleDA) {
                                case "Admin":
                                    RolesBox.setSelectedIndex(0);
                                    break;
                                case "Technician":
                                    RolesBox.setSelectedIndex(1);
                                    break;
                                case "Customer":
                                    RolesBox.setSelectedIndex(2);
                                    break;
                            }

                            NameField.setEditable(false);
                            PasswordField.setEditable(false);

                            RolesBox.setEnabled(false);
                            RolesBox.setRenderer(new DefaultListCellRenderer() {
                                @Override
                                public void paint(Graphics g) {
                                    setForeground(Color.BLACK);
                                    super.paint(g);
                                }
                            });

                            ProceedButton.addActionListener((ActionEvent e) -> {
                                deleteAccountFromList();
                            });
                            break;
                    }
                    PopUpBox.setVisible(true);

                } catch (Exception err) {
                    System.out.println("Error inside SubAdminGUIs createSubFrame class");
                }
            }

            private void closePopUpBox(Boolean askPrompt) {
                if (askPrompt) {
                    int exitPopUpBox = JOptionPane.showConfirmDialog(null, "Are you sure you want exit?", "Are You Sure?", JOptionPane.YES_NO_OPTION);
                    if (exitPopUpBox == 0) {
                        NameField.setText("");
                        PasswordField.setText("");
                        PopUpBox.dispose();
                        PopUpBox = null;
                    }
                } else {
                    NameField.setText("");
                    PasswordField.setText("");
                    PopUpBox.dispose();
                    PopUpBox = null;
                }
            }

            private void addAccountToList() {
                try {
                    String Name = NameField.getText();
                    String Password = String.valueOf(PasswordField.getPassword());
                    String Role = RolesBox.getSelectedItem().toString();
                    int RoleInt = -1;

                    switch (Role) {
                        case "Admin":
                            RoleInt = 0;
                            break;
                        case "Technician":
                            RoleInt = 1;
                            break;
                        case "Customer":
                            RoleInt = 10;
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Incorrect Role Number");
                            throw new Exception();
                    }

                    if (Name.isEmpty() || Password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Username/Password Cannot be Empty");
                        throw new Exception();
                    }

                    for (int i = 0; i < DataIO.allUsers.size(); i++) {
                        if (DataIO.allUsers.get(i).getName().equals(Name)) {
                            JOptionPane.showMessageDialog(null, "This User has been Registered Before");
                            throw new Exception();
                        }
                    }

                    DataIO.allUsers.add(new Users(Name, Password, RoleInt));
                    DataIO.writeUser();
                    JOptionPane.showMessageDialog(null, "Input Successfully");
                    closePopUpBox(false);
                    LoginGUI.AdminGUI.OpenAccounts();
                } catch (Exception err) {
                }
            }

            private void updateAccountToList() {
                String NameOld = null;
                String PasswordOld = null;
                String RoleOld = null;

                int[] row = AllAccounts.getSelectedRows();
                for (int i = 0; i < row.length; i++) {
                    NameOld = (String) AllAccounts.getValueAt(row[i], 0);
                    PasswordOld = (String) AllAccounts.getValueAt(row[i], 1);
                    RoleOld = (String) AllAccounts.getValueAt(row[i], 2);
                }

                String NameNew = NameField.getText();
                String PasswordNew = String.valueOf(PasswordField.getPassword());
                String RoleNew = RolesBox.getSelectedItem().toString();
                try {
                    if (NameNew.equals(NameOld) && PasswordNew.equals(PasswordOld) && RoleNew.equals(RoleOld)) {
                        int Confirmation = JOptionPane.showConfirmDialog(null, "No datas has been change. \nThis will not update the database. \nAre You sure?", "Confirm Update Empty Data", JOptionPane.YES_NO_OPTION);
                        if (Confirmation == 0) {
                            JOptionPane.showMessageDialog(null, "No data has been Updated");
                            closePopUpBox(false);
                            LoginGUI.AdminGUI.OpenAccounts();
                        }
                    } else {
                        for (int i = 0; i < DataIO.allUsers.size(); i++) {
                            if (DataIO.allUsers.get(i).getName().equals(NameOld)) {
                                DataIO.allUsers.remove(i);
                            }
                        }

                        int RoleInt;
                        switch (RoleNew) {
                            case "Admin":
                                RoleInt = 0;
                                break;
                            case "Technician":
                                RoleInt = 1;
                                break;
                            case "Customer":
                                RoleInt = 10;
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "Incorrect Role Number");
                                throw new Exception();
                        }

                        DataIO.allUsers.add(new Users(NameNew, PasswordNew, RoleInt));
                        DataIO.writeUser();
                        JOptionPane.showMessageDialog(null, "Update Successfully");
                        closePopUpBox(false);
                        LoginGUI.AdminGUI.OpenAccounts();
                    }
                } catch (Exception err) {
                }

            }

            private void deleteAccountFromList() {
                JLabel DeleteTextFormat = new JLabel(String.format("<html>Do you want to delete this user?<br>Name: %s<br>Password: %s<br>Role: %s</html>", NameField.getText(), String.valueOf(PasswordField.getPassword()), RolesBox.getSelectedItem().toString()));
                DeleteTextFormat.setFont(new Font("Serif", Font.PLAIN, 20));
                int DeleteConfirmation = JOptionPane.showConfirmDialog(null, DeleteTextFormat, "Are You Sure?", JOptionPane.YES_NO_OPTION);
                // the user confirm for the 1st time
                if (DeleteConfirmation == 0) {
                    JLabel FinalConfirmText = new JLabel("<html>Are You Sure<br>There is no way to reverse this action!</html>");
                    FinalConfirmText.setFont(new Font("Serif", Font.PLAIN, 20));
                    int FinalConfirmation = JOptionPane.showConfirmDialog(null, FinalConfirmText, "Final Confirm", JOptionPane.YES_NO_OPTION);
                    // the user confirm for the final timee
                    if (FinalConfirmation == 0) {
                        for (int i = 0; i < DataIO.allUsers.size(); i++) {
                            if (DataIO.allUsers.get(i).getName().equals(NameField.getText())) {
                                DataIO.allUsers.remove(i);
                            }
                        }
                        DataIO.writeUser();
                        JOptionPane.showMessageDialog(null, "Account Delete Successfully");
                        closePopUpBox(false);
                        LoginGUI.AdminGUI.OpenAccounts();
                    } else {
                        JOptionPane.showMessageDialog(null, "Action Cancelled");
                        closePopUpBox(false);
                        LoginGUI.AdminGUI.OpenAccounts();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Action Cancelled");
                    closePopUpBox(false);
                    LoginGUI.AdminGUI.OpenAccounts();
                }
            }

            private void InitCompPopUpBox() {
                NameField = new JTextField();
                PasswordField = new JPasswordField();
                RolesBox = new JComboBox(Roles_Selection);

                Name = new JLabel("Name");
                Name.setFont(LabelFont);
                Password = new JLabel("Password");
                Password.setFont(LabelFont);
                Role = new JLabel("Role");
                Role.setFont(LabelFont);

                NameField.setMargin(FieldInsets);
                NameField.setFont(TextFont);

                PasswordField.setMargin(FieldInsets);
                PasswordField.setFont(TextFont);

                RolesBox.setFont(TextFont);

                CancelButton = new JButton("Cancel");
                CancelButton.setFont(ButtonFont);
                CancelButton.setBackground(ButtonColor);
                CancelButton.setFocusPainted(false);

                ProceedButton = new JButton();
                ProceedButton.setFont(ButtonFont);
                ProceedButton.setBackground(ButtonColor);
                ProceedButton.setFocusPainted(false);
            }

            public JFrame PopUpBox;

            public String[] Roles_Selection = {"Admin", "Technician", "Customer"};

            private JTextField NameField;
            private JPasswordField PasswordField;
            private JComboBox RolesBox;
            private JLabel Name, Password, Role;
            private JButton CancelButton, ProceedButton;
        }

        private void InitCompManageAccount() {
            BackButton = new JButton("Back");
            BackButton.setBackground(ButtonColor);
            BackButton.setFont(ButtonFont);
            BackButton.setFocusPainted(false);

            MenuName = new JLabel("All Accounts");
            MenuName.setFont(DefaultFont);
            MenuName.setHorizontalAlignment(JLabel.CENTER);

            AddAccount = new JButton("Add Account");
            AddAccount.setBackground(ButtonColor);
            AddAccount.setFont(ButtonFont);
            AddAccount.setFocusPainted(false);

            EditAccount = new JButton("Edit Account");
            EditAccount.setBackground(ButtonColor);
            EditAccount.setFont(ButtonFont);
            EditAccount.setFocusPainted(false);

            DeleteAccount = new JButton("Delete Account");
            DeleteAccount.setBackground(ButtonColor);
            DeleteAccount.setFont(ButtonFont);
            DeleteAccount.setFocusPainted(false);

            AllAccounts = new JTable(CreateTableData(), ColName);
            select = AllAccounts.getSelectionModel();
            select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            AllAccounts.setShowGrid(false);
            AllAccounts.setCellSelectionEnabled(false);
            AllAccounts.setRowSelectionAllowed(true);
            AllAccounts.setDefaultEditor(Object.class, null);
            AllAccounts.setFocusable(false);
            AllAccounts.getTableHeader().setFont(new Font("Serif", Font.BOLD, 20));
            AllAccounts.setFont(new Font("Serif", Font.PLAIN, 20));
            AllAccounts.setRowHeight(50);

            scp = new JScrollPane(AllAccounts);

        }

        private void LayoutManageAccount() {
            gl.setVerticalGroup(gl.createSequentialGroup()
                    .addGap(10)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(MenuName, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(gl.createSequentialGroup()
                            //                .addComponent(AllAccounts.getTableHeader(),GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                            .addComponent(scp, GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
                    )
                    .addGap(30)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(AddAccount, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(EditAccount, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(DeleteAccount, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGap(30)
            );

            gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(gl.createSequentialGroup()
                            .addGap(10)
                            .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                            .addComponent(MenuName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                    )
                    .addGroup(gl.createSequentialGroup()
                            .addGap(20)
                            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(scp, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            )
                            .addGap(20)
                    )
                    .addGroup(gl.createSequentialGroup()
                            .addGap(50)
                            .addComponent(AddAccount, GroupLayout.PREFERRED_SIZE, 150, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                            .addComponent(EditAccount, GroupLayout.PREFERRED_SIZE, 150, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                            .addComponent(DeleteAccount, GroupLayout.PREFERRED_SIZE, 150, Short.MAX_VALUE)
                            .addGap(50)
                    )
            );
        }

        private String[][] CreateTableData() {
            int UserSize = DataIO.allUsers.size();
            String[][] TableDataTemp = new String[UserSize][3];
            for (int i = 0; i < UserSize; i++) {
                TableDataTemp[i][0] = DataIO.allUsers.get(i).getName();
                TableDataTemp[i][1] = DataIO.allUsers.get(i).getPassword();
                switch (DataIO.allUsers.get(i).getRole()) {
                    case 0:
                        TableDataTemp[i][2] = "Admin";
                        break;
                    case 1:
                        TableDataTemp[i][2] = "Technician";
                        break;
                    case 10:
                        TableDataTemp[i][2] = "Customer";
                        break;
                }

            }
            return TableDataTemp;
        }

        final String[] ColName = {"USERNAME", "PASSWORD", "ROLE"};

        private JPanel tempPanel;
        private GroupLayout gl;

        private JButton BackButton, AddAccount, EditAccount, DeleteAccount;
        private JLabel MenuName;
        public JTable AllAccounts;
        private JScrollPane scp;
        private ListSelectionModel select;

    }

    private class ManageAppointment {

        public ManageAppointment() {
            tempPanel = new JPanel();
            gl = new GroupLayout(tempPanel);
            tempPanel.setLayout(gl);

            InitCompManageAppointment();
            LayoutManageAppointment();

            BackButton.addActionListener((ActionEvent e) -> {
                GoBackDashboard();
            });

            AllAppointments.addActionListener((ActionEvent e) -> {
                updatePanelViewAppointment();
            });

            CreateAppointments.addActionListener((ActionEvent e) -> {
                updatePanelCreateAppointment();
            });
        }

        private class CreateAppPanel {

            public CreateAppPanel() {
                tempPanel = new JPanel();
                gl = new GroupLayout(tempPanel);
                tempPanel.setLayout(gl);

                InitCompCreateAppointment();
                LayoutCreateAppointment();

                TechnicianSelection.addActionListener((ActionEvent e) -> {
                    GetDataCustTechSubFrame CuTePicker = new GetDataCustTechSubFrame(0);
                    CuTePicker.PopUpCreateApp.setVisible(true);
                });

                CustomerSelection.addActionListener((ActionEvent e) -> {
                    GetDataCustTechSubFrame CuTePicker = new GetDataCustTechSubFrame(1);
                    CuTePicker.PopUpCreateApp.setVisible(true);
                });

                ApplianceIssue.addFocusListener(new FocusListener() {
                    // this part of code is to create a placeholder feel inside the appliance Issue TextBox
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (ApplianceIssue.getText().equals("Enter your Appliance Issue")) {
                            ApplianceIssue.setText("");
                            ApplianceIssue.setForeground(Color.BLACK);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent fe) {
                        if (ApplianceIssue.getText().isEmpty()) {
                            ApplianceIssue.setForeground(Color.GRAY);
                            ApplianceIssue.setText("Enter Your Appliance Issue");
                        }
                    }
                });

                ComfirmCreateAppointment.addActionListener((ActionEvent e) -> {
                    AddAppointment();
                });

                BackButton.addActionListener((ActionEvent e) -> {
                    LoginGUI.AdminGUI.OpenAppointments();
                });
            }

            private class GetDataCustTechSubFrame {

                public GetDataCustTechSubFrame(int level) {
                    PopUpCreateApp = new JFrame("Create Appointment");
                    PopUpCreateApp.setSize(600, 300);
                    PopUpCreateApp.setResizable(false);
                    PopUpCreateApp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    PopUpCreateApp.setLayout(new BorderLayout());

                    listName = new DefaultListModel<>();

                    switch (level) {
                        case 0: //get tech
                            PopUpCreateApp.setTitle("Select Technician");
                            AllName = AHHASC.Admin.getAllTechnicianName();
                            break;

                        case 1: // get cust
                            PopUpCreateApp.setTitle("Select Customer");
                            AllName = AHHASC.Admin.getAllCustomerName();
                            break;
                    }

                    for (int i = 0; i < AllName.size(); i++) {
                        listName.addElement(AllName.get(i));
                    }

                    UsersList = new JList<>(listName);
                    UsersList.setFont(TextFont);

                    BackButton = new JButton("Back");
                    BackButton.setBackground(ButtonColor);
                    BackButton.setFont(TextFont);
                    BackButton.setFocusPainted(false);
                    BackButton.addActionListener((ActionEvent e) -> {
                        PopUpCreateApp.setVisible(false);
                        PopUpCreateApp.dispose();
                        PopUpCreateApp = null;
                    });

                    ConfirmButton = new JButton("Confirm");
                    ConfirmButton.setBackground(ButtonColor);
                    ConfirmButton.setFont(TextFont);
                    ConfirmButton.setFocusPainted(false);
                    ConfirmButton.addActionListener((ActionEvent e) -> {
                        try {
                            if (level == 0) {
                                TechnicianName.setText(UsersList.getSelectedValue().toString());
                                PopUpCreateApp.setVisible(false);
                                PopUpCreateApp.dispose();
                                PopUpCreateApp = null;
                            }

                            if (level == 1) {
                                CustomerName.setText(UsersList.getSelectedValue().toString());
                                PopUpCreateApp.setVisible(false);
                                PopUpCreateApp.dispose();
                                PopUpCreateApp = null;
                            }
                        } catch (NullPointerException err) {
                            System.out.println("Cannot select Empty");
                            JOptionPane.showMessageDialog(null, "Please Select One");
                        }
                    });

                    JPanel BottomPanel = new JPanel();
                    BottomPanel.add(BackButton);
                    BottomPanel.add(ConfirmButton);

                    PopUpCreateApp.add(new JPanel(), BorderLayout.NORTH);
                    PopUpCreateApp.add(new JPanel(), BorderLayout.EAST);
                    PopUpCreateApp.add(new JPanel(), BorderLayout.WEST);
                    PopUpCreateApp.add(BottomPanel, BorderLayout.SOUTH);
                    PopUpCreateApp.add(UsersList, BorderLayout.CENTER);

                }

                public JFrame PopUpCreateApp;
                private JButton BackButton, ConfirmButton;
                private JList UsersList;
                private DefaultListModel listName;
                private ArrayList<String> AllName = new ArrayList<String>();
            }

            public JPanel returnPanel() {
                return tempPanel;
            }

            private void AddAppointment() {
                try {
                    if (CustomerName.getText().isEmpty() || TechnicianName.getText().isEmpty() || ApplianceLists.getSelectedIndex() <= -1) {
                        JOptionPane.showMessageDialog(null, new JLabel("Some Input have not been filled"));
                        throw new Exception();
                    }
                    int AppointmentSize = DataIO.allAppointment.size();
                    int AppID = 5000 + AppointmentSize;
                    DataIO.allAppointment.add(new Appointment(AppID, CustomerName.getText(), Arrays.toString(ApplianceLists.getSelectedObjects()), ApplianceIssue.getText(), TechnicianName.getText(), LocalDate.parse(datePicker.getJFormattedTextField().getText())));
                    DataIO.writeAppointment();
                    JOptionPane.showMessageDialog(null, new JLabel("Appointment Added. \nReturning Appointment Menu"));
                    LoginGUI.AdminGUI.OpenAppointments();

                } catch (Exception ex) {
                }
            }

            private void InitCompCreateAppointment() {

                CreateAppointmentMenuText = new JLabel("Create Appointment");
                CreateAppointmentMenuText.setFont(DefaultFont);
                CreateAppointmentMenuText.setHorizontalAlignment(JLabel.CENTER);

                CustomerName = new JTextField();
                TechnicianName = new JTextField();
                BackButton = new JButton("Back");
                CustomerSelection = new JButton("...");
                TechnicianSelection = new JButton("...");
                ApplianceLists = new JComboBox(ApplianceCatagory) {
                    @Override
                    public Object getSelectedItem() {
                        Object selected = super.getSelectedItem();

                        if (selected == null) {
                            selected = "Select An Appliance Catagory";
                        }

                        return selected;
                    }
                };
                ApplianceLists.setSelectedIndex(-1);

                ApplianceIssue = new JTextField();

                model = new UtilDateModel();
                p = new Properties();
                p.put("text.today", "Today");
                p.put("text.month", "Month");
                p.put("text.year", "Year");
                datePanel = new JDatePanelImpl(model, p);
                datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

                model.setDate(GetTodayDate[0], GetTodayDate[1], GetTodayDate[2]);
                model.setSelected(true);

                datePicker.getComponent(0).setPreferredSize(new Dimension(200, 70)); // textbox
                datePicker.getComponent(0).setFont(TextFont);

                datePicker.getComponent(1).setPreferredSize(new Dimension(70, 10)); // button
                datePicker.getComponent(1).setFont(ButtonFont);
                datePicker.getComponent(1).setBackground(ButtonColor);

                ComfirmCreateAppointment = new JButton("Create Appointment");

                BackButton.setFont(ButtonFont);
                BackButton.setBackground(ButtonColor);
                BackButton.setFocusPainted(false);

                CustomerName.setEditable(false);
                CustomerName.setText("Select Customer Name");
                CustomerName.setMargin(new Insets(0, 5, 0, 0));
                CustomerName.setFont(TextFont);

                CustomerSelection.setFont(ButtonFont);
                CustomerSelection.setBackground(ButtonColor);
                CustomerSelection.setFocusPainted(false);

                TechnicianName.setEditable(false);
                TechnicianName.setText("Select Technician Name");
                TechnicianName.setMargin(new Insets(0, 5, 0, 0));
                TechnicianName.setFont(TextFont);

                TechnicianSelection.setFont(ButtonFont);
                TechnicianSelection.setBackground(ButtonColor);
                TechnicianSelection.setFocusPainted(false);

                ApplianceLists.setBackground(ButtonColor);
                ApplianceLists.setFont(TextFont);

                ApplianceIssue.setMargin(new Insets(0, 5, 0, 0));
                ApplianceIssue.setFont(TextFont);
                ApplianceIssue.setText("Enter your Appliance Issue");
                ApplianceIssue.setForeground(Color.GRAY);

                ComfirmCreateAppointment.setFont(ButtonFont);
                ComfirmCreateAppointment.setBackground(ButtonColor);
                ComfirmCreateAppointment.setFocusPainted(false);
            }

            private void LayoutCreateAppointment() {
                gl.setVerticalGroup(gl.createSequentialGroup()
                        .addGap(10)
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addComponent(CreateAppointmentMenuText, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(40)
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(CustomerName, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                .addComponent(CustomerSelection, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                .addComponent(TechnicianName, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                .addComponent(TechnicianSelection, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(40)
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(ApplianceLists, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                .addComponent(ApplianceIssue, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(40)
                        .addComponent(datePicker, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                        .addGap(40)
                        .addComponent(ComfirmCreateAppointment, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                );

                gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl.createSequentialGroup()
                                .addGap(10)
                                .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addComponent(CreateAppointmentMenuText, GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
                                .addGap(10)
                        )
                        .addGroup(gl.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                                .addGroup(gl.createSequentialGroup()
                                        .addComponent(CustomerName, GroupLayout.PREFERRED_SIZE, 180, Short.MAX_VALUE)
                                        .addComponent(CustomerSelection, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 60, GroupLayout.PREFERRED_SIZE)
                                .addGroup(gl.createSequentialGroup()
                                        .addComponent(TechnicianName, GroupLayout.PREFERRED_SIZE, 180, Short.MAX_VALUE)
                                        .addComponent(TechnicianSelection, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(gl.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                                .addComponent(ApplianceLists, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 60, GroupLayout.PREFERRED_SIZE)
                                .addComponent(ApplianceIssue, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(gl.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                                .addComponent(datePicker, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(gl.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(ComfirmCreateAppointment, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                        )
                );
            }

            private UtilDateModel model;
            private Properties p;
            private JDatePanelImpl datePanel;
            private JDatePickerImpl datePicker;
            private JLabel CreateAppointmentMenuText;
            private JComboBox ApplianceLists;
            private JTextField ApplianceIssue, CustomerName, TechnicianName;
            private JButton CustomerSelection, TechnicianSelection, ComfirmCreateAppointment, BackButton;
            private String[] ApplianceCatagory = {"TV", "Fridge", "Electronics"};

            private JPanel tempPanel;
            private GroupLayout gl;

        }

        private void updatePanelViewAppointment() {
            JPanel doPanelReq = new JPanel();

            PastApp PastAppPanel = new PastApp();
            doPanelReq = PastAppPanel.requestPanel();
            LoginGUI.AdminGUI.doRequstPanel(doPanelReq);
        }

        private void updatePanelCreateAppointment() {
            JPanel doPanelReq = new JPanel();

            CreateAppPanel CreAppPanel = new CreateAppPanel();
            doPanelReq = CreAppPanel.returnPanel();
            LoginGUI.AdminGUI.doRequstPanel(doPanelReq);
        }

        public JPanel returnPanel() {
            return tempPanel;
        }

        private void InitCompManageAppointment() {
            BackButton = new JButton("Back");
            BackButton.setBackground(ButtonColor);
            BackButton.setFont(ButtonFont);
            BackButton.setFocusPainted(false);

            MenuName = new JLabel("Appointment Manager");
            MenuName.setFont(DefaultFont);
            MenuName.setHorizontalAlignment(JLabel.CENTER);

            AllAppointments = new JButton("Appointments");
            AllAppointments.setFont(ButtonFont);
            AllAppointments.setBackground(ButtonColor);
            AllAppointments.setFocusPainted(false);

            CreateAppointments = new JButton("Create Appointment");
            CreateAppointments.setFont(ButtonFont);
            CreateAppointments.setBackground(ButtonColor);
            CreateAppointments.setFocusPainted(false);

        }

        private void LayoutManageAppointment() {
            gl.setVerticalGroup(gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                            .addComponent(MenuName, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                            .addGap(10)
                    )
                    .addGap(50)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(AllAppointments, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGap(50)
                    .addComponent(CreateAppointments, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
            );

            gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(gl.createSequentialGroup()
                            .addGap(10)
                            .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                            .addComponent(MenuName, GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
                            .addGap(10)
                    )
                    .addGroup(gl.createSequentialGroup()
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                            .addComponent(AllAppointments, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGroup(gl.createSequentialGroup()
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                            .addComponent(CreateAppointments, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                    )
            );
        }

        private JPanel tempPanel;
        private GroupLayout gl;

        private JButton BackButton, AllAppointments, CreateAppointments;
        private JLabel MenuName;

        private class PastApp {

            public PastApp() {
                tempPanel = new JPanel();
                gl = new GroupLayout(tempPanel);
                tempPanel.setLayout(gl);

                InitCompPastApp();
                LayoutOpenPassAppUI();

                BackButton.addActionListener((ActionEvent e) -> {
                    LoginGUI.AdminGUI.OpenAppointments();
                });

                DetailsButton.addActionListener((ActionEvent e) -> {
                    try {
                        if (SelectedAppID == 0) {
                            JOptionPane.showMessageDialog(null, "No Row Selected");
                            throw new Exception();
                        }

                        SubFrame SB = new SubFrame();
                        SB.showFrame();
                        LoginGUI.AdminGUI.frame.setVisible(false);
                    } catch (Exception err) {
                        System.out.println("appointments inside switch case 2 " + err);
                    }
                });

                PastAppointmentsTable.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int[] row = PastAppointmentsTable.getSelectedRows();
                        for (int i = 0; i < row.length; i++) {
                            SelectedAppID = Integer.parseInt(PastAppointmentsTable.getModel().getValueAt(row[i], 9).toString());
                            // [CustName, TechName, AppCata, Req Data, AppIsue, FinDate, Cost, TechFeed, CustFeed, ID]

                            SelectedDatas[0] = (String) PastAppointmentsTable.getModel().getValueAt(row[i], 0);
                            SelectedDatas[1] = (String) PastAppointmentsTable.getModel().getValueAt(row[i], 1);
                            SelectedDatas[2] = (String) PastAppointmentsTable.getModel().getValueAt(row[i], 2);
                            SelectedDatas[3] = (String) PastAppointmentsTable.getModel().getValueAt(row[i], 3);
                            SelectedDatas[4] = (String) PastAppointmentsTable.getModel().getValueAt(row[i], 4);
                            SelectedDatas[5] = (String) PastAppointmentsTable.getModel().getValueAt(row[i], 5);
                            SelectedDatas[6] = (String) PastAppointmentsTable.getModel().getValueAt(row[i], 6);
                            SelectedDatas[7] = (String) PastAppointmentsTable.getModel().getValueAt(row[i], 7);
                            SelectedDatas[8] = (String) PastAppointmentsTable.getModel().getValueAt(row[i], 8);
                            SelectedDatas[9] = (String) PastAppointmentsTable.getModel().getValueAt(row[i], 9);
                        }
                    }
                });
            }

            public JPanel requestPanel() {
                return tempPanel;
            }

            private class SubFrame {

                public SubFrame() {
                    SubFrame = new JFrame();
//                    SubFrame.setTitle();
                    SubFrame.setLayout(new BorderLayout());
                    SubFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    SubFrame.setSize(1200, 800);
                    SubFrame.setResizable(false);

                    JPanel SubFramePanel = new SubPanelPastApp().returnPanel();

                    BackButton.addActionListener((ActionEvent e) -> {
                        closeSubFrame();
                    });

                    SubFrame.getContentPane().add(SubFramePanel, BorderLayout.CENTER);
                    SubFrame.getContentPane().add(new JPanel(), BorderLayout.NORTH);
                    SubFrame.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
                    SubFrame.getContentPane().add(new JPanel(), BorderLayout.EAST);
                    SubFrame.getContentPane().add(new JPanel(), BorderLayout.WEST);
                }

                private class SubPanelPastApp {

                    public SubPanelPastApp() {
                        tempPanel = new JPanel();
                        gl = new GroupLayout(tempPanel);
                        tempPanel.setLayout(gl);

                        InitCompPastApp();
                        LayoutPastApp();

                        // [CustName, TechName, AppCata, Req Data, AppIsue, FinDate, Cost, TechFeed, CustFeed, ID]
                        VACustomerNameField.setText(SelectedDatas[0]);
                        VATechnicianNameField.setText(SelectedDatas[1]);
                        VAApplianceCatagoryField.setText(SelectedDatas[2]);
                        VARequestDateField.setText(SelectedDatas[3]);
                        VAApplianceIssueField.setText(SelectedDatas[4]);
                        VAFinishDateField.setText(SelectedDatas[5]);
                        VACostField.setText(SelectedDatas[6]);
                        VATechnicianFeedbackField.setText(SelectedDatas[7]);
                        VACustomerFeedbackField.setText(SelectedDatas[8]);

                    }

                    public JPanel returnPanel() {
                        return tempPanel;
                    }

                    private void InitCompPastApp() {
                        ViewingTitle = new JLabel();
                        ViewingTitle.setFont(DefaultFont);
                        ViewingTitle.setHorizontalAlignment(JLabel.CENTER);

                        VATechnicianNameLabel = new JLabel("Technician Name");
                        VATechnicianNameLabel.setFont(LabelFont);

                        VACustomerNameLabel = new JLabel("Customer Name");
                        VACustomerNameLabel.setFont(LabelFont);

                        VAApplianceCatagoryLabel = new JLabel("Catagory");
                        VAApplianceCatagoryLabel.setFont(LabelFont);

                        VAApplianceIssueLabel = new JLabel("Appliance");
                        VAApplianceIssueLabel.setFont(LabelFont);

                        VARequestDateLabel = new JLabel("Request Date");
                        VARequestDateLabel.setFont(LabelFont);

                        VAFinishDateLabel = new JLabel("Finish Date");
                        VAFinishDateLabel.setFont(LabelFont);

                        VACostLabel = new JLabel("Price");
                        VACostLabel.setFont(LabelFont);

                        VACustomerFeedbackLabel = new JLabel("Customer Feedback");
                        VACustomerFeedbackLabel.setFont(LabelFont);

                        VATechnicianFeedbackLabel = new JLabel("Technician Feedback");
                        VATechnicianFeedbackLabel.setFont(LabelFont);

                        VATechnicianNameField = new JTextField();
                        VATechnicianNameField.setFont(TextFont);
                        VATechnicianNameField.setMargin(FieldInsets);
                        VATechnicianNameField.setEditable(false);

                        VACustomerNameField = new JTextField();
                        VACustomerNameField.setFont(TextFont);
                        VACustomerNameField.setMargin(FieldInsets);
                        VACustomerNameField.setEditable(false);

                        VAApplianceCatagoryField = new JTextField();
                        VAApplianceCatagoryField.setFont(TextFont);
                        VAApplianceCatagoryField.setMargin(FieldInsets);
                        VAApplianceCatagoryField.setEditable(false);

                        VAApplianceIssueField = new JTextField();
                        VAApplianceIssueField.setFont(TextFont);
                        VAApplianceIssueField.setMargin(FieldInsets);
                        VAApplianceIssueField.setEditable(false);

                        VARequestDateField = new JTextField();
                        VARequestDateField.setFont(TextFont);
                        VARequestDateField.setMargin(FieldInsets);
                        VARequestDateField.setEditable(false);

                        VAFinishDateField = new JTextField();
                        VAFinishDateField.setFont(TextFont);
                        VAFinishDateField.setMargin(FieldInsets);
                        VAFinishDateField.setEditable(false);

                        VACostField = new JTextField();
                        VACostField.setFont(TextFont);
                        VACostField.setMargin(FieldInsets);
                        VACostField.setEditable(false);

                        VATechnicianFeedbackField = new JTextField();
                        VATechnicianFeedbackField.setFont(TextFont);
                        VATechnicianFeedbackField.setMargin(FieldInsets);
                        VATechnicianFeedbackField.setEditable(false);

                        VACustomerFeedbackField = new JTextField();
                        VACustomerFeedbackField.setFont(TextFont);
                        VACustomerFeedbackField.setMargin(FieldInsets);
                        VACustomerFeedbackField.setEditable(false);

                        BackButton = new JButton("Back");
                        BackButton.setBackground(ButtonColor);
                        BackButton.setFont(ButtonFont);
                        BackButton.setFocusPainted(false);
                    }

                    private void LayoutPastApp() {
                        ViewingTitle.setText("Viewing Pass Appointment");

                        gl.setVerticalGroup(gl.createSequentialGroup()
                                .addComponent(ViewingTitle, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(gl.createSequentialGroup()
                                                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(gl.createSequentialGroup()
                                                                .addComponent(VATechnicianNameLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10)
                                                                .addComponent(VATechnicianNameField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                        )
                                                        .addGroup(gl.createSequentialGroup()
                                                                .addComponent(VACustomerNameLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10)
                                                                .addComponent(VACustomerNameField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                        )
                                                )
                                                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(gl.createSequentialGroup()
                                                                .addComponent(VAApplianceCatagoryLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10)
                                                                .addComponent(VAApplianceCatagoryField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                        )
                                                        .addGroup(gl.createSequentialGroup()
                                                                .addComponent(VAApplianceIssueLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10)
                                                                .addComponent(VAApplianceIssueField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                        )
                                                )
                                                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(gl.createSequentialGroup()
                                                                .addComponent(VARequestDateLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10)
                                                                .addComponent(VARequestDateField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                        )
                                                        .addGroup(gl.createSequentialGroup()
                                                                .addComponent(VAFinishDateLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10)
                                                                .addComponent(VAFinishDateField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                        )
                                                )
                                                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(gl.createSequentialGroup()
                                                                .addComponent(VACostLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10)
                                                                .addComponent(VACostField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                        )
                                                )
                                        )
                                        .addGroup(gl.createSequentialGroup()
                                                .addGroup(gl.createSequentialGroup()
                                                        .addComponent(VATechnicianFeedbackLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(10)
                                                        .addComponent(VATechnicianFeedbackField, GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
                                                )
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                                                .addGroup(gl.createSequentialGroup()
                                                        .addComponent(VACustomerFeedbackLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(10)
                                                        .addComponent(VACustomerFeedbackField, GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
                                                )
                                        )
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, GroupLayout.PREFERRED_SIZE)
                                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                )
                        );

                        gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(ViewingTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addGroup(gl.createSequentialGroup()
                                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(VATechnicianNameLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VATechnicianNameField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VAApplianceCatagoryLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VAApplianceCatagoryField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VARequestDateLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VARequestDateField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VACostLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VACostField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(VACustomerNameLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VACustomerNameField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VAApplianceIssueLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VAApplianceIssueField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VAFinishDateLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VAFinishDateField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(VATechnicianFeedbackLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VATechnicianFeedbackField, GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE)
                                                .addComponent(VACustomerFeedbackLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(VACustomerFeedbackField, GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE)
                                        )
                                )
                                .addGroup(gl.createSequentialGroup()
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                                        .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                                )
                        );
                    }

                    private JPanel tempPanel;
                    private GroupLayout gl;
                }

                public void showFrame() {
                    SubFrame.setVisible(true);
                }

                public void closeSubFrame() {
                    SubFrame.setVisible(false);
                    SubFrame.dispose();
                    LoginGUI.AdminGUI.frame.setVisible(true);
                }

                public JFrame SubFrame;

                private JLabel ViewingTitle,
                        VATechnicianNameLabel, VACustomerNameLabel, VARequestDateLabel,
                        VAApplianceCatagoryLabel, VAFinishDateLabel, VAApplianceIssueLabel,
                        VACostLabel, VATechnicianFeedbackLabel, VACustomerFeedbackLabel;

                private JTextField VATechnicianNameField, VACustomerNameField, VARequestDateField,
                        VAApplianceCatagoryField, VAFinishDateField, VAApplianceIssueField,
                        VACostField, VATechnicianFeedbackField, VACustomerFeedbackField;

                private JButton BackButton;

            }

            private void InitCompPastApp() {
                MenuName = new JLabel("Appointment Status");
                MenuName.setFont(DefaultFont);
                MenuName.setHorizontalAlignment(JLabel.CENTER);

                BackButton = new JButton("Back");
                BackButton.setBackground(ButtonColor);
                BackButton.setFont(ButtonFont);
                BackButton.setFocusPainted(false);

                DetailsButton = new JButton("View Details");
                DetailsButton.setBackground(ButtonColor);
                DetailsButton.setFont(ButtonFont);
                DetailsButton.setFocusPainted(false);

                PastAppointmentsTable = new JTable(GetDatasForTablePassAppointment(), ColName);
                selector = PastAppointmentsTable.getSelectionModel();
                selector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                PastAppointmentsTable.setShowGrid(false);
                PastAppointmentsTable.setCellSelectionEnabled(false);
                PastAppointmentsTable.setRowSelectionAllowed(true);
                PastAppointmentsTable.setDefaultEditor(Object.class, null);
                PastAppointmentsTable.setFocusable(false);
                PastAppointmentsTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 20));
                PastAppointmentsTable.setFont(new Font("Serif", Font.PLAIN, 20));
                PastAppointmentsTable.setRowHeight(50);

                scp = new JScrollPane(PastAppointmentsTable);
            }

            private void LayoutOpenPassAppUI() {
                gl.setVerticalGroup(gl.createSequentialGroup()
                        .addGap(10)
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                .addComponent(MenuName, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(10)
                        .addComponent(scp, GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
                        .addGap(10)
                        .addComponent(DetailsButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                );

                gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl.createSequentialGroup()
                                .addGap(10)
                                .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addComponent(MenuName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(gl.createSequentialGroup()
                                .addGap(10)
                                .addComponent(scp, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addGap(10)
                        )
                        .addGroup(gl.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                                .addComponent(DetailsButton, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                        )
                );
            }

            private JPanel tempPanel;

            private JLabel MenuName;
            private JButton BackButton, DetailsButton;
            private JTable PastAppointmentsTable;
            private ListSelectionModel selector;
            private JScrollPane scp;

            private String[] ColName = {"Customer Name", "Technician Name", "Appliance Catagory", "Request Date"};
            private int SelectedAppID;
            private String[] SelectedDatas = new String[10]; // [ID, CustName, AppCata, AppIssu, TechName, ReqsDate, FinDate, Cost, TechFeed, CustFeed]

        }

    }

    public JPanel returnPanel() {
        return tempPanel;
    }

}
