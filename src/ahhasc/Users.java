package ahhasc;

import java.time.LocalDate;
import java.util.ArrayList;

public class Users {

    private String Name;
    private String Password;
    private int Role;

    public Users(String name, String password, int role) {
        this.Name = name;
        this.Password = password;
        this.Role = role;
    }

    public Users(Users user) {
        this.Name = user.getName();
        this.Password = user.getPassword();
        this.Role = user.getRole();
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public int getRole() {
        return Role;
    }

    public void setName(String i) {
        this.Name = i;
    }

    public void setPassword(String i) {
        this.Password = i;
    }

    public static final int ROLE_ADMIN = 0;
    public static final int ROLE_TECHNICIAN = 1;
    public static final int ROLE_CUSTOMER = 10;
}

class Customer extends Users {

    private final String Name;

    public Customer(String name, String password, int role) {
        super(name, password, role);
        this.Name = name;
    }

    public Customer(Users user) {
        super(user);
        this.Name = user.getName();
    }

    public ArrayList<Appointment> getAllFeedbackLessAppointment() {
        ArrayList<Appointment> tempList = new ArrayList<Appointment>();
        for (int i = 0; i < DataIO.allAppointment.size(); i++) {
            // check if the list have the same name as the current cust
            if (DataIO.allAppointment.get(i).getCustomerName().equals(Name)) {
                if (DataIO.allAppointment.get(i).getIsPaid()) {
                    if (DataIO.allAppointment.get(i).getCustomerFeedback() == null) {
                        tempList.add(DataIO.allAppointment.get(i));
                    }
                }
            }
        }
        return tempList;
    }

    public ArrayList<Appointment> getAllPassAppointment() {
        ArrayList<Appointment> tempList = new ArrayList<Appointment>();
        for (int i = 0; i < DataIO.allAppointment.size(); i++) {
            // check if the list have the same name as the current cust
            if (DataIO.allAppointment.get(i).getCustomerName().equals(Name)) {
                if (DataIO.allAppointment.get(i).getIsPaid() && DataIO.allAppointment.get(i).getIsFixed()) {
                    tempList.add(DataIO.allAppointment.get(i));
                }
            }
        }
        return tempList;
    }

    public void updateAppointent(int AppID, String Feedback) {
        try {
            if (AppID == 0 || Feedback == null) {
                throw new Exception();
            }

            for (int i = 0; i < DataIO.allAppointment.size(); i++) {
                if (DataIO.allAppointment.get(i).getAppointmentID() == AppID) {
                    DataIO.allAppointment.get(i).setCustFeedback(Feedback);
                    System.out.println("Debug: Done Writing");
                    break;
                }
            }
            System.out.println("Debug: write all appointment");
            DataIO.writeAppointment();

        } catch (Exception err) {
            System.out.println("Issue in Users-Customer > UpdateAppointment");
        }
    }
}

class Admin extends Users {

    ArrayList<Customer> allCustomer = new ArrayList<Customer>();
    ArrayList<Technician> allTechnician = new ArrayList<Technician>();

    public Admin(String name, String password, int role) {
        super(name, password, role);
        allCustomer = DataIO.getAllCustomer();
        allTechnician = DataIO.getAllTechnician();
    }

    public Admin(Users user) {
        super(user);
        allCustomer = DataIO.getAllCustomer();
        allTechnician = DataIO.getAllTechnician();
    }

    public ArrayList<Customer> getAllCustomer() {
        return allCustomer;
    }

    public ArrayList<Technician> getAllTechnician() {
        return allTechnician;
    }

    public ArrayList<String> getAllCustomerName() {
        ArrayList<String> tempList = new ArrayList<>();

        for (int i = 0; i < allCustomer.size(); i++) {
            tempList.add(allCustomer.get(i).getName());
        }
        return tempList;
    }

    public ArrayList<String> getAllTechnicianName() {
        ArrayList<String> tempList = new ArrayList<>();
        for (int i = 0; i < allTechnician.size(); i++) {
            tempList.add(allTechnician.get(i).getName());
        }
        return tempList;
    }

    public ArrayList<Appointment> getAllPastAppointment() {
        ArrayList<Appointment> tempList = new ArrayList<Appointment>();
        for (int i = 0; i < DataIO.allAppointment.size(); i++) {
            // check if the list have the same name as the current tech
            if (DataIO.allAppointment.get(i).getIsPaid() && DataIO.allAppointment.get(i).getIsFixed()) {
                tempList.add(DataIO.allAppointment.get(i));
            }
        }
        return tempList;
    }
}

class Technician extends Users {

    private final String Name;

    public Technician(String name, String password, int role) {
        super(name, password, role);
        this.Name = name;
    }

    public Technician(Users user) {
        super(user);
        this.Name = user.getName();
    }

    public ArrayList<Appointment> getAllAppointment() {
        ArrayList<Appointment> tempList = new ArrayList<Appointment>();
        for (int i = 0; i < DataIO.allAppointment.size(); i++) {
            if (DataIO.allAppointment.get(i).getTechnicianName().equals(Name)) { // check if the list have the same name as the current tech
                if (!DataIO.allAppointment.get(i).getIsFixed()) { // check if the appointment is fixed, is false then add to tempList
                    tempList.add(DataIO.allAppointment.get(i));
                }
            }
        }
        return tempList;
    }

    public void updateAppointent(int AppID, String FinishDate, int Price, String Feedback) {
        try {
            if (AppID == 0 || FinishDate == null || Price == 0 || Feedback == null) {
                throw new Exception();
            }

            for (int i = 0; i < DataIO.allAppointment.size(); i++) {
                if (DataIO.allAppointment.get(i).getAppointmentID() == AppID) {
                    DataIO.allAppointment.get(i).setFinishDate(FinishDate);
                    DataIO.allAppointment.get(i).setPrice(Price);
                    DataIO.allAppointment.get(i).setTechFeedback(Feedback);
                    DataIO.allAppointment.get(i).setIsFixed(true);
                    DataIO.allAppointment.get(i).setIsPaid(true);
                    System.out.println("Debug: Done Writing");
                    break;
                }
            }
            System.out.println("Debug: write all appointment");
            DataIO.writeAppointment();

        } catch (Exception err) {
            System.out.println("Issue in Users-Technician > UpdateAppointment");
        }
    }

    public ArrayList<Appointment> getAllPassAppointment() {
        ArrayList<Appointment> tempList = new ArrayList<Appointment>();
        for (int i = 0; i < DataIO.allAppointment.size(); i++) {
            // check if the list have the same name as the current tech
            if (DataIO.allAppointment.get(i).getTechnicianName().equals(Name)) {
                if (DataIO.allAppointment.get(i).getIsPaid() && DataIO.allAppointment.get(i).getIsFixed()) {
                    tempList.add(DataIO.allAppointment.get(i));
                }
            }
        }
        return tempList;
    }

    public Boolean validatePassword(String i) {
        if (i.equals(this.getPassword())) {
            return true;
        }
        return false;
    }

    public void updateAccount(String OldName, String NewName, String Password) {
        try {
            if (OldName == null || NewName == null || Password == null) {
                throw new Exception();
            }

            for (Users User : DataIO.allUsers) {
                if (User.getName().equals(OldName)) {
                    User.setName(NewName);
                    User.setPassword(Password);

                    System.out.println("Debug: Done Updating per Account");
                    break;
                }
            }

            for (Appointment appointment : DataIO.allAppointment) {
                if (appointment.getTechnicianName().equals(OldName)) {
                    appointment.setTechName(NewName);

                    System.out.println("Debug: Done Updating all Appointments");
                }
            }

            DataIO.writeUser();
            DataIO.writeAppointment();
        } catch (Exception err) {
            System.out.println("Issue in Users-Technician > UpdateAccount");
        }
    }

}
