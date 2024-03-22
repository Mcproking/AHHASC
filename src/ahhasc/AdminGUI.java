package ahhasc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.border.EmptyBorder;


public class AdminGUI {
    JPanel RequestedPanel;

    
    public void RefreshFrame(){
        System.out.println("refreshed");
        frame.revalidate();
        frame.repaint();
    }
    
    public void ReturnDashboard(){
        frame.getContentPane().removeAll();
        frame.getContentPane().add(Header, BorderLayout.NORTH);
        frame.getContentPane().add(Contents, BorderLayout.CENTER);
        RefreshFrame();
    }
    
    public void addPanelBorder(){
        frame.getContentPane().add(new JPanel(), BorderLayout.NORTH);
        frame.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
        frame.getContentPane().add(new JPanel(), BorderLayout.EAST);
        frame.getContentPane().add(new JPanel(), BorderLayout.WEST);
    }
    
    public void doRequstPanel(JPanel Reqs){
        frame.getContentPane().removeAll();
        frame.getContentPane().add(Reqs, BorderLayout.CENTER);
        addPanelBorder();
        RefreshFrame();
    }
    
    public void OpenAppointments(){
        RequestedPanel = new SubAdminGUIs().getAppointmentPanel();
        
        frame.getContentPane().removeAll();
        frame.getContentPane().add(RequestedPanel, BorderLayout.CENTER);
        addPanelBorder();
        RefreshFrame();
    }
    
    public void OpenAccounts(){
        RequestedPanel = new SubAdminGUIs().getAccountPanel();
        
        frame.getContentPane().removeAll();
        frame.getContentPane().add(RequestedPanel, BorderLayout.CENTER);
        RefreshFrame();
    }
    
    public void Logout(){
        frame.setVisible(false);
        AHHASC.Admin = null;
        AHHASC.LoginGUI.frame.setVisible(true);
        AHHASC.LoginGUI.frame.toFront();
        AHHASC.LoginGUI.frame.repaint();
        AHHASC.LoginGUI.destoryObjects();
    }
    
    public JFrame frame;
    JLabel HeaderText, HeaderImage,WelcomeText;
    JButton AppointmentButton, AccountsButton, FeedbacksButton, InvoiceButton,LogButton, LogoutButton;
    JPanel Header, Contents;
    
    Font DefaultFont;
    Color ButtonColor;
    
    
    
    public AdminGUI(){
        
        frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Debug Purposes
        frame.setSize(1000,650);
        frame.setResizable(false);
        frame.setLocation(250,80);
        frame.setLayout(new BorderLayout());
        
        DefaultFont = new Font("Serif", Font.BOLD, 50);
        ButtonColor = new Color(242,242,242);
        
/************************************************************************
 *  This Section of the JPanel is to create the header of the frame
 ***********************************************************************/
        Header = new JPanel();
        GroupLayout HeaderGL = new GroupLayout(Header);
        Header.setLayout(HeaderGL);
        
        HeaderText = new JLabel("Admin Dashboard");
        HeaderText.setFont(new Font("Serif", Font.BOLD, 40));
        HeaderText.setBorder(new EmptyBorder(0,10,0,0));
        
        HeaderImage = new JLabel();
        HeaderImage.setIcon(new ImageIcon(getClass().getResource("/ahhasc/imgs/AdminBG.jpg")));
        
        HeaderGL.setVerticalGroup(HeaderGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
            .addComponent(HeaderText,GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
            .addComponent(HeaderImage, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
        );
        
        HeaderGL.setHorizontalGroup(HeaderGL.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(HeaderText,GroupLayout.PREFERRED_SIZE, 500, Short.MAX_VALUE)
            .addComponent(HeaderImage, GroupLayout.PREFERRED_SIZE, 500, Short.MAX_VALUE)
        );
        
        
 /************************************************************************
 *  This Section of the JPanel is to create the contents of the frame
 ***********************************************************************/       
        Contents = new JPanel();
        GroupLayout ContentsGL = new GroupLayout(Contents);
        Contents.setLayout(ContentsGL);
        
        WelcomeText = new JLabel();
        String name = null;
        // check the varible got admin class or not
        try{
            name = AHHASC.Admin.getName();
        }
        catch(Exception e)
        {
            System.out.println("Null Poniter");
        } 
        WelcomeText.setText(String.format("Welcome %s, What Would You Want To Do Today?", name));
        WelcomeText.setFont(new Font("Serif", Font.PLAIN, 24));
        WelcomeText.setBorder(new EmptyBorder(10,10,0,0));
        
        
        AppointmentButton = new JButton("Appointment"); 
        AppointmentButton.setFont(DefaultFont);
        AppointmentButton.setBackground(ButtonColor);
        AppointmentButton.setFocusPainted(false);
        
        AccountsButton = new JButton("Accounts");
        AccountsButton.setFont(DefaultFont);
        AccountsButton.setBackground(ButtonColor);
        AccountsButton.setFocusPainted(false);
        
        FeedbacksButton = new JButton ("Feedbacks");
        FeedbacksButton.setFont(DefaultFont);
        FeedbacksButton.setBackground(ButtonColor);
        FeedbacksButton.setFocusPainted(false);
        
        InvoiceButton = new JButton("Invoice");
        InvoiceButton.setFont(DefaultFont);
        InvoiceButton.setBackground(ButtonColor);
        InvoiceButton.setFocusPainted(false);
        
        LogButton = new JButton("Log");
        LogButton.setFont(DefaultFont);
        LogButton.setBackground(ButtonColor);
        LogButton.setFocusPainted(false);
        
        LogoutButton = new JButton("Logout");
        LogoutButton.setFont(DefaultFont);
        LogoutButton.setBackground(ButtonColor);
        LogoutButton.setFocusPainted(false);
        
        AppointmentButton.addActionListener((ActionEvent e) -> {
            OpenAppointments();
        });
        
        AccountsButton.addActionListener((ActionEvent e)-> {
            OpenAccounts();
        });
        
        
        LogoutButton.addActionListener((ActionEvent e) -> {
            Logout();
        });
        
        ContentsGL.setVerticalGroup(ContentsGL.createSequentialGroup()
            .addComponent(WelcomeText,GroupLayout.PREFERRED_SIZE, 70,GroupLayout.PREFERRED_SIZE)
            .addGroup(ContentsGL.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(ContentsGL.createSequentialGroup()
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,GroupLayout.PREFERRED_SIZE,30)
                    .addComponent(AppointmentButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                )
                .addGroup(ContentsGL.createSequentialGroup()
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.PREFERRED_SIZE, 30)
                    .addComponent(AccountsButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                )
            )
            .addGroup(ContentsGL.createSequentialGroup()
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.PREFERRED_SIZE, 30)
                .addComponent(LogoutButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
            )
        );
        
        ContentsGL.setHorizontalGroup(ContentsGL.createSequentialGroup()
            .addGroup(ContentsGL.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(WelcomeText, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                
                .addGroup(ContentsGL.createSequentialGroup()
                    .addGap(60)
                    .addComponent(AppointmentButton, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addGap(60)
                    .addComponent(AccountsButton, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addGap(60))
                .addGroup(ContentsGL.createSequentialGroup()
                    .addGap(60)
                    .addComponent(LogoutButton, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addGap(60))
            ));
        
        frame.getContentPane().add(Header, BorderLayout.NORTH);
        frame.getContentPane().add(Contents, BorderLayout.CENTER);
        

//        OpenAppointments();// debug Purposes
//        frame.setVisible(true); // debug purposes
    }
}
