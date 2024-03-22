package ahhasc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataIO {
    public static final int ROLE_TECHNICIAN = 1;
    public static final int ROLE_CUSTOMER = 10;
    
    public static ArrayList<Users> allUsers;
    public static ArrayList<Appointment> allAppointment;
    
    public static void Init(){
        allUsers = new ArrayList<Users>();
        allAppointment = new ArrayList<Appointment>();
        
        readUser();
        readAppointment();
    }
    
    public static void readUser(){
        try{
            Scanner input = new Scanner(new File("Users.txt"));
            
            while(input.hasNext()){
                String lines = input.nextLine();
                String[] data = lines.split(",", 0);
                allUsers.add(new Users(data[0],data[1],Integer.parseInt(data[2])));
            }    
            
//            System.out.println("done read");
        }
        catch (Exception e){
            System.out.print("Error in DataIO with error" + e);
        }
    }  
    public static void readAppointment(){
        try{
            Scanner input = new Scanner(new File("Appointment.txt"));
            
            while(input.hasNext()){
                String lines = input.nextLine();
                String[] data = lines.split(",", 0);
                
                LocalDate reqsDate = LocalDate.parse(data[5]);
                LocalDate finishDate = LocalDate.parse(data[6]);
                
                String TechFeedback = convertStringToNull(data[10]);
                String CustFeedback = convertStringToNull(data[11]);
                
                
                allAppointment.add(new Appointment(
                    Integer.parseInt(data[0]),
                    data[1],
                    data[2],
                    data[3],
                    data[4],
                    reqsDate,
                    finishDate,
                    Boolean.valueOf(data[7]),
                    Integer.parseInt(data[8]),
                    Boolean.valueOf(data[9]),
                    TechFeedback,
                    CustFeedback));
            }
        } catch (Exception e){
            System.out.print("Error in DataIO with error: read Appointment" + e);
        }
    }
    
    public static ArrayList<Customer> getAllCustomer(){
        
        ArrayList<Customer> allCustomer = new ArrayList<>();
    
        try{
            for(int i = 0; i <allUsers.size(); i++){
                if(allUsers.get(i).getRole() == ROLE_CUSTOMER){
                    allCustomer.add(new Customer(allUsers.get(i)));
                }
            }
            
//            System.out.println("Done get Customer");
        }catch (Exception e){
            System.out.println("Issue in DataIO in ReadUsersRoleLevel");
        }
        
        return allCustomer;
    } 
    public static ArrayList<Technician> getAllTechnician(){
        ArrayList<Technician> allTechnician = new ArrayList<>();
        
        try{
            Scanner input = new Scanner(new File("Users.txt"));
            
            while(input.hasNext()){
                String lines = input.nextLine();
                String[] data = lines.split(",", 0);
                
                if(Integer.parseInt(data[2]) == ROLE_TECHNICIAN){
                    allTechnician.add(new Technician(data[0], data[1], Integer.parseInt(data[2])));
                }
            }
            
        }catch (Exception e){
            System.out.println("Issue in DataIO in ReadUsersRoleLevel");
        }
        
        return allTechnician;
    } 
    
    public static void writeUser(){
        try {
            PrintWriter output = new PrintWriter("Users.txt");
            
            for(int i = 0; i <allUsers.size(); i++){
                String format = String.format("%s,%s,%s", allUsers.get(i).getName(), allUsers.get(i).getPassword(), allUsers.get(i).getRole());
                output.println(format);
            }
            output.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void writeAppointment(){
        try{
            PrintWriter output = new PrintWriter("Appointment.txt");
            int AppInt = 50000;
            // 50001,customer name (string), fix catagory (combo selection), fix item (string),technicion , req date, finish date,
            // isfixed?, cost, isPaid?, techFeedback, custFeedback
            for (int i = 0; i <allAppointment.size(); i++){
                String format = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        AppInt + i,
                        allAppointment.get(i).getCustomerName(),
                        allAppointment.get(i).getApplianceCatagory(),
                        allAppointment.get(i).getApplianceIssue(),
                        allAppointment.get(i).getTechnicianName(),
                        allAppointment.get(i).getRequestedDate(),
                        allAppointment.get(i).getFinishDate(),
                        allAppointment.get(i).getIsFixed(),
                        allAppointment.get(i).getCost(),
                        allAppointment.get(i).getIsPaid(),
                        allAppointment.get(i).getTechnicianFeedback(),
                        allAppointment.get(i).getCustomerFeedback());
//                System.out.println(format);
                output.println(format);
            }
            output.close();
            
        }catch (FileNotFoundException ex){
            System.out.println("Issue in DataIO inside WriteAppointment" + ex);
        }
    }
    
    
    public static Users checkUsers(String name){
        for(int i = 0; i < allUsers.size(); i++){
            if(allUsers.get(i).getName().equals(name)){
                return allUsers.get(i);
            }
        }
        return null;
    }
    
    private static String convertStringToNull(String str) {
        if ("null".equals(str)) {
            return null;
        }
        return str;
    }
}
