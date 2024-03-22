package ahhasc;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;

public class SubCustomerGUIs {
    JPanel RequestedPanel = new JPanel();
    
    public static final int VIEW_APPOINTMENTS = 1;
    public static final int PAST_APPOINTMENTS = 2;
    
    public static final int SUBFRAME_VIEW_APPOINTMENT = 1;
    public static final int SUBFRAME_PAST_APPOINTMENT = 2;
    
    static Font DefaultFont = new Font("Serif", Font.BOLD, 50);
    static Color ButtonColor = new Color(242,242,242);
    static Font ButtonFont = new Font("Serif", Font.PLAIN, 20);
    static Font TextFont = new Font("Serif", Font.PLAIN, 25);
    static Font LabelFont = new Font("Serif", Font.BOLD, 20);
    static Insets FieldInsets = new Insets(0,5,0,0);
    
    static Appointments AS;
    static Appointments PA;
    
    
    public SubCustomerGUIs(){}
    
    public JPanel getAppStatus(){
        AS = new Appointments(VIEW_APPOINTMENTS);
        return AS.RequestPanel();
    }
    
    public JPanel getPastApp(){
        PA = new Appointments(PAST_APPOINTMENTS);
        return PA.RequestPanel();
    }
    
    public String[][] GetDataForTableFeedbackLessAppointment(){
        ArrayList<Appointment> CustAppointment = AHHASC.Customer.getAllFeedbackLessAppointment();
        int AppointmentSize = CustAppointment.size();
        String[][] Datas = new String[AppointmentSize][8]; // [ID, AppCata, AppIssue, FinDate, Cost, TechName, ReqsDate, TechFeed]
        
        for(int i = 0; i < AppointmentSize; i++){
            Appointment appointment = CustAppointment.get(i);
            
            Datas[i][0] = String.valueOf(appointment.getAppointmentID());
            Datas[i][1] = appointment.getApplianceCatagory();
            Datas[i][2] = appointment.getApplianceIssue();
            Datas[i][3] = appointment.getFinishDate().toString();
            Datas[i][4] = String.valueOf(appointment.getCost());
            Datas[i][5] = appointment.getTechnicianName();
            Datas[i][6] = appointment.getRequestedDate().toString();
            Datas[i][7] = appointment.getTechnicianFeedback();
        }
        
        return Datas;
    }
    
    public String[][] GetDataForTablePassAppointment(){
        ArrayList<Appointment> CustAppointment = AHHASC.Customer.getAllPassAppointment();
        int AppointmentSize = CustAppointment.size();
        String[][] Datas = new String[AppointmentSize][9]; // [ID, AppCata, AppIssue, FinDate, Cost, TechName, ReqsDate, TechFeed, CustFeed]
        
        for(int i = 0; i < AppointmentSize; i++){
            Appointment appointment = CustAppointment.get(i);
            
            Datas[i][0] = String.valueOf(appointment.getAppointmentID());
            Datas[i][1] = appointment.getApplianceCatagory();
            Datas[i][2] = appointment.getApplianceIssue();
            Datas[i][3] = appointment.getFinishDate().toString();
            Datas[i][4] = String.valueOf(appointment.getCost());
            Datas[i][5] = appointment.getTechnicianName();
            Datas[i][6] = appointment.getRequestedDate().toString();
            Datas[i][7] = appointment.getTechnicianFeedback();
            Datas[i][8] = appointment.getCustomerFeedback();
        }
        
        return Datas;
    }
    
    public void GoBackDashboard(){
        LoginGUI.CustomerGUI.ReturnDashboard();
    }
    
    public SubFrameCustomer getSubFrame(int i){
        SubFrameCustomer SF = new SubFrameCustomer(i);
        return SF;
    }
}

class Appointments extends SubCustomerGUIs{
    private JPanel RequestedPanel;
    public static ViewApp VA;
    public static PastApp PA;
    
    public Appointments(int i){
//        try{
            RequestedPanel = new JPanel();
            
            switch(i){
                case VIEW_APPOINTMENTS: // view appoins not yet feedback
                    VA = new ViewApp();
                    RequestedPanel = VA.returnPanel();
                    break;
                    
                case PAST_APPOINTMENTS: // view past appoints
                    PA = new PastApp();
                    RequestedPanel = PA.returnPanel();
                    break;
            }            
//        }catch(Exception err){
//            System.out.println(err);
//        }
        
    }
    
    // ui for show all the unfeedback-ed appointments
    private class ViewApp{
        public ViewApp(){
            tempPanel = new JPanel();
            gl = new GroupLayout(tempPanel);
            tempPanel.setLayout(gl);
            
            initCompounentsViewAppointment();
            LayoutViewAppointment();
            
            DetailsButton.addActionListener((ActionEvent e) -> {
                try{
                    if(SelectedAppointmentID == 0){
                        JOptionPane.showMessageDialog(null, "No Row Selected");
                        throw new Exception();
                    }

                    SubFrameCustomer SB = getSubFrame(SUBFRAME_VIEW_APPOINTMENT);
                    SB.showFrame();
                    LoginGUI.CustomerGUI.frame.setVisible(false);
                }catch(Exception err){
                    System.out.println("appointments inside switch case 1 -SubCusGUIs " +err);
                }
            });
            
            BackButton.addActionListener((ActionEvent e) -> {
                GoBackDashboard();
            });
            
            AppointmentsTable.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    int[] row = AppointmentsTable.getSelectedRows();
                    for(int i = 0; i < row.length; i++){
                        SelectedAppointmentID = Integer.parseInt(AppointmentsTable.getModel().getValueAt(row[i], 0).toString());
                    }
                }   
            });

        }
        
        public void initCompounentsViewAppointment(){
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

            AppointmentsTable = new JTable(GetDataForTableFeedbackLessAppointment(), ColName);
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
    
        public void LayoutViewAppointment(){
            gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(10)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    .addComponent(MenuName, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(10)
                .addComponent(scp,GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
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
        
        public JPanel returnPanel(){
            return tempPanel;
        }
        
        private String[] getSelectedData(){
            String[] temp = new String[8]; 

            // Datas: [ID, AppCata, AppIssue, FinDate, Cost, TechName, ReqsDate, TechFeed]
            // temp: [TechName, AppCata, AppIssue, ReqsDate, FinDate, Cost, TechFeed]

            int[] row = AppointmentsTable.getSelectedRows();

            for(int i = 0; i< row.length; i++){
                temp[0] = (String) AppointmentsTable.getModel().getValueAt(row[i], 5);
                temp[1] = (String) AppointmentsTable.getModel().getValueAt(row[i], 2);
                temp[2] = (String) AppointmentsTable.getModel().getValueAt(row[i], 3);
                temp[3] = (String) AppointmentsTable.getModel().getValueAt(row[i], 6);
                temp[4] = (String) AppointmentsTable.getModel().getValueAt(row[i], 3);
                temp[5] = (String) AppointmentsTable.getModel().getValueAt(row[i], 4);
                temp[6] = (String) AppointmentsTable.getModel().getValueAt(row[i], 7);
                temp[7] = (String) AppointmentsTable.getModel().getValueAt(row[i], 0);
            }

            return temp;
        }
        
        private JPanel tempPanel;
        private GroupLayout gl;
        private JButton BackButton, DetailsButton;
        private JLabel MenuName;

        private JTable AppointmentsTable;
        private JScrollPane scp;
        private ListSelectionModel selector;
        
        private String[] ColName = {"ID", "Catagory", "Item", "Finish Date", "Cost"};
        private int SelectedAppointmentID = 0;
    }
    
    // ui for show all the past appointments
    private class PastApp{
        public PastApp(){
            tempPanel = new JPanel();
            gl = new GroupLayout(tempPanel);
            tempPanel.setLayout(gl);
            
            initCompounentsPastApp();
            LayoutPastApp();
            
            DetailsButton.addActionListener((ActionEvent e) -> {
                try{
                    if(SelectedAppointmentID == 0){
                        JOptionPane.showMessageDialog(null, "No Row Selected");
                        throw new Exception();
                    }

                    SubFrameCustomer SB = getSubFrame(SUBFRAME_PAST_APPOINTMENT);
                    SB.showFrame();
                    LoginGUI.CustomerGUI.frame.setVisible(false);
                }catch(Exception err){
                    System.out.println("appointments inside switch case 2 -SubCustGUIs" + err);
                }
            });
            
            BackButton.addActionListener((ActionEvent e) -> {
                GoBackDashboard();
            });
            
            AppointmentsTable.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    int[] row = AppointmentsTable.getSelectedRows();
                    for(int i = 0; i < row.length; i++){
                        SelectedAppointmentID = Integer.parseInt(AppointmentsTable.getModel().getValueAt(row[i], 0).toString());
                    }
                }   
            });
        }
        
        public void initCompounentsPastApp(){
            MenuName = new JLabel("Past Appointment Status");
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

            AppointmentsTable = new JTable(GetDataForTablePassAppointment(), ColName);
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
    
        public void LayoutPastApp(){
            gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(10)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    .addComponent(MenuName, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(10)
                .addComponent(scp,GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
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
        
        public JPanel returnPanel(){
            return tempPanel;
        }
        
        private String[] getSelectedData(){
            String[] temp = new String[9]; 

            // Datas: [ID, AppCata, AppIssue, FinDate, Cost, TechName, ReqsDate, TechFeed, CustFeed]
            // temp: [TechName, AppCata, AppIssue, ReqsDate, FinDate, Cost, TechFeed, CustFeed]

            int[] row = AppointmentsTable.getSelectedRows();

            for(int i = 0; i< row.length; i++){
                temp[0] = (String) AppointmentsTable.getModel().getValueAt(row[i], 5);
                temp[1] = (String) AppointmentsTable.getModel().getValueAt(row[i], 2);
                temp[2] = (String) AppointmentsTable.getModel().getValueAt(row[i], 3);
                temp[3] = (String) AppointmentsTable.getModel().getValueAt(row[i], 6);
                temp[4] = (String) AppointmentsTable.getModel().getValueAt(row[i], 3);
                temp[5] = (String) AppointmentsTable.getModel().getValueAt(row[i], 4);
                temp[6] = (String) AppointmentsTable.getModel().getValueAt(row[i], 7);
                temp[7] = (String) AppointmentsTable.getModel().getValueAt(row[i], 0);
                temp[8] = (String) AppointmentsTable.getModel().getValueAt(row[i], 8);
            }

            return temp;
        }
        
        private JPanel tempPanel;
        private GroupLayout gl;
        private JButton BackButton, DetailsButton;
        private JLabel MenuName;

        private JTable AppointmentsTable;
        private JScrollPane scp;
        private ListSelectionModel selector;
        
        private String[] ColName = {"ID", "Catagory", "Item", "Finish Date", "Cost"};
        private int SelectedAppointmentID = 0;
    }
    
    // return the panel to the frame
    public JPanel RequestPanel(){
        return RequestedPanel;
    }
    
    public String[] getAllSelectedDatasForFeedback(){ 
        return VA.getSelectedData();
    }
    
    public String[] getAllSelcetedDataForPast(){
        return PA.getSelectedData();
    }
}

// this sub frame can work as starting job / checking appointment in more detail
// the admin and customer will have a different ver of this
class SubFrameCustomer extends SubCustomerGUIs{
    private JFrame SubFrame;
    
    private class SubFrameFeedback{
        public SubFrameFeedback(){
            temp = new JPanel();
            gl = new GroupLayout(temp);
            temp.setLayout(gl);
            
            INITCompFeedback();
            LayoutFeedback();
        
            // temp: [TechName, AppCata, AppIssue, ReqsDate, FinDate, Cost, TechFeed, ID]
            TechnicianNameField.setText(Datas[0]);
            ApplianceCatagoryField.setText(Datas[1]);
            ApplianceIssueField.setText(Datas[2]);
            RequestDateField.setText(Datas[3]);
            FinishDateField.setText(Datas[4]);
            CostField.setText(Datas[5]);
            TechnicianFeedbackField.setText(Datas[6]);
            ViewingTitle.setText(String.format("Viewing on %s’s Appointment", Datas[7]));
            
            FeedbackButton.addActionListener((ActionEvent e) ->{
                UpdateAppointment();
            });

            BackButton.addActionListener((ActionEvent e)->{
                closeSubFrame();
            });
            
        }
        
        private void UpdateAppointment(){
            try{
                int ID = Integer.parseInt(Datas[7]);
                String Feedback = CustomerFeedbackField.getText();

                AHHASC.Customer.updateAppointent(ID, Feedback);
                JOptionPane.showMessageDialog(null, new JLabel("Feedback Submited"));
                System.out.println("Debug: Close Frame");
                closeSubFrame();
                LoginGUI.CustomerGUI.ReturnDashboard();
                LoginGUI.CustomerGUI.OpenAppStatus();
            }catch(Exception err){
                System.out.println("Issue in SubCustomerGUIS - UpdateAppintment");
                System.out.println(err);
            }
        }
        
        private void INITCompFeedback(){
            ViewingTitle = new JLabel();
            ViewingTitle.setFont(DefaultFont);
            ViewingTitle.setHorizontalAlignment(JLabel.CENTER);

            TechnicianNameLabel = new JLabel("Technician Name");
            TechnicianNameLabel.setFont(LabelFont);

            ApplianceCatagoryLabel = new JLabel("Catagory");
            ApplianceCatagoryLabel.setFont(LabelFont);

            ApplianceIssueLabel = new JLabel("Appliance");
            ApplianceIssueLabel.setFont(LabelFont);

            RequestDateLabel = new JLabel("Request Date");
            RequestDateLabel.setFont(LabelFont);

            FinishDateLabel = new JLabel("Finish Date");
            FinishDateLabel.setFont(LabelFont);

            CostLabel = new JLabel("Price");
            CostLabel.setFont(LabelFont);

            CustomerFeedbackLabel = new JLabel("Customer Feedback");
            CustomerFeedbackLabel.setFont(LabelFont);

            TechnicianFeedbackLabel = new JLabel("Technician Feedback");
            TechnicianFeedbackLabel.setFont(LabelFont);

            TechnicianNameField = new JTextField();
            TechnicianNameField.setFont(TextFont);
            TechnicianNameField.setMargin(FieldInsets);
            TechnicianNameField.setEditable(false);

            ApplianceCatagoryField = new JTextField();
            ApplianceCatagoryField.setFont(TextFont);
            ApplianceCatagoryField.setMargin(FieldInsets);
            ApplianceCatagoryField.setEditable(false);

            ApplianceIssueField = new JTextField();
            ApplianceIssueField.setFont(TextFont);
            ApplianceIssueField.setMargin(FieldInsets);
            ApplianceIssueField.setEditable(false);

            RequestDateField = new JTextField();
            RequestDateField.setFont(TextFont);
            RequestDateField.setMargin(FieldInsets);
            RequestDateField.setEditable(false);

            FinishDateField = new JTextField();
            FinishDateField.setFont(TextFont);
            FinishDateField.setMargin(FieldInsets);
            FinishDateField.setEditable(false);

            CostField = new JTextField();
            CostField.setFont(TextFont);
            CostField.setMargin(FieldInsets);
            CostField.setEditable(false);

            TechnicianFeedbackField = new JTextField();
            TechnicianFeedbackField.setFont(TextFont);
            TechnicianFeedbackField.setMargin(FieldInsets);
            TechnicianFeedbackField.setEditable(false);

            CustomerFeedbackField = new JTextField();
            CustomerFeedbackField.setFont(TextFont);
            CustomerFeedbackField.setMargin(FieldInsets);

            FeedbackButton = new JButton("Feedback");
            FeedbackButton.setBackground(ButtonColor);
            FeedbackButton.setFont(ButtonFont);
            FeedbackButton.setFocusPainted(false);

            BackButton = new JButton("Back");
            BackButton.setBackground(ButtonColor);
            BackButton.setFont(ButtonFont);
            BackButton.setFocusPainted(false);
        }
        
        private JPanel LayoutFeedback(){
            gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(ViewingTitle, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(TechnicianNameLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(TechnicianNameField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(RequestDateLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(RequestDateField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                        )
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(ApplianceCatagoryLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(ApplianceCatagoryField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(FinishDateLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(FinishDateField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                        )
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(ApplianceIssueLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(ApplianceIssueField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(CostLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(CostField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                        )
                    )
                    .addGroup(gl.createSequentialGroup()
                        .addGroup(gl.createSequentialGroup()
                            .addComponent(TechnicianFeedbackLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                            .addGap(10)
                            .addComponent(TechnicianFeedbackField, GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                        .addGroup(gl.createSequentialGroup()
                            .addComponent(CustomerFeedbackLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                            .addGap(10)
                            .addComponent(CustomerFeedbackField, GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
                        )  
                    )
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, GroupLayout.PREFERRED_SIZE)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    .addComponent(FeedbackButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                )
            );

            gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(ViewingTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                .addGroup(gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(TechnicianNameLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(TechnicianNameField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addComponent(ApplianceCatagoryLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(ApplianceCatagoryField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addComponent(ApplianceIssueLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(ApplianceIssueField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(RequestDateLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(RequestDateField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addComponent(FinishDateLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(FinishDateField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addComponent(CostLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(CostField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(TechnicianFeedbackLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(TechnicianFeedbackField, GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE)
                        .addComponent(CustomerFeedbackLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(CustomerFeedbackField, GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE)
                    )
                )
                .addGroup(gl.createSequentialGroup()
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                    .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(FeedbackButton, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                )
            );

            return temp;
        }
        
        public JPanel returnPanel(){
            return temp;
        }
        
        private String[] Datas = AS.getAllSelectedDatasForFeedback();
        
        private JPanel temp;
        private GroupLayout gl;
        
        private JTextField 
            TechnicianNameField, 
            ApplianceCatagoryField,
            ApplianceIssueField, 
            RequestDateField, 
            FinishDateField,
            CostField, 
            CustomerFeedbackField,
            TechnicianFeedbackField;
    
        private JLabel 
            TechnicianNameLabel, 
            ApplianceCatagoryLabel, 
            ApplianceIssueLabel,
            RequestDateLabel, 
            FinishDateLabel, 
            CostLabel,
            CustomerFeedbackLabel, 
            TechnicianFeedbackLabel;

        private JLabel ViewingTitle;
        private JButton FeedbackButton, BackButton;
    }
    
    private class SubFramePastApp{
        public SubFramePastApp(){
            temp = new JPanel();
            gl = new GroupLayout(temp);
            temp.setLayout(gl);
            
            INITCompPastApp();
            LayoutPastApp();
            
            TechnicianNameField.setText(Datas[0]);
            ApplianceCatagoryField.setText(Datas[1]);
            ApplianceIssueField.setText(Datas[2]);
            RequestDateField.setText(Datas[3]);
            FinishDateField.setText(Datas[4]);
            CostField.setText(Datas[5]);
            TechnicianFeedbackField.setText(Datas[6]);
            CustomerFeedbackField.setText(Datas[8]);
            
            BackButton.addActionListener((ActionEvent e)->{
                closeSubFrame();
            });
        }
        
        private void INITCompPastApp(){
            ViewingTitle = new JLabel();
            ViewingTitle.setFont(DefaultFont);
            ViewingTitle.setHorizontalAlignment(JLabel.CENTER);

            TechnicianNameLabel = new JLabel("Technician Name");
            TechnicianNameLabel.setFont(LabelFont);

            ApplianceCatagoryLabel = new JLabel("Catagory");
            ApplianceCatagoryLabel.setFont(LabelFont);

            ApplianceIssueLabel = new JLabel("Appliance");
            ApplianceIssueLabel.setFont(LabelFont);

            RequestDateLabel = new JLabel("Request Date");
            RequestDateLabel.setFont(LabelFont);

            FinishDateLabel = new JLabel("Finish Date");
            FinishDateLabel.setFont(LabelFont);

            CostLabel = new JLabel("Price");
            CostLabel.setFont(LabelFont);

            CustomerFeedbackLabel = new JLabel("Customer Feedback");
            CustomerFeedbackLabel.setFont(LabelFont);

            TechnicianFeedbackLabel = new JLabel("Technician Feedback");
            TechnicianFeedbackLabel.setFont(LabelFont);

            TechnicianNameField = new JTextField();
            TechnicianNameField.setFont(TextFont);
            TechnicianNameField.setMargin(FieldInsets);
            TechnicianNameField.setEditable(false);

            ApplianceCatagoryField = new JTextField();
            ApplianceCatagoryField.setFont(TextFont);
            ApplianceCatagoryField.setMargin(FieldInsets);
            ApplianceCatagoryField.setEditable(false);

            ApplianceIssueField = new JTextField();
            ApplianceIssueField.setFont(TextFont);
            ApplianceIssueField.setMargin(FieldInsets);
            ApplianceIssueField.setEditable(false);

            RequestDateField = new JTextField();
            RequestDateField.setFont(TextFont);
            RequestDateField.setMargin(FieldInsets);
            RequestDateField.setEditable(false);

            FinishDateField = new JTextField();
            FinishDateField.setFont(TextFont);
            FinishDateField.setMargin(FieldInsets);
            FinishDateField.setEditable(false);

            CostField = new JTextField();
            CostField.setFont(TextFont);
            CostField.setMargin(FieldInsets);
            CostField.setEditable(false);

            TechnicianFeedbackField = new JTextField();
            TechnicianFeedbackField.setFont(TextFont);
            TechnicianFeedbackField.setMargin(FieldInsets);
            TechnicianFeedbackField.setEditable(false);

            CustomerFeedbackField = new JTextField();
            CustomerFeedbackField.setFont(TextFont);
            CustomerFeedbackField.setMargin(FieldInsets);
            CustomerFeedbackField.setEditable(false);

            BackButton = new JButton("Back");
            BackButton.setBackground(ButtonColor);
            BackButton.setFont(ButtonFont);
            BackButton.setFocusPainted(false);
        }
        
        private void LayoutPastApp(){
            gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(ViewingTitle, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(TechnicianNameLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(TechnicianNameField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(RequestDateLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(RequestDateField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                        )
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(ApplianceCatagoryLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(ApplianceCatagoryField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(FinishDateLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(FinishDateField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                        )
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(ApplianceIssueLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(ApplianceIssueField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGroup(gl.createSequentialGroup()
                                .addComponent(CostLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(CostField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                            )
                        )
                    )
                    .addGroup(gl.createSequentialGroup()
                        .addGroup(gl.createSequentialGroup()
                            .addComponent(TechnicianFeedbackLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                            .addGap(10)
                            .addComponent(TechnicianFeedbackField, GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                        .addGroup(gl.createSequentialGroup()
                            .addComponent(CustomerFeedbackLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                            .addGap(10)
                            .addComponent(CustomerFeedbackField, GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
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
                        .addComponent(TechnicianNameLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(TechnicianNameField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addComponent(ApplianceCatagoryLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(ApplianceCatagoryField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addComponent(ApplianceIssueLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(ApplianceIssueField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(RequestDateLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(RequestDateField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addComponent(FinishDateLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(FinishDateField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addComponent(CostLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(CostField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, GroupLayout.PREFERRED_SIZE)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(TechnicianFeedbackLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(TechnicianFeedbackField, GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE)
                        .addComponent(CustomerFeedbackLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(CustomerFeedbackField, GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE)
                    )
                )
                .addGroup(gl.createSequentialGroup()
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                    .addComponent(BackButton, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                )
            );

        }
        
        private String[] Datas = PA.getAllSelcetedDataForPast();
        
        
        public JPanel requestPanel(){
            return temp;
        }
        
        private JPanel temp;
        private GroupLayout gl;
        
        private JTextField 
            TechnicianNameField, 
            ApplianceCatagoryField,
            ApplianceIssueField, 
            RequestDateField, 
            FinishDateField,
            CostField, 
            CustomerFeedbackField,
            TechnicianFeedbackField;
    
        private JLabel 
            TechnicianNameLabel, 
            ApplianceCatagoryLabel, 
            ApplianceIssueLabel,
            RequestDateLabel, 
            FinishDateLabel, 
            CostLabel,
            CustomerFeedbackLabel, 
            TechnicianFeedbackLabel;

        private JLabel ViewingTitle;
        private JButton BackButton;
        
    }
    
    public SubFrameCustomer(int i){
        SubFrame = new JFrame();
        SubFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        SubFrame.setSize(1200,800);
        SubFrame.setLayout(new BorderLayout());
        SubFrame.setResizable(false);
        JPanel SubJPanel = new JPanel();

        switch (i){
            case SUBFRAME_VIEW_APPOINTMENT: // Viewing on Appointments
                SubFrame.setTitle("Viewing Appointment Details");
                SubFrameFeedback SFFeedback = new SubFrameFeedback();
                SubJPanel = SFFeedback.returnPanel();
                break;
                
            case SUBFRAME_PAST_APPOINTMENT: // Viewing on Past Appointments
                SubFrame.setTitle("Viewing Past Appointment");
                SubFramePastApp SFPast = new SubFramePastApp();
                SubJPanel = SFPast.requestPanel();
                break;
            default:
                System.out.println("notiing");
        }
            
        try{
            if(SubJPanel == null){
                throw new Exception();
            }
            
            SubFrame.add(SubJPanel, BorderLayout.CENTER);
            SubFrame.add(new JPanel(), BorderLayout.NORTH);
            SubFrame.add(new JPanel(), BorderLayout.SOUTH);
            SubFrame.add(new JPanel(), BorderLayout.EAST);
            SubFrame.add(new JPanel(), BorderLayout.WEST);
            
        } catch (Exception err){
            System.out.println("Error in adding Sub JPanel into SubFrame (SubCustomerGUIS - SubFrame) ");
        }
    }
    
    public void showFrame(){
        System.out.println("Show frame");
        SubFrame.setVisible(true);
    }
    
    private void closeSubFrame(){
        SubFrame.setVisible(false);
        SubFrame.dispose();
        LoginGUI.CustomerGUI.frame.setVisible(true);
    }
}
