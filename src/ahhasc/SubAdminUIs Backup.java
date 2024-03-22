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
import javax.swing.border.EmptyBorder;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class SubAdminUIs {
    static JPanel RequestedPanel;
    
    static int[] CreateAppointmentToday = {LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()};
    
    public JPanel RequestPanel(String requests){
        try{
            switch(requests){
                case "Accounts":
                    RequestedPanel = new JPanelAccounts().Accounts();
                    return RequestedPanel;
                default:
                    throw new Exception();
            }
        } catch (Exception e){
            System.out.println("Got issue in overloading requestpanel (0)");
            return null;
        }
    }
    
    public JPanel RequestPanel(String requests, String subGUIName) {
        try{
            switch(requests){
                case "Appointments":
                    RequestedPanel = new JPanelAppointments().Appointments(subGUIName);
                    return RequestedPanel;
                default:
                    throw new Exception();
            }
        }catch (Exception e){
            System.out.println("Got issue in overloading requestpanel (1)");
            return null;
        }
    }
    
    private void GoBackDashboard(){
        LoginGUI.AdminGUI.ReturnDashboard();
    }
    
    class JPanelAccounts{     
        final String[] ColName = {"USERNAME", "PASSWORD", "ROLE"};
        String[][] Datas;
        
        public void CreateTableData(){
            int UsersSize = DataIO.allUsers.size();
            Datas = new String[UsersSize][3];
            for(int i = 0; i < UsersSize; i++){
                Users User = DataIO.allUsers.get(i);
                Datas[i][0] = User.getName();
                Datas[i][1] = User.getPassword();

                // Turn the int of the role into names;
                switch(User.getRole()){
                    case 0:
                        Datas[i][2] = "Admin";
                        break;
                    case 1:
                        Datas[i][2] = "Technician";
                        break;
                    case 10:
                        Datas[i][2] = "Customer";
                        break;
                    default:
                        Datas[i][2] = "Null";
                }
            }
            AllAccounts = new JTable(Datas, ColName);
        }        
       
        
        /****************
        * Account Panel
        *****************/
        JPanel Contents = new JPanel();
        JButton BackButton, AddAccount, EditAccount, DeleteAccount;
        JLabel MenuName;
        JTable AllAccounts;
        JScrollPane scp;
        ListSelectionModel select;
        
        public JPanel Accounts(){
            GroupLayout gl = new GroupLayout(Contents);
            Contents.setLayout(gl);

            BackButton = new JButton("Back");
            BackButton.setBackground(ButtonColor);
            BackButton.setFont(ButtonFont);
            BackButton.setFocusPainted(false);
            BackButton.addActionListener((ActionEvent e) ->{
                GoBackDashboard();
            });

            MenuName = new JLabel("All Accounts");
            MenuName.setFont(DefaultFont);
            MenuName.setHorizontalAlignment(JLabel.CENTER);

            AddAccount = new JButton("Add Account");
            AddAccount.setBackground(ButtonColor);
            AddAccount.setFont(ButtonFont);
            AddAccount.setFocusPainted(false);
            AddAccount.addActionListener((ActionEvent e) ->{
                AddAccountPressed();
            });

            EditAccount = new JButton("Edit Account");
            EditAccount.setBackground(ButtonColor);
            EditAccount.setFont(ButtonFont);
            EditAccount.setFocusPainted(false);
            EditAccount.addActionListener((ActionEvent e) -> {
                EditAccountPressed();
            });

            DeleteAccount = new JButton("Delete Account");
            DeleteAccount.setBackground(ButtonColor);
            DeleteAccount.setFont(ButtonFont);
            DeleteAccount.setFocusPainted(false);
            DeleteAccount.addActionListener((ActionEvent e) ->{
                DeleteAccountPressed();
            });

            /*******************************
            * Creation of JTable
            ********************************/
            

            CreateTableData();
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
    //                    .addComponent(AllAccounts.getTableHeader(), GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
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

            return Contents;
        }
        
        /**********************************
        * Responsible for buttons Functions
        ***********************************/
        JFrame PopUpBox;
                
        String[] Roles = {"Admin", "Technician", "Customer"};
        
        JTextField NameField = new JTextField();
        JPasswordField PasswordField = new JPasswordField();
        JComboBox RolesBox = new JComboBox(Roles);
        public JButton ConfirmAddAccount = new JButton();
        public JButton ConfirmEditAccount = new JButton();
        public JButton ConfirmDeleteAccount = new JButton();
        
        public static final int ADDACCOUNT_CONFIRM = -1;
        public static final int EDITACCOUNT_CONFIRM = -2;
        public static final int DELETEACCOUNT_CONFIRM = -3;
        
        void PopUpBoxInit(String FrameName, int i){
            PopUpBox.setTitle(FrameName);
            PopUpBox.setSize(600,300);
            PopUpBox.setLocation(400,300);
            PopUpBox.setResizable(false);
            PopUpBox.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            JPanel Panel = new JPanel();
            Panel.setLayout(new GridLayout(4,2));
            Panel.setBorder(new EmptyBorder(10,10,10,10));
            
            
            
            JLabel Name = new JLabel("Name");
            Name.setFont(new Font("Serif", Font.BOLD, 20));

            
            JLabel Password = new JLabel("Password");
            Password.setBorder(new EmptyBorder(10,5,10,10));
            Password.setFont(new Font("Serif", Font.BOLD, 20));

            
            JLabel Role = new JLabel("Role");
            Role.setBorder(new EmptyBorder(10,5,10,10));
            Role.setFont(new Font("Serif", Font.BOLD, 20));

            NameField.setMargin(new Insets(0,5,0,0));
            NameField.setFont(new Font("Serif", Font.PLAIN, 20));
            
            PasswordField.setMargin(new Insets(0,5,0,0));
            NameField.setFont(new Font("Serif", Font.PLAIN, 20));
            
            RolesBox.setFont(new Font("Serif", Font.PLAIN, 20));
            
            JButton Cancel = new JButton("Cancel");
            Cancel.setFont(ButtonFont);
            Cancel.setBackground(ButtonColor);
            Cancel.setFocusPainted(false);
            Cancel.addActionListener((ActionEvent e) ->{
                if(e.getSource() == Cancel){
                    int exitPopUpBox = JOptionPane.showConfirmDialog(null, "Are you sure you want exit?", "Are You Sure?", JOptionPane.YES_NO_OPTION);
                    if(exitPopUpBox == 0){
                        NameField.setText("");
                        PasswordField.setText("");
                        PopUpBox.dispose();
                        PopUpBox = null;
                    }
                }
            });
            
            Panel.add(Name);
            Panel.add(NameField);
            Panel.add(Password);
            Panel.add(PasswordField);
            Panel.add(Role);
            Panel.add(RolesBox);
            Panel.add(Cancel);
            
            switch(i){
                case -1:
                    ConfirmAddAccount.setFont(ButtonFont);
                    ConfirmAddAccount.setBackground(ButtonColor);
                    ConfirmAddAccount.setFocusPainted(false);
                    ConfirmAddAccount.setText("Add to List");
                    Panel.add(ConfirmAddAccount);
                    break;
                case -2:
                    ConfirmEditAccount.setFont(ButtonFont);
                    ConfirmEditAccount.setBackground(ButtonColor);
                    ConfirmEditAccount.setFocusPainted(false);
                    ConfirmEditAccount.setText("Update List");
                    Panel.add(ConfirmEditAccount);
                    break;
                case -3:
                    ConfirmDeleteAccount.setFont(ButtonFont);
                    ConfirmDeleteAccount.setBackground(ButtonColor);
                    ConfirmDeleteAccount.setFocusPainted(false);
                    ConfirmDeleteAccount.setText("Delete Account");
                    Panel.add(ConfirmDeleteAccount);
                    break;

            }
            
            PopUpBox.getContentPane().add(Panel);
            
        }
                
//        @SuppressWarnings("StringEquality")
        private void AddAccountPressed(){
            if(PopUpBox == null){
                System.out.println("make window");
                PopUpBox = new JFrame();
                PopUpBoxInit("Add Account", ADDACCOUNT_CONFIRM);
            }
            
            ConfirmAddAccount.addActionListener((ActionEvent e) -> {
                try{
                    String Name = NameField.getText();
                    String Password = String.valueOf(PasswordField.getPassword());
                    String Role = RolesBox.getSelectedItem().toString();
                    int RoleInt = -1;

                    switch(Role){
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

                    if(Name.isEmpty() || Password.isEmpty()){
                        JOptionPane.showMessageDialog(null, "Username/Password Cannot be Empty");
                        throw new Exception();
                    }

                    for(int i = 0; i < DataIO.allUsers.size(); i++){
                        if(DataIO.allUsers.get(i).getName().equals(Name)){
                            JOptionPane.showMessageDialog(null, "This User has been Registered Before");
                            throw new Exception();
                        }
                    }

                    DataIO.allUsers.add(new Users(Name, Password, RoleInt));
                    DataIO.writeUser();
                    JOptionPane.showMessageDialog(null, "Input Successfully");
                    NameField.setText("");
                    PasswordField.setText("");
                    PopUpBox.dispose();
                    PopUpBox = null;
                    LoginGUI.AdminGUI.OpenAccounts();
                }catch(Exception err){}
            });
            
            PopUpBox.setVisible(true);
            
        }
        
        private void EditAccountPressed(){          
            String Name = null;
            String Password = null;
            String Role = null;
            int[] row = AllAccounts.getSelectedRows();
            for(int i = 0; i < row.length; i++){
                Name = (String)AllAccounts.getValueAt(row[i], 0);
                Password = (String)AllAccounts.getValueAt(row[i], 1);
                Role = (String)AllAccounts.getValueAt(row[i], 2);
            }
            
            try{
                // check if user got selected a row
                if(Name == null || Password == null || Role == null){
                    JOptionPane.showMessageDialog(null, "No Row Selected");
                    throw new Exception();
                }
                
                NameField.setText(Name);
                PasswordField.setText(Password);
                switch(Role){
                    case "Admin":
                        RolesBox.setSelectedIndex(0);
                        break;
                    case "Technician":
                        RolesBox.setSelectedIndex(1);
                        break;
                    case "Customer":
                        RolesBox.setSelectedIndex(2);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Issue with Roles");
                        throw new Exception();
                }
                
                //if selected a row, then only create the new window
                if(PopUpBox == null){
                    System.out.println("make window");
                    PopUpBox = new JFrame();
                    PopUpBoxInit("Edit Account",EDITACCOUNT_CONFIRM);
                }
                PopUpBox.setVisible(true);
            }catch(Exception err){
//                System.out.println(err);
            }
            
            final String NameOld = Name;
            final String PasswordOld = Password;
            final String RoleOld = Role;
            
            ConfirmEditAccount.addActionListener((ActionEvent e) -> {
                try{
                    String NameNew = NameField.getText();
                    String PasswordNew = String.valueOf(PasswordField.getPassword());
                    String RoleNew = RolesBox.getSelectedItem().toString();
                    
                    if(NameNew.equals(NameOld) && PasswordNew.equals(PasswordOld) && RoleNew.equals(RoleOld)){
                        int Confirmation = JOptionPane.showConfirmDialog(null, "No datas has been change. \nThis will not update the database. \nAre You sure?","Confirm Update Empty Data", JOptionPane.YES_NO_OPTION);
                        if(Confirmation == 0){
                            JOptionPane.showMessageDialog(null, "No data has been Updated");
                            NameField.setText("");
                            PasswordField.setText("");
                            PopUpBox.dispose();
                            PopUpBox = null;
                            LoginGUI.AdminGUI.OpenAccounts();
                        }
                    }else{
                        for(int i = 0; i < DataIO.allUsers.size(); i++){
                            if(DataIO.allUsers.get(i).getName().equals(NameOld)){
                                DataIO.allUsers.remove(i);
                            }
                        }
                        
                        int RoleInt;
                        switch(RoleNew){
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
                        NameField.setText("");
                        PasswordField.setText("");
                        PopUpBox.dispose();
                        PopUpBox = null;
                        LoginGUI.AdminGUI.OpenAccounts();
                    }
                }catch(Exception err){}
            });
            
            
        }
        
        private void DeleteAccountPressed(){
            String Name = null;
            String Password = null;
            String Role = null;
            int[] row = AllAccounts.getSelectedRows();
            for(int i = 0; i < row.length; i++){
                Name = (String)AllAccounts.getValueAt(row[i], 0);
                Password = (String)AllAccounts.getValueAt(row[i], 1);
                Role = (String)AllAccounts.getValueAt(row[i], 2);
            }
            
            // check if user got selected a row
            try{
                if(Name == null || Password == null || Role == null){
                    JOptionPane.showMessageDialog(null, "No Row Selected");
                    throw new Exception();
                }
                
                NameField.setText(Name);
                PasswordField.setText(Password);
                switch(Role){
                    case "Admin":
                        RolesBox.setSelectedIndex(0);
                        break;
                    case "Technician":
                        RolesBox.setSelectedIndex(1);
                        break;
                    case "Customer":
                        RolesBox.setSelectedIndex(2);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Issue with Roles");
                        throw new Exception();
                }
                
                if(PopUpBox == null){
                    System.out.println("make window");
                    PopUpBox = new JFrame();
                    PopUpBoxInit("Delete Account",DELETEACCOUNT_CONFIRM);
                }
                PopUpBox.setVisible(true);
                
                NameField.setEditable(false);
                PasswordField.setEditable(false);
                
                RolesBox.setEnabled(false);
                RolesBox.setRenderer(new DefaultListCellRenderer(){
                    @Override
                    public void paint(Graphics g){
                        setForeground(Color.BLACK);
                        super.paint(g);
                    }
                });
            }catch(Exception err){
//                System.out.println(err);
            }
                          
           
            ConfirmDeleteAccount.addActionListener((ActionEvent e) ->{
                JLabel DeleteTextFormat = new JLabel(String.format("<html>Do you want to delete this user?<br>Name: %s<br>Password: %s<br>Role: %s</html>",NameField.getText(), String.valueOf(PasswordField.getPassword()), RolesBox.getSelectedItem().toString()));
                DeleteTextFormat.setFont(new Font("Serif", Font.PLAIN, 20));
                int DeleteConfirmation = JOptionPane.showConfirmDialog(null, DeleteTextFormat, "Are You Sure?", JOptionPane.YES_NO_OPTION);
                // the user confirm for the 1st time
                if(DeleteConfirmation == 0){
                    JLabel FinalConfirmText = new JLabel("<html>Are You Sure<br>There is no way to reverse this action!</html>");
                    FinalConfirmText.setFont(new Font("Serif", Font.PLAIN, 20));
                    int FinalConfirmation = JOptionPane.showConfirmDialog(null, FinalConfirmText, "Final Confirm",JOptionPane.YES_NO_OPTION);
                    // the user confirm for the final timee
                    if(FinalConfirmation == 0){
                        for(int i = 0; i < DataIO.allUsers.size(); i++){
                            if(DataIO.allUsers.get(i).getName().equals(NameField.getText())){
                                DataIO.allUsers.remove(i);
                            }
                        }
                        DataIO.writeUser();
                        JOptionPane.showMessageDialog(null, "Account Delete Successfully");
                        NameField.setText("");
                        PasswordField.setText("");
                        PopUpBox.dispose();
                        PopUpBox = null;
                        LoginGUI.AdminGUI.OpenAccounts();
                    }else{
                        JOptionPane.showMessageDialog(null, "Action Cancelled");
                        NameField.setText("");
                        PasswordField.setText("");
                        PopUpBox.dispose();
                        PopUpBox = null;
                        LoginGUI.AdminGUI.OpenAccounts();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Action Cancelled");
                    NameField.setText("");
                    PasswordField.setText("");
                    PopUpBox.dispose();
                    PopUpBox = null;
                    LoginGUI.AdminGUI.OpenAccounts();
                }
            });
        }
    }
    
    class JPanelAppointments{
        JPanel Contents = new JPanel();
        JFrame SubFrame;
        JPanel SubContents;
        JButton BackButton;
        
        public void ResetSubContentsPanel(){
            SubContents = null;
            SubContents = new JPanel();
        }
        
        public JPanel Appointments(String SwitchSubUI){
            Contents.setLayout(new BorderLayout());
            
            BackButton = new JButton("Back");
            BackButton.setBackground(ButtonColor);
            BackButton.setFocusPainted(false);
            BackButton.setFont(ButtonFont);
            
            switch(SwitchSubUI){
                case "CreateAppointment":
                    System.out.println("Create appointment");
                    ResetSubContentsPanel();
                    CreateAppointmentMenu(SubContents);
                    BackButton.addActionListener((ActionEvent e) -> {
                        LoginGUI.AdminGUI.OpenAppointments();
                    });
                    break;
                default:
                    System.out.println("Main main");
                    ResetSubContentsPanel();
                    AppointmentsMenu(SubContents);
                    break;
            }
            
            Contents.add(SubContents,BorderLayout.CENTER);
            return Contents;
        }
        
        public JPanel AppointmentsMenu(JPanel Panel){
            JLabel MenuName;
            JButton AllAppointments, RequestedAppointments, InProgressAppointments, FinishedAppointments, CreateAppointments;
            
            GroupLayout gl = new GroupLayout(Panel);
            Panel.setLayout(gl);
            
            
            MenuName = new JLabel("Appointment Manager");
            MenuName.setFont(DefaultFont);
            MenuName.setHorizontalAlignment(JLabel.CENTER);
            
            
            AllAppointments = new JButton("Appointments");
            RequestedAppointments = new JButton("Requested Appointments");
            InProgressAppointments = new JButton("In Progress Appointments");
            FinishedAppointments = new JButton("Finish Appointments");
            CreateAppointments = new JButton("Create Appointments");
            
            AllAppointments.setBackground(ButtonColor);
            RequestedAppointments.setBackground(ButtonColor);
            InProgressAppointments.setBackground(ButtonColor);
            FinishedAppointments.setBackground(ButtonColor);
            CreateAppointments.setBackground(ButtonColor);
            
            AllAppointments.setFocusPainted(false);
            RequestedAppointments.setFocusPainted(false);
            InProgressAppointments.setFocusPainted(false);
            FinishedAppointments.setFocusPainted(false);
            CreateAppointments.setFocusPainted(false);
            
            AllAppointments.setFont(ButtonFont);
            RequestedAppointments.setFont(ButtonFont);
            InProgressAppointments.setFont(ButtonFont);
            FinishedAppointments.setFont(ButtonFont);
            CreateAppointments.setFont(ButtonFont);
            
            BackButton.addActionListener((ActionEvent e) -> {
                GoBackDashboard();
            });
            
            CreateAppointments.addActionListener((ActionEvent e) -> {
                LoginGUI.AdminGUI.OpenAppointmentsSub("CreateAppointment");
            });
            
            gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(10)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addComponent(MenuName, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                )
                .addGap(50)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(AllAppointments, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    .addComponent(RequestedAppointments, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(50)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(InProgressAppointments, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    .addComponent(FinishedAppointments, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(50)
                .addComponent(CreateAppointments,GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
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
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 60, GroupLayout.PREFERRED_SIZE)
                    .addComponent(RequestedAppointments, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                )
                .addGroup(gl.createSequentialGroup()
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(InProgressAppointments, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 60, GroupLayout.PREFERRED_SIZE)
                    .addComponent(FinishedAppointments, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                )
                .addGroup(gl.createSequentialGroup()
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(CreateAppointments, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                )
            );
            
            return Panel;
        }
        
        private void CustomerTechnicianSelectionFrame(int level){
            JButton BackButton, ConfirmButton;
            JList UsersList;
            DefaultListModel listName;
            ArrayList<String> AllName = new ArrayList<String>();
            
            SubFrame = new JFrame();
            SubFrame.setSize(600,300);
            SubFrame.setResizable(false);
//            SubFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            SubFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            SubFrame.setLayout(new BorderLayout());
            
            listName = new DefaultListModel<>();
            
            switch(level){
                case 0:
                    // technician
                    SubFrame.setTitle("Select technician");
                    AllName = AHHASC.Admin.getAllTechnicianName();
                    break;
                    
                case 1:
                    // Customer
                    SubFrame.setTitle("Select Customer");
                    AllName = AHHASC.Admin.getAllCustomerName();
                    break;
                    
                default:
                    System.out.println("Error in SubAdminUIs Sub Frame in Creating Appointments");
            }
            
            for (int i = 0; i<AllName.size(); i++){
                listName.addElement(AllName.get(i));
            }
            
            UsersList = new JList<>(listName);
            UsersList.setFont(TextFont);
            
            BackButton = new JButton("Back");
            BackButton.setBackground(ButtonColor);
            BackButton.setFont(TextFont);
            BackButton.setFocusPainted(false);
            BackButton.addActionListener((ActionEvent e) ->{
                SubFrame.setVisible(false);
                SubFrame.dispose();
            });
            
            ConfirmButton = new JButton("Confirm");
            ConfirmButton.setBackground(ButtonColor);
            ConfirmButton.setFont(TextFont);
            ConfirmButton.setFocusPainted(false);
            ConfirmButton.addActionListener((ActionEvent e)->{
                if(level == 0){
                    TechnicianName.setText(UsersList.getSelectedValue().toString());
                    SubFrame.setVisible(false);
                    SubFrame.dispose();
                }
                
                if(level == 1){
                    CustomerName.setText(UsersList.getSelectedValue().toString());
                    SubFrame.setVisible(false);
                    SubFrame.dispose();
                    SubFrame = null;
                }
            });
            
            JPanel BottomPanel = new JPanel();
            BottomPanel.add(BackButton);
            BottomPanel.add(ConfirmButton);
            
            SubFrame.add(new JPanel(),BorderLayout.NORTH);
            SubFrame.add(new JPanel(),BorderLayout.EAST);
            SubFrame.add(new JPanel(),BorderLayout.WEST);
            SubFrame.add(BottomPanel, BorderLayout.SOUTH);
            SubFrame.add(UsersList, BorderLayout.CENTER);
        }
        
        JTextField CustomerName, TechnicianName;
        
        public void CreateAppointmentMenu(JPanel Panel){
            GroupLayout gl = new GroupLayout(Panel);
            Panel.setLayout(gl);
            
            UtilDateModel model;
            Properties p;
            JDatePanelImpl datePanel;
            JDatePickerImpl datePicker;
            JLabel CreateAppointmentMenuText;
            JComboBox ApplianceLists;
            JTextField ApplianceIssue;
            JButton CustomerSelection, TechnicianSelection, ComfirmCreateAppointment;
            String[] ApplianceCatagory = {"TV", "Fridge", "Electronics"};
            
            CreateAppointmentMenuText = new JLabel("Create Appointment");
            CreateAppointmentMenuText.setFont(DefaultFont);
            CreateAppointmentMenuText.setHorizontalAlignment(JLabel.CENTER);
            
            CustomerName = new JTextField();
                TechnicianName = new JTextField();
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

                model.setDate(CreateAppointmentToday[0], CreateAppointmentToday[1], CreateAppointmentToday[2]);
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
        
        public void ViewAllAppointmentMenu(JPanel Panel){
            GroupLayout gl = new GroupLayout(Panel);
            Panel.setLayout(gl);
            
            gl.setVerticalGroup(gl.createSequentialGroup());
            
            gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING));
        }
    }
}
