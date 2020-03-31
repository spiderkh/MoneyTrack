package com.bhaskar.moneytrack;

public class Post {
    private String dropcategory;
    private Double amount;
    private String comment;
    private String date;
    private String email;
    private String Month;
    private String yearName;
    private String time;
    private String shortName;
    private String status;
    private String uniquekey;
    private String updated;


    @Override
    public String toString() {
        return "Post{" +
                "dropcategory='" + dropcategory + '\'' +
                ", amount=" + amount +
                ", comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                ", email='" + email + '\'' +
                ", Month='" + Month + '\'' +
                ", yearName='" + yearName + '\'' +
                ", time='" + time + '\'' +
                ", shortName='" + shortName + '\'' +
                ", status='" + status + '\'' +
                ", uniquekey='" + uniquekey + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsernName() {
        return shortName;
    }

    public void setUsernName(String usernName) {
        this.shortName = usernName;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Post() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDropcategory() {
        return dropcategory;
    }

    public void setDropcategory(String dropcategory) {
        this.dropcategory = dropcategory;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Post(String dropcategory, Double amount, String comment, String date, String email, String month, String yearName, String time, String shortName, String status, String uniquekey, String updated) {
        this.dropcategory = dropcategory;
        this.amount = amount;
        this.comment = comment;
        this.date = date;
        this.email = email;
        this.Month = month;
        this.yearName = yearName;
        this.time = time;
        this.shortName = shortName;
        this.status = status;
        this.uniquekey = uniquekey;
        this.updated = updated;
    }
}
