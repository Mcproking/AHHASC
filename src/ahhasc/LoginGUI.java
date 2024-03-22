package ahhasc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

public class LoginGUI {
    public static AdminGUI AdminGUI;
    public static TechnicianGUI TechnicianGUI;
    public static CustomerGUI CustomerGUI;
    
    public void destoryObjects(){
        DataIO.Init();
        AdminGUI = null;
        TechnicianGUI = null;
        CustomerGUI = null;
    }
    
    public void loginButtonPressed (ActionEvent e){
        try{
            String username = UsernameInput.getText();
            String password = String.valueOf(PasswordInput.getPassword());
            Users found = DataIO.checkUsers(username);
            
            if(found == null){
                //JOptionPane.showMessageDialog(frame, "Worng name?");
                throw new Exception();
                // return a error, but instead pop up a red text?
            }
            
            // use equals to compare the contents of this two object
            // cant use == due to the memori location of the 2 object is different
            if(!found.getPassword().equals(password)){
                //JOptionPane.showMessageDialog(frame, "Worng pass");
                throw new Exception();
                // return a error, but instead pop up a red text?
            }

            
            // hide this container

            if(found.getRole() == Users.ROLE_ADMIN){
                System.out.println("Debug: Logined Admin");
                AHHASC.Admin = new Admin(found);
                
                frame.setVisible(false);
                FailLabel.setVisible(false);
                
                // create AdminGUI object and show to user
                AdminGUI = new AdminGUI();
                AdminGUI.frame.setVisible(true);
            }

            if(found.getRole() == Users.ROLE_TECHNICIAN){
                System.out.println("Debug: Logined Technician");
                AHHASC.Technician = new Technician(found);
                
                frame.setVisible(false);
                FailLabel.setVisible(false);
                
                TechnicianGUI = new TechnicianGUI();
                TechnicianGUI.frame.setVisible(true);
                // show technicaian container
            }

            if(found.getRole() == Users.ROLE_CUSTOMER){
                System.out.println("Debug: Logined Customer");
                AHHASC.Customer = new Customer(found);
                
                frame.setVisible(false);
                FailLabel.setVisible(false);
                
                CustomerGUI = new CustomerGUI();
                CustomerGUI.frame.setVisible(true);
                // show customer container
            }
            
            UsernameInput.setText("");
            PasswordInput.setText("");
        }catch(Exception err){
            //throw red text here
            //JOptionPane.showMessageDialog(frame, "Error yeet");
            FailLabel.setVisible(true);
        }
    }
    
    JFrame frame;
    JLabel thumb,UiMainText, UsernameLabel, PasswordLabel, FailLabel;
    JTextField UsernameInput;
    JPasswordField PasswordInput;
    JButton LoginButton;
    
    public LoginGUI(){

        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,500);
        frame.setResizable(false);
        frame.setLocation(300,100);
        
        
        Container c = frame.getContentPane();
        GroupLayout gl = new GroupLayout(c);
        frame.setLayout(gl);
        
        thumb = new JLabel();
        thumb.setIcon(new ImageIcon(getClass().getResource("/ahhasc/imgs/loginBG.jpg")));
        
        UiMainText = new JLabel("Login");
        UiMainText.setFont(new Font("Serif", Font.BOLD, 48));
        
        UsernameLabel = new JLabel("Username");
        UsernameLabel.setFont(new Font("Serif", Font.BOLD, 24));
        
        PasswordLabel = new JLabel("Password");
        PasswordLabel.setFont(new Font("Serif", Font.BOLD, 24));
        
        UsernameInput = new JTextField();
        UsernameInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        UsernameInput.setMargin(new Insets(0,5,0,0));
        
        PasswordInput = new JPasswordField();
        PasswordInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        PasswordInput.setMargin(new Insets(0,5,0,0));
        
        LoginButton = new JButton("Login");
        LoginButton.setFont(new Font("Serif", Font.PLAIN, 20));
        LoginButton.setBackground(new Color(242,242,242));
        LoginButton.addActionListener((ActionEvent e) -> {
            loginButtonPressed(e);
        });
        
        FailLabel = new JLabel("Login Failed");
        FailLabel.setForeground(Color.RED);
        FailLabel.setVisible(false);
        
        gl.setVerticalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(thumb, GroupLayout.PREFERRED_SIZE, 500, Short.MAX_VALUE)
                .addGroup(gl.createSequentialGroup()
                    .addGap(26,26,26)
                    .addComponent(UiMainText)
                    .addGap(41,41,41)
                    .addComponent(UsernameLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(UsernameInput)
                    .addGap(41,41,41)
                    .addComponent(PasswordLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(PasswordInput)
                    .addGap(30,30,30)
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(LoginButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                            .addComponent(FailLabel,GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
                ));
        
        gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(gl.createSequentialGroup()
                    .addComponent(thumb, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,10,Short.MAX_VALUE)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, gl.createParallelGroup(GroupLayout.Alignment.LEADING,false)
                            .addComponent(UsernameLabel)
                            .addComponent(UiMainText)
                            .addComponent(UsernameInput,GroupLayout.PREFERRED_SIZE,486,Short.MAX_VALUE)
                            .addComponent(PasswordLabel)
                            .addComponent(PasswordInput,GroupLayout.PREFERRED_SIZE,0,Short.MAX_VALUE))
                        .addComponent(FailLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                        .addComponent(LoginButton, GroupLayout.Alignment.TRAILING , GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                .addGap(40,40,40)));
        
        frame.setVisible(true);
    }    
}
