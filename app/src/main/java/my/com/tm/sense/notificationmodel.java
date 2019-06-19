package my.com.tm.sense;

public class notificationmodel {

    private String id;
    private String msg;
    private String activity;
    private String activityremark;
    private String sender;
    private String receiver;
    private String notificationdate;
    private String division;
    private String staffid;
    private String employeename;

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivityremark() {
        return activityremark;
    }

    public void setActivityremark(String activityremark) {
        this.activityremark = activityremark;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getNotificationdate() {
        return notificationdate;
    }

    public void setNotificationdate(String notificationdate) {
        this.notificationdate = notificationdate;
    }
}
