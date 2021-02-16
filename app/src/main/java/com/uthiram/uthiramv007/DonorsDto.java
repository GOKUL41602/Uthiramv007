package com.uthiram.uthiramv007;

public class DonorsDto {

    private String donorName;
    private String rollNo;
    private String age;
    private String bloodGroup;
    private String phoneNo;
    private String address;
    private String district;
    private String pinCode;
    private String weight;
    private String password;
    private String status;
    private String deptName;
    private String lastDonatedDate;
    private int count=0;

    public DonorsDto() {
        count++;
    }

    public DonorsDto(String donorName, String rollNo, String age, String bloodGroup, String phoneNo, String address, String district, String pinCode, String weight, String password, String status, String deptName, String lastDonatedDate) {
        this.donorName = donorName;
        this.rollNo = rollNo;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.phoneNo = phoneNo;
        this.address = address;
        this.district = district;
        this.pinCode = pinCode;
        this.weight = weight;
        this.password = password;
        this.status = status;
        this.deptName = deptName;
        this.lastDonatedDate = lastDonatedDate;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLastDonatedDate() {
        return lastDonatedDate;
    }

    public void setLastDonatedDate(String lastDonatedDate) {
        this.lastDonatedDate = lastDonatedDate;
    }

    @Override
    public String toString() {
        return "DonorsDto{" +
                "donorName='" + donorName + '\'' +
                ", rollNo='" + rollNo + '\'' +
                ", age='" + age + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", address='" + address + '\'' +
                ", district='" + district + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", weight='" + weight + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", deptName='" + deptName + '\'' +
                ", lastDonatedDate='" + lastDonatedDate + '\'' +
                '}';
    }
}
