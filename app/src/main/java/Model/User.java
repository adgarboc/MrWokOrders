package Model;

import com.google.firebase.firestore.FieldValue;

public class User
{

    public User()
    {
    }

    public User(String uid, String name, String mail, String password, String usertype, boolean status, FieldValue timeStamp) {
        this.uid = uid;
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.usertype = usertype;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public String getUid() {return uid;}

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getUsertype() {
        return usertype;
    }

    public FieldValue getTimeStamp() {
        return timeStamp;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public void setTimeStamp(FieldValue timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isStatus() {return status;}

    public void setStatus(boolean status) {this.status = status;}

    private String uid;
    private String name;
    private String mail;
    private String password;
    private String usertype;
    private boolean status;
    private FieldValue timeStamp;







}
