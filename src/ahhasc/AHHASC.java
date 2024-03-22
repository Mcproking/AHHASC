package ahhasc;

public class AHHASC {
    static LoginGUI LoginGUI;
    static AdminGUI AdminGUI;
    static TechnicianGUI TechnicianGUI;
    public static Customer Customer = null;
    public static Admin Admin = null;
    public static Technician Technician = null;
    
    public static void main(String[] args) {
        DataIO.Init();

        LoginGUI = new LoginGUI();
    }
}
