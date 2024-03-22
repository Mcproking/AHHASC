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

public class TechnicianGUI {
    JPanel RequestedPanel;
    
    public void RefreshFrame(){
        System.out.println("refreshed");
        frame.revalidate();
        frame.repaint();
    }
    
    public void addEmptyBorder(){
        frame.getContentPane().add(new JPanel(), BorderLayout.NORTH);
        frame.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
        frame.getContentPane().add(new JPanel(), BorderLayout.EAST);
        frame.getContentPane().add(new JPanel(), BorderLayout.WEST);
    }
    
    public void ReturnDashboard(){
        frame.getContentPane().removeAll();
        frame.getContentPane().add(Header, BorderLayout.NORTH);
        frame.getContentPane().add(Contents, BorderLayout.CENTER);
        RefreshFrame();
    }
    
    public void Logout(){
        frame.setVisible(false);
        AHHASC.Technician = null;
        AHHASC.LoginGUI.frame.setVisible(true);
        AHHASC.LoginGUI.frame.toFront();
        AHHASC.LoginGUI.frame.repaint();
        AHHASC.LoginGUI.destoryObjects();
    }
    
    public void OpenAppointments(){
        RequestedPanel = new SubTechnicianGUIs().getAppointments();
        
        frame.getContentPane().remove(Header);
        frame.getContentPane().remove(Contents);
        frame.getContentPane().add(RequestedPanel);
        RefreshFrame();
    }
    
    public void OpenSetting(){
        RequestedPanel = new SubTechnicianGUIs().getSetting();
        
        frame.getContentPane().removeAll();
        addEmptyBorder();
        frame.getContentPane().add(RequestedPanel, BorderLayout.CENTER);
        RefreshFrame();
    }
    
    public void OpenPastApp(){
        RequestedPanel = new SubTechnicianGUIs().getPastApp();
        
        frame.getContentPane().removeAll();
        addEmptyBorder();
        frame.getContentPane().add(RequestedPanel, BorderLayout.CENTER);
        RefreshFrame();
    }
    
    JFrame frame;
    Font DefaultFont;
    Color ButtonColor;
    
    JPanel Header, Contents;
    JLabel HeaderText, HeaderImage, WelcomeText;
    JButton WorkAppointments, CheckPastAppointments, SettingButton, LogoutButton;
    
    
    public TechnicianGUI(){
        frame = new JFrame("Technician Dashboard");
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
        
        HeaderText = new JLabel("Technician Dashboard");
        HeaderText.setFont(new Font("Serif", Font.BOLD, 40));
        HeaderText.setBorder(new EmptyBorder(0,10,0,0));
        
        HeaderImage = new JLabel();
        HeaderImage.setIcon(new ImageIcon(getClass().getResource("/ahhasc/imgs/TechnicianBG.jpg")));
        
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
            name = AHHASC.Technician.getName();
        }
        catch(Exception e)
        {
            System.out.println("Null Poniter");
        } 
        WelcomeText.setText(String.format("Welcome %s, What Would You Want To Do Today?", name));
        WelcomeText.setFont(new Font("Serif", Font.PLAIN, 24));
        WelcomeText.setBorder(new EmptyBorder(10,10,0,0));
        
        WorkAppointments = new JButton("Appointments");
        WorkAppointments.setFont(DefaultFont);
        WorkAppointments.setBackground(ButtonColor);
        WorkAppointments.setFocusPainted(false);
        WorkAppointments.setToolTipText("See all appointments that not been work on.");
        WorkAppointments.addActionListener((ActionEvent e) -> {
            OpenAppointments();
        });
        
        SettingButton = new JButton("Setting");
        SettingButton.setFont(DefaultFont);
        SettingButton.setBackground(ButtonColor);
        SettingButton.setFocusPainted(false);
        SettingButton.setToolTipText("Modify Account");
        SettingButton.addActionListener((ActionEvent e) ->{
            OpenSetting();
        });
        
        
        CheckPastAppointments = new JButton("See All Past Appointments");
        CheckPastAppointments.setFont(new Font("Serif", Font.BOLD, 32));
        CheckPastAppointments.setBackground(ButtonColor);
        CheckPastAppointments.setFocusPainted(false);
        CheckPastAppointments.setToolTipText("See all appointments that is done and paid.");
        CheckPastAppointments.addActionListener((ActionEvent e) ->{
            OpenPastApp();
        });
        
        LogoutButton = new JButton("Logout");
        LogoutButton.setFont(DefaultFont);
        LogoutButton.setBackground(ButtonColor);
        LogoutButton.setFocusPainted(false);
        
        
        
        LogoutButton.addActionListener((ActionEvent e) -> {
            Logout();
        });
        
        
        ContentsGL.setVerticalGroup(ContentsGL.createSequentialGroup()
            .addComponent(WelcomeText,GroupLayout.PREFERRED_SIZE, 70,GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 90, GroupLayout.PREFERRED_SIZE)
            .addGroup(ContentsGL.createSequentialGroup()
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 60, GroupLayout.PREFERRED_SIZE)
                .addGroup(ContentsGL.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(WorkAppointments,GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addComponent(CheckPastAppointments, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(60)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 90, GroupLayout.PREFERRED_SIZE)
                .addGroup(ContentsGL.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(SettingButton, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addComponent(LogoutButton, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                )
            )
        );
        
        ContentsGL.setHorizontalGroup(ContentsGL.createSequentialGroup()
            .addGroup(ContentsGL.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(WelcomeText, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                .addGroup(ContentsGL.createSequentialGroup()
                    .addGap(60)
                    .addComponent(WorkAppointments,GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addGap(60)
                    .addComponent(CheckPastAppointments, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addGap(60)
                )
                .addGroup(ContentsGL.createSequentialGroup()
                    .addGap(60)
                    .addComponent(SettingButton, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addGap(60)
                    .addComponent(LogoutButton, GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addGap(60)
                )
            )
        );
        
        frame.getContentPane().add(Header, BorderLayout.NORTH);
        frame.getContentPane().add(Contents, BorderLayout.CENTER);
        
        frame.setVisible(true); // debug purpose
    }

}
