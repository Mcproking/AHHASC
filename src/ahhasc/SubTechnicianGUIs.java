package ahhasc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;

public class SubTechnicianGUIs {

    JPanel RequestedPanel = new JPanel();

    public static final int WORKING = 0;
    public static final int PAST_APPOINTMENT = 2;
    public static final int SETTING = 10;

    public static final int SUBFRAME_WORKING = 0;
    public static final int SUBFRAME_PAST_APPOINTMENT = 2;

    static Font DefaultFont = new Font("Serif", Font.BOLD, 50);
    static Color ButtonColor = new Color(242, 242, 242);
    static Font ButtonFont = new Font("Serif", Font.PLAIN, 20);
    static Font TextFont = new Font("Serif", Font.PLAIN, 25);
    static Font LabelFont = new Font("Serif", Font.BOLD, 20);
    static Insets FieldInsets = new Insets(0, 5, 0, 0);

    static SubUI VA;
    static SubUI ST;
    static SubUI PA;

    public SubTechnicianGUIs() {
    }

    public String[][] GetDataForTable() {
        ArrayList<Appointment> TechAppointment = AHHASC.Technician.getAllAppointment();
        int AppointmentSize = TechAppointment.size();
        String[][] Datas = new String[AppointmentSize][5];

        for (int i = 0; i < AppointmentSize; i++) {
            Appointment appointment = TechAppointment.get(i);

            Datas[i][0] = String.valueOf(appointment.getAppointmentID());
            Datas[i][1] = appointment.getCustomerName();
            Datas[i][2] = appointment.getApplianceCatagory();
            Datas[i][3] = appointment.getRequestedDate().toString();
            Datas[i][4] = appointment.getApplianceIssue();
        }

        return Datas;
    }

    public String[][] GetDatasForTablePassAppointment() {
        ArrayList<Appointment> TechPastAppointment = AHHASC.Technician.getAllPassAppointment();
        int AppointmentSize = TechPastAppointment.size();

        String[][] Datas = new String[AppointmentSize][10]; // [ID, CustName, AppCata, reqsdate , TechName, AppIssu, FinDate, Cost, TechFeed, CustFeed]

        for (int i = 0; i < AppointmentSize; i++) {
            Appointment appointment = TechPastAppointment.get(i);

            Datas[i][0] = String.valueOf(appointment.getAppointmentID());
            Datas[i][1] = appointment.getCustomerName();
            Datas[i][2] = appointment.getApplianceCatagory();
            Datas[i][3] = appointment.getRequestedDate().toString();
            Datas[i][4] = appointment.getApplianceIssue();
            Datas[i][5] = appointment.getTechnicianName();
            Datas[i][6] = appointment.getFinishDate().toString();
            Datas[i][7] = String.valueOf(appointment.getCost());
            Datas[i][8] = appointment.getTechnicianFeedback();
            Datas[i][9] = appointment.getCustomerFeedback();
        }

        return Datas;
    }

    public void GoBackDashboard() {
        LoginGUI.TechnicianGUI.ReturnDashboard();
    }

    public void ForceLogout() {
        LoginGUI.TechnicianGUI.Logout();
    }

    public JPanel getAppointments() {
        VA = new SubUI(WORKING);
        return VA.ReturnPanel();
    }

    public JPanel getSetting() {
        ST = new SubUI(SETTING);
        return ST.ReturnPanel();
    }

    public JPanel getPastApp() {
        PA = new SubUI(PAST_APPOINTMENT);
        return PA.ReturnPanel();
    }

    public SubFrameTechnician getSubFrame(int i) {
        SubFrameTechnician SF = new SubFrameTechnician(i);
        return SF;
    }
}

class SubUI extends SubTechnicianGUIs {

    private static JPanel RequestedPanel;
    public static SettingUI SU;
    public static AppUI AU;
    public static PassAppUI PAU;

    public SubUI(int i) {
        try {
            RequestedPanel = new JPanel();

            switch (i) {
                case SETTING: // open setting panel
                    SU = new SettingUI();
                    RequestedPanel = SU.returnPanel();
                    break;
                case WORKING: // open panel for need to work on appointment
                    AU = new AppUI();
                    RequestedPanel = AU.returnPanel();
                    break;
                case PAST_APPOINTMENT: // open past appoinment what is done
                    PAU = new PassAppUI();
                    RequestedPanel = PAU.returnPanel();
                    break;
            }
        } catch (Exception err) {
            System.out.println("Issue in SubUI in SubTechnicianGUIs: " + err);
        }
    }

    // class for View Appointment UI
    private class AppUI {

        private JPanel tempPanel;
        private GroupLayout gl;
        private JLabel MenuName, CustomerNameLabel, ApplianceCatagoryLabel, RequestDateLabel, ApplianceIssueLabel;
        private JTextField CustomerNameField, ApplianceCatagoryField, RequestDateField, ApplianceIssueField;
        private JButton BackButton, StartWork;
        private JTable AppointmentsTable;
        private JScrollPane scp;
        private ListSelectionModel selector;

        private String[] ColName = {"ID", "Customer Name", "Appliance Catagory", "Request Date"};
        private int SelectedAppointmentID;
        private String SelectedAppointmentCustomerName;
        private String SelectedAppointmentApplianceCatagory;
        private String SelectedAppointmentRequestDate;
        private String SelectedAppointmentApplianceIssue;

        public AppUI() {
            tempPanel = new JPanel();
            gl = new GroupLayout(tempPanel);
            tempPanel.setLayout(gl);

            initCompounentsOpenApp();
            LayoutOpenApp();

            StartWork.addActionListener((ActionEvent e) -> {
                try {
                    if (SelectedAppointmentID == 0) {
                        JOptionPane.showMessageDialog(null, "No Row Selected");
                        throw new Exception();
                    }
                    SubFrameTechnician SB = getSubFrame(SUBFRAME_WORKING);
                    SB.showFrame();
                    LoginGUI.TechnicianGUI.frame.setVisible(false);
                } catch (Exception err) {
                }
            });

            BackButton.addActionListener((ActionEvent e) -> {
                GoBackDashboard();
            });

            AppointmentsTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int[] row = AppointmentsTable.getSelectedRows();
                    for (int i = 0; i < row.length; i++) {
                        SelectedAppointmentID = Integer.parseInt(AppointmentsTable.getModel().getValueAt(row[i], 0).toString());
                        SelectedAppointmentCustomerName = (String) AppointmentsTable.getValueAt(row[i], 1);
                        SelectedAppointmentApplianceCatagory = (String) AppointmentsTable.getValueAt(row[i], 2);
                        SelectedAppointmentRequestDate = (String) AppointmentsTable.getValueAt(row[i], 3);
                        SelectedAppointmentApplianceIssue = (String) AppointmentsTable.getModel().getValueAt(row[i], 4);

                        CustomerNameField.setText(SelectedAppointmentCustomerName);
                        ApplianceCatagoryField.setText(SelectedAppointmentApplianceCatagory);
                        RequestDateField.setText(SelectedAppointmentRequestDate);
                        ApplianceIssueField.setText(SelectedAppointmentApplianceIssue);
                    }
                }
            });
        }

        private void initCompounentsOpenApp() {
            MenuName = new JLabel("Appointments Menu");
            MenuName.setFont(DefaultFont);
            MenuName.setHorizontalAlignment(JLabel.CENTER);

            BackButton = new JButton("Back");
            BackButton.setBackground(ButtonColor);
            BackButton.setFont(ButtonFont);
            BackButton.setFocusPainted(false);

            CustomerNameLabel = new JLabel("CustomerName");
            CustomerNameLabel.setFont(LabelFont);

            CustomerNameField = new JTextField();
            CustomerNameField.setFont(TextFont);
            CustomerNameField.setMargin(FieldInsets);
            CustomerNameField.setEditable(false);

            ApplianceCatagoryLabel = new JLabel("Appliance Catagory");
            ApplianceCatagoryLabel.setFont(LabelFont);

            ApplianceCatagoryField = new JTextField();
            ApplianceCatagoryField.setFont(TextFont);
            ApplianceCatagoryField.setMargin(FieldInsets);
            ApplianceCatagoryField.setEditable(false);

            RequestDateLabel = new JLabel("Date Requsted");
            RequestDateLabel.setFont(LabelFont);

            RequestDateField = new JTextField();
            RequestDateField.setFont(TextFont);
            RequestDateField.setMargin(FieldInsets);
            RequestDateField.setEditable(false);

            ApplianceIssueLabel = new JLabel("Issue stated");
            ApplianceIssueLabel.setFont(LabelFont);

            ApplianceIssueField = new JTextField();
            ApplianceIssueField.setFont(TextFont);
            ApplianceIssueField.setMargin(FieldInsets);
            ApplianceIssueField.setEditable(false);

            StartWork = new JButton("Start Work");
            StartWork.setBackground(ButtonColor);
            StartWork.setFont(ButtonFont);
            StartWork.setFocusPainted(false);

            AppointmentsTable = new JTable(GetDataForTable(), ColName);
            selector = AppointmentsTable.getSelectionModel();
            selector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            AppointmentsTable.setShowGrid(false);
            AppointmentsTable.setCellSelectionEnabled(false);
            AppointmentsTable.setRowSelectionAllowed(true);
            AppointmentsTable.setDefaultEditor(Object.class, null);
            AppointmentsTable.setFocusable(false);
            AppointmentsTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 20));
            AppointmentsTable.setFont(new Font("Serif", Font.PLAIN, 20));
            AppointmentsTable.setRowHeight(50);

            scp = new JScrollPane(AppointmentsTable);

        }

        private void LayoutOpenApp() {
            gl.setVerticalGroup(gl.createSequentialGroup()
                    .addGap(10)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(MenuName, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGap(10)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                    .addComponent(CustomerNameLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(CustomerNameField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addGap(20)
                                    .addComponent(ApplianceCatagoryLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(ApplianceCatagoryField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addGap(20)
                                    .addComponent(RequestDateLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(RequestDateField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addGap(20)
                                    .addComponent(ApplianceIssueLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(ApplianceIssueField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                            )
                            .addComponent(scp, GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE)
                    )
                    .addGap(10)
                    .addGroup(gl.createSequentialGroup()
                            .addComponent(StartWork, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    )
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
                            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(CustomerNameLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CustomerNameField, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ApplianceCatagoryLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ApplianceCatagoryField, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(RequestDateLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(RequestDateField, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ApplianceIssueLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ApplianceIssueField, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGap(20)
                            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(scp, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                            )
                            .addGap(20)
                    )
                    .addGroup(gl.createSequentialGroup()
                            .addGap(10)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                            .addComponent(StartWork, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                    )
            );
        }

        public JPanel returnPanel() {
            return tempPanel;
        }

        private String[] getSelectedData() {
            String[] temp = new String[5];

            temp[0] = String.valueOf(SelectedAppointmentID);
            temp[1] = SelectedAppointmentCustomerName;
            temp[2] = SelectedAppointmentApplianceCatagory;
            temp[3] = SelectedAppointmentRequestDate;
            temp[4] = SelectedAppointmentApplianceIssue;

            return temp;
        }
    }

    // class for Pass Appointment UI
    private class PassAppUI {

        JPanel tempPanel;
        GroupLayout gl;
        private JLabel MenuName;
        private JButton BackButton, DetailsButton;
        private JTable PastAppointmentsTable;
        private ListSelectionModel selector;
        private JScrollPane scp;

        private String[] ColName = {"ID", "Customer Name", "Appliance Catagory", "Request Date"};
        private int SelectedAppID;
        private String[] SelectedDatas = new String[10]; // [ID, CustName, AppCata, AppIssu, TechName, ReqsDate, FinDate, Cost, TechFeed, CustFeed]

        public PassAppUI() {
            tempPanel = new JPanel();
            gl = new GroupLayout(tempPanel);
            tempPanel.setLayout(gl);

            initCompounnetsPassAppUI();
            LayoutOpenPassAppUI();

            BackButton.addActionListener((ActionEvent e) -> {
                GoBackDashboard();
            });

            DetailsButton.addActionListener((ActionEvent e) -> {
                try {
                    if (SelectedAppID == 0) {
                        JOptionPane.showMessageDialog(null, "No Row Selected");
                        throw new Exception();
                    }

                    SubFrameTechnician SB = getSubFrame(SUBFRAME_PAST_APPOINTMENT);
                    SB.showFrame();
                    LoginGUI.TechnicianGUI.frame.setVisible(false);
                } catch (Exception err) {
                    System.out.println("appointments inside switch case 2 " + err);
                }
            });

            PastAppointmentsTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int[] row = PastAppointmentsTable.getSelectedRows();
                    for (int i = 0; i < row.length; i++) {
                        SelectedAppID = Integer.parseInt(PastAppointmentsTable.getModel().getValueAt(row[i], 0).toString());

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

        private void initCompounnetsPassAppUI() {
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

        public JPanel returnPanel() {
            return tempPanel;
        }

        public String[] getSelectedData() {
            return SelectedDatas;
        }
    }

    // class for Setting UI
    private class SettingUI {

        JPanel tempPanel;
        GroupLayout gl;

        JLabel OldNameLabel, OldPasswordLabel, NewNameLabel, NewPasswordLabel, ConPasswordLabel;
        JTextField OldNameField, NewNameField;
        JPasswordField OldPasswordField, NewPasswordField, ConPasswordField;

        JButton BackButton, UpdateButton;

        JLabel CurrentLabel, NewLabel;

        private String curTechName;
        // show old name, ask enter old pass, then new pass and conPass
        // if old pass correct -> update to new old data to new
        // if old pass worng -> pop up say old pass worng and back to dashbord

        public SettingUI() {
            tempPanel = new JPanel();
            gl = new GroupLayout(tempPanel);
            tempPanel.setLayout(gl);

            initCompounnetsSetting();
            LayoutOpenSetting();

            curTechName = AHHASC.Technician.getName();

            OldNameField.setText(curTechName);

            BackButton.addActionListener((ActionEvent e) -> {
                GoBackDashboard();
            });

            UpdateButton.addActionListener((ActionEvent e) -> {
                UpdateTechData();
            });

        }

        private void initCompounnetsSetting() {
            CurrentLabel = new JLabel("Current");
            CurrentLabel.setFont(DefaultFont);

            NewLabel = new JLabel("New");
            NewLabel.setFont(DefaultFont);

            OldNameLabel = new JLabel("Name");
            OldNameLabel.setFont(LabelFont);

            OldPasswordLabel = new JLabel("Password");
            OldPasswordLabel.setFont(LabelFont);

            NewNameLabel = new JLabel("Name");
            NewNameLabel.setFont(LabelFont);

            NewPasswordLabel = new JLabel("Password");
            NewPasswordLabel.setFont(LabelFont);

            ConPasswordLabel = new JLabel("Confirm Password");
            ConPasswordLabel.setFont(LabelFont);

            OldNameField = new JTextField();
            OldNameField.setFont(TextFont);
            OldNameField.setMargin(FieldInsets);
            OldNameField.setEditable(false);

            NewNameField = new JTextField();
            NewNameField.setFont(TextFont);
            NewNameField.setMargin(FieldInsets);

            OldPasswordField = new JPasswordField();
            OldPasswordField.setMargin(FieldInsets);

            NewPasswordField = new JPasswordField();
            NewPasswordField.setMargin(FieldInsets);

            ConPasswordField = new JPasswordField();
            ConPasswordField.setMargin(FieldInsets);

            BackButton = new JButton("Back");
            BackButton.setBackground(ButtonColor);
            BackButton.setFont(ButtonFont);
            BackButton.setFocusPainted(false);

            UpdateButton = new JButton("Update");
            UpdateButton.setBackground(ButtonColor);
            UpdateButton.setFont(ButtonFont);
            UpdateButton.setFocusPainted(false);

        }

        private void LayoutOpenSetting() {
            gl.setVerticalGroup(gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(CurrentLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                            .addComponent(NewLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                    .addComponent(OldNameLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(OldNameField, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGroup(gl.createSequentialGroup()
                                    .addComponent(NewNameLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(NewNameField, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                            )
                    )
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                    .addComponent(OldPasswordLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(OldPasswordField, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGroup(gl.createSequentialGroup()
                                    .addComponent(NewPasswordLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(NewPasswordField, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                            )
                    )
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                    .addComponent(ConPasswordLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(ConPasswordField, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                            )
                    )
                    .addGap(50)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(UpdateButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    )
            );

            gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(gl.createSequentialGroup()
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(CurrentLabel, GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(OldNameLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(OldNameField, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(OldPasswordLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(OldPasswordField, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                            )
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 60, GroupLayout.PREFERRED_SIZE)
                            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(NewLabel, GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(NewNameLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NewNameField, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NewPasswordLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NewPasswordField, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ConPasswordLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ConPasswordField, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                            )
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                    )
                    .addGroup(gl.createSequentialGroup()
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                            .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 100, GroupLayout.PREFERRED_SIZE)
                            .addComponent(UpdateButton, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                    )
            );
        }

        public JPanel returnPanel() {
            return tempPanel;
        }

        private void UpdateTechData() {
            try {
                String NewName = String.valueOf(NewNameField.getText());
                String OldPass = String.valueOf(OldPasswordField.getPassword());
                String NewPass = String.valueOf(NewPasswordField.getPassword());
                String ConPass = String.valueOf(ConPasswordField.getPassword());

                if (NewPass.isEmpty() || ConPass.isEmpty()) {
                    System.out.println("new pass no empty");
                    throw new Exception();
                }

                if (NewName.isEmpty()) {
                    NewName = curTechName;
                }

                if (AHHASC.Technician.validatePassword(OldPass)) {
                    if (!NewPass.equals(ConPass)) {
                        System.out.println("Confirm Password Incorret");
                        throw new Exception();
                    }

                    AHHASC.Technician.updateAccount(curTechName, NewName, ConPass);
                    JOptionPane.showMessageDialog(null, "Updated Account, Please relog again");
                    System.out.println("Updated Account, relog again");
                    GoBackDashboard();
                    ForceLogout();
                }

            } catch (Exception err) {
                System.out.println("Issue in updating account: SubTech > SubUI > SettingUI > UpdateTechData: " + err);
            }
        }
    }

    public JPanel ReturnPanel() {
        return RequestedPanel;
    }

    public String[] getSelectedDatasFromAU() {
        String[] temp = new String[5]; //temp: [ID, custName, AppCata, ReqData, AppIssue]
        String[] retriveData = AU.getSelectedData();
        temp[0] = String.valueOf(retriveData[0]);
        temp[1] = retriveData[1];
        temp[2] = retriveData[2];
        temp[3] = retriveData[3];
        temp[4] = retriveData[4];

        return temp;
    }

    public String[] getSelectedDataFromPA() {
        return PAU.getSelectedData();
    }
}

// this sub frame can work as starting job / checking appointment in more detail
// the admin and customer will have a different ver of this
class SubFrameTechnician extends SubTechnicianGUIs {

    private JFrame SubFrame;

    //init subframeworking with compounents and layout
    private class SubFrameWorking {

        private JPanel temp;
        private GroupLayout gl;

        private JLabel WorkingTitle, WorkCustomerNameLabel, WorkApplianceCatagoryLabel, WorkApplianceIssueLabel, WorkRequestDateLabel, WorkCostLabel, WorkFeedbackLabel;
        private JButton StopWork, FinishWork;
        private JTextField WorkCustomerNameField, WorkApplianceCatagoryField, WorkApplianceIssueField, WorkRequestDateField, WorkCostField, WorkFeedbackField;

        private String[] Datas = VA.getSelectedDatasFromAU(); // get data from viewAppointment [AppID, CustName, AppCata,ReqDate,AppIss]

        public SubFrameWorking() {
            temp = new JPanel();
            gl = new GroupLayout(temp);
            temp.setLayout(gl);

            INITComponetsWorkingAppointments();
            LayoutWorkingAppointment();

            StopWork.addActionListener((ActionEvent evt) -> {
                closeSubFrame();
            });

            FinishWork.addActionListener((ActionEvent evt) -> {
                UpdateAppointment();
            });
        }

        private void INITComponetsWorkingAppointments() {
            WorkingTitle = new JLabel();
            WorkingTitle.setFont(DefaultFont);
            WorkingTitle.setHorizontalAlignment(JLabel.CENTER);

            StopWork = new JButton("Pause Work");
            StopWork.setBackground(ButtonColor);
            StopWork.setFont(ButtonFont);
            StopWork.setFocusPainted(false);

            FinishWork = new JButton("Finish Work");
            FinishWork.setBackground(ButtonColor);
            FinishWork.setFont(ButtonFont);
            FinishWork.setFocusPainted(false);

            WorkCustomerNameLabel = new JLabel("Customer Name");
            WorkCustomerNameLabel.setFont(LabelFont);

            WorkCustomerNameField = new JTextField();
            WorkCustomerNameField.setFont(TextFont);
            WorkCustomerNameField.setMargin(FieldInsets);
            WorkCustomerNameField.setEditable(false);

            WorkApplianceCatagoryLabel = new JLabel("Appliance Catagory");
            WorkApplianceCatagoryLabel.setFont(LabelFont);

            WorkApplianceCatagoryField = new JTextField();
            WorkApplianceCatagoryField.setFont(TextFont);
            WorkApplianceCatagoryField.setMargin(FieldInsets);
            WorkApplianceCatagoryField.setEditable(false);

            WorkApplianceIssueLabel = new JLabel("Appliance Issue");
            WorkApplianceIssueLabel.setFont(LabelFont);

            WorkApplianceIssueField = new JTextField();
            WorkApplianceIssueField.setFont(TextFont);
            WorkApplianceIssueField.setMargin(FieldInsets);
            WorkApplianceIssueField.setEditable(false);

            WorkRequestDateLabel = new JLabel("Requested Date");
            WorkRequestDateLabel.setFont(LabelFont);

            WorkRequestDateField = new JTextField();
            WorkRequestDateField.setFont(TextFont);
            WorkRequestDateField.setMargin(FieldInsets);
            WorkRequestDateField.setEditable(false);

            WorkCostLabel = new JLabel("Price");
            WorkCostLabel.setFont(LabelFont);

            WorkCostField = new JTextField();
            WorkCostField.setFont(TextFont);
            WorkCostField.setMargin(FieldInsets);

            WorkFeedbackLabel = new JLabel("Feedback");
            WorkFeedbackLabel.setFont(LabelFont);

            WorkFeedbackField = new JTextField();
            WorkFeedbackField.setFont(TextFont);
            WorkFeedbackField.setMargin(FieldInsets);
        }

        private JPanel LayoutWorkingAppointment() {

            String MenuTexts = String.format("Working on %s’s %s Appointment", Datas[1], Datas[0]);
            WorkingTitle.setText(MenuTexts);

            WorkCustomerNameField.setText(Datas[1]);
            WorkApplianceCatagoryField.setText(Datas[2]);
            WorkApplianceIssueField.setText(Datas[4]);
            WorkRequestDateField.setText(Datas[3]);

            gl.setVerticalGroup(gl.createSequentialGroup()
                    .addComponent(WorkingTitle, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(gl.createSequentialGroup()
                                                    .addComponent(WorkCustomerNameLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(10)
                                                    .addComponent(WorkCustomerNameField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                            )
                                            .addGroup(gl.createSequentialGroup()
                                                    .addComponent(WorkRequestDateLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(10)
                                                    .addComponent(WorkRequestDateField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                            )
                                    )
                                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(gl.createSequentialGroup()
                                                    .addComponent(WorkApplianceCatagoryLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(10)
                                                    .addComponent(WorkApplianceCatagoryField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                            )
                                            .addGroup(gl.createSequentialGroup()
                                                    .addComponent(WorkCostLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(10)
                                                    .addComponent(WorkCostField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                            )
                                    )
                                    .addGroup(gl.createSequentialGroup()
                                            .addComponent(WorkApplianceIssueLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                            .addGap(10)
                                            .addComponent(WorkApplianceIssueField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                    )
                            )
                            .addGroup(gl.createSequentialGroup()
                                    .addComponent(WorkFeedbackLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(WorkFeedbackField, GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
                            )
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, GroupLayout.PREFERRED_SIZE)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(StopWork, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(FinishWork, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    )
            );

            gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(WorkingTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                    .addGroup(gl.createSequentialGroup()
                            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(WorkCustomerNameLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(WorkCustomerNameField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(WorkApplianceCatagoryLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(WorkApplianceCatagoryField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(WorkApplianceIssueLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(WorkApplianceIssueField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                            )
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(WorkRequestDateLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(WorkRequestDateField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(WorkCostLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(WorkCostField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                            )
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(WorkFeedbackLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(WorkFeedbackField, GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE)
                            )
                    )
                    .addGroup(gl.createSequentialGroup()
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                            .addComponent(StopWork, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                            .addComponent(FinishWork, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                    )
            );
            return temp;
        }

        public JPanel ReturnPanel() {
            return temp;
        }

        private void closeSubFrame() {
            SubFrame.setVisible(false);
            SubFrame.dispose();
            LoginGUI.TechnicianGUI.frame.setVisible(true);
        }

        private void UpdateAppointment() {
            try {
                int ID = Integer.parseInt(Datas[0]);
                String TodayDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                int price = Integer.parseInt(WorkCostField.getText());
                String Feedback = WorkFeedbackField.getText();

                AHHASC.Technician.updateAppointent(ID, TodayDate, price, Feedback);

                System.out.println("Debug: Close Frame");
                closeSubFrame();
                LoginGUI.TechnicianGUI.ReturnDashboard();
                LoginGUI.TechnicianGUI.OpenAppointments();
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, new JLabel("Price need to be in a Number"));
                System.out.println("Price is not in number form");
            } catch (Exception err) {
                System.out.println("Issue in SubTechnicianGUIS - UpdateAppintment");
            }
        }
    }

    //init this to show the appointment individually
    private class SubFrameViewAppointment {

        private JPanel temp;
        private GroupLayout gl;

        JLabel ViewingTitle,
                VATechnicianNameLabel, VACustomerNameLabel, VARequestDateLabel,
                VAApplianceCatagoryLabel, VAFinishDateLabel, VAApplianceIssueLabel,
                VACostLabel, VATechnicianFeedbackLabel, VACustomerFeedbackLabel;

        JTextField VATechnicianNameField, VACustomerNameField, VARequestDateField,
                VAApplianceCatagoryField, VAFinishDateField, VAApplianceIssueField,
                VACostField, VATechnicianFeedbackField, VACustomerFeedbackField;

        JButton BackButton;

        private String[] Datas = PA.getSelectedDataFromPA(); // [ID, CustName, AppCata, ReqsDate, TechName,appiss, FinDate, Cost, TechFeed, CustFeed]

        public SubFrameViewAppointment() {
            temp = new JPanel();
            gl = new GroupLayout(temp);
            temp.setLayout(gl);

            INITComponetsViewAppointments();
            LayoutViewAppointment();

            BackButton.addActionListener((ActionEvent e) -> {
                closeSubFrame();
            });

            VACustomerNameField.setText(Datas[1]);
            VAApplianceCatagoryField.setText(Datas[2]);
            VARequestDateField.setText(Datas[3]);
            VATechnicianNameField.setText(Datas[4]);
            VAApplianceIssueField.setText(Datas[5]);
            VAFinishDateField.setText(Datas[6]);
            VACostField.setText(Datas[7]);
            VATechnicianFeedbackField.setText(Datas[8]);
            VACustomerFeedbackField.setText(Datas[9]);
        }

        private void INITComponetsViewAppointments() {
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

        private JPanel LayoutViewAppointment() {
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
            return temp;
        }

        public JPanel ReturnPanel() {
            return temp;
        }
    }

    public SubFrameTechnician(int i) {
//        System.out.println("Created subframe");
        SubFrame = new JFrame();
        SubFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        SubFrame.setSize(1200, 800);
        SubFrame.setLayout(new BorderLayout());
        SubFrame.setResizable(false);
        JPanel SubJPanel = new JPanel();

        switch (i) {
            case 0: // Working on Appointment
                SubFrame.setTitle("Working");
                SubFrameWorking SFWorking = new SubFrameWorking();
                SubJPanel = SFWorking.ReturnPanel();
                break;
            case 2: // Viewing on Past Appointments
                SubFrame.setTitle("Viewing Past Appointment");
                SubFrameViewAppointment SFViewAppointment = new SubFrameViewAppointment();
                SubJPanel = SFViewAppointment.ReturnPanel();
                break;
        }

        try {
            if (SubJPanel == null) {
                throw new Exception();
            }

            SubFrame.add(SubJPanel, BorderLayout.CENTER);
            SubFrame.add(new JPanel(), BorderLayout.NORTH);
            SubFrame.add(new JPanel(), BorderLayout.SOUTH);
            SubFrame.add(new JPanel(), BorderLayout.EAST);
            SubFrame.add(new JPanel(), BorderLayout.WEST);

        } catch (Exception err) {
            System.out.println("Error in adding Sub JPanel into SubFrame (SubTechnicianGUIS - SubFrame) ");
        }
    }

    public void showFrame() {
        System.out.println("Show frame");
        SubFrame.setVisible(true);
    }

    private void closeSubFrame() {
        SubFrame.setVisible(false);
        SubFrame.dispose();
        LoginGUI.TechnicianGUI.frame.setVisible(true);
    }

}
