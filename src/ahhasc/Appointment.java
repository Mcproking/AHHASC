package ahhasc;

import java.time.LocalDate;
import java.time.Month;

public class Appointment {
    private int AppID;
    private String CustomerName; //1
    private String ApplianceCatagory; //2
    private String ApplianceIssue; //3
    private String TechnicianName; //4
    private LocalDate RequestDate; //5
    private LocalDate FinishDate; //6
    private Boolean IsFixed; //7
    private int Cost; //8
    private Boolean IsPaid; //9
    private String TechnicianFeedback; //10
    private String CustomerFeedback; //11
    
    public Appointment(int ID, String customerName,String applianceCatagory,String applianceIssue,String technicianName,LocalDate requestDate, LocalDate finishDate,Boolean isFixed,int cost,Boolean isPaid,String technicianFeedback,String customerFeedback){
        this.AppID = ID;
        this.CustomerName = customerName;
        this.ApplianceCatagory = applianceCatagory;
        this.ApplianceIssue = applianceIssue;
        this.TechnicianName = technicianName;
        this.RequestDate = requestDate;
        this.FinishDate = finishDate;
        this.IsFixed = isFixed;
        this.Cost = cost;
        this.IsPaid = isPaid;
        this.TechnicianFeedback = technicianFeedback;
        this.CustomerFeedback = customerFeedback;
    }
    
    public Appointment(int ID,String customerName,String applianceCatagory,String applianceIssue,String technicianName,LocalDate requestDate){
        this.AppID = ID;
        this.CustomerName = customerName;
        this.ApplianceCatagory = applianceCatagory;
        this.ApplianceIssue = applianceIssue;
        this.TechnicianName = technicianName;
        this.RequestDate = requestDate;
        this.FinishDate = LocalDate.of(0001, Month.JANUARY, 1);
        this.IsFixed = false;
        this.Cost = 0;
        this.IsPaid = false;
        this.TechnicianFeedback = null;
        this.CustomerFeedback = null;
    }

    public int getAppointmentID(){
        return AppID;
    }
    public String getCustomerName(){
        return CustomerName;
    }
    public String getApplianceCatagory(){
        return ApplianceCatagory;
    }
    public String getApplianceIssue(){
        return ApplianceIssue;
    }
    public String getTechnicianName(){
        return TechnicianName;
    }
    public LocalDate getRequestedDate(){
        return RequestDate;
    }
    public LocalDate getFinishDate(){
        return FinishDate;
    }
    public Boolean getIsFixed(){
        return IsFixed;
    }
    public int getCost(){
        return Cost;
    }
    public Boolean getIsPaid(){
        return IsPaid;
    }
    public String getTechnicianFeedback(){
        return TechnicianFeedback;
    }
    public String getCustomerFeedback(){
        return CustomerFeedback;
    }
    
    public void setFinishDate(String i){
        this.FinishDate = LocalDate.parse(i);
    }
    public void setPrice(int i){
        this.Cost = i;
    }
    public void setTechFeedback(String i){
        this.TechnicianFeedback = i;
    }
    public void setIsFixed(Boolean i ){
        this.IsFixed = i;
    }
    public void setIsPaid(Boolean i){
        this.IsPaid = i;
    }
    public void setCustFeedback(String i){
        this.CustomerFeedback = i;
    }
    
    public void setTechName(String i){
        this.TechnicianName = i;
    }
    
}
