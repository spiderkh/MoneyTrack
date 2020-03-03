package com.bhaskar.moneytrack;

public class Profile {

    private String uniqueKey;
    private String name;
    private  String email;
    private String role;
    private String mobile;
    private String groupCode;
    private String groupName;
    private String  shortName;
    private Double limit;


    public Profile() {
    }

    public Profile(String uniqueKey, String name, String email, String role, String mobile, String groupCode, String groupName, String shortName,Double limit) {
        this.uniqueKey = uniqueKey;
        this.name = name;
        this.email = email;
        this.role = role;
        this.mobile = mobile;
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.shortName = shortName;
        this.limit=limit;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "uniqueKey='" + uniqueKey + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", mobile='" + mobile + '\'' +
                ", groupCode='" + groupCode + '\'' +
                ", groupName='" + groupName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", limitValue=" + limit +
                '}';
    }
}
