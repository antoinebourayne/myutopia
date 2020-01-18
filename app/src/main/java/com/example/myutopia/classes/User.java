package com.example.myutopia.classes;

public class User {

    //Attributes
    private String id;
    private String firstName;
    private String secondName;
    private String userName;
    private String password;


    //Constructors
    public User(){}
    public User(String m_userName, String m_password, String m_firstName, String m_secondName)
    {
        userName   = m_userName;
        password   = m_password;
        firstName  = m_firstName;
        secondName = m_secondName;
    }


    //Getters
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getSecondName() { return secondName; }
    public String getUserName() { return userName; }
    public String getPassword() { return password; }


    //Setters
    public void setId(String id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setSecondName(String secondName) { this.secondName = secondName; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setPassword(String password) { this.password = password; }


    //Methods
}
