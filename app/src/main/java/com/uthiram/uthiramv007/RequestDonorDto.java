package com.uthiram.uthiramv007;

public class RequestDonorDto {
    private String userName;
    private String patientName;
    private String bloodGroup;
    private String unitsNeeded;
    private String hospitalName;
    private String patientPhoneNo;
    private String neededWithInDate;
    private String neededWithInTime;

    public RequestDonorDto(String userName, String patientName, String bloodGroup, String unitsNeeded, String hospitalName, String patientPhoneNo, String neededWithInDate, String neededWithInTime) {
        this.userName = userName;
        this.patientName = patientName;
        this.bloodGroup = bloodGroup;
        this.unitsNeeded = unitsNeeded;
        this.hospitalName = hospitalName;
        this.patientPhoneNo = patientPhoneNo;
        this.neededWithInDate = neededWithInDate;
        this.neededWithInTime = neededWithInTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getUnitsNeeded() {
        return unitsNeeded;
    }

    public void setUnitsNeeded(String unitsNeeded) {
        this.unitsNeeded = unitsNeeded;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getPatientPhoneNo() {
        return patientPhoneNo;
    }

    public void setPatientPhoneNo(String patientPhoneNo) {
        this.patientPhoneNo = patientPhoneNo;
    }

    public String getNeededWithInDate() {
        return neededWithInDate;
    }

    public void setNeededWithInDate(String neededWithInDate) {
        this.neededWithInDate = neededWithInDate;
    }

    public String getNeededWithInTime() {
        return neededWithInTime;
    }

    public void setNeededWithInTime(String neededWithInTime) {
        this.neededWithInTime = neededWithInTime;
    }

    @Override
    public String toString() {
        return "RequestDonorDto{" +
                "patientName='" + patientName + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", unitsNeeded='" + unitsNeeded + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", patientPhoneNo='" + patientPhoneNo + '\'' +
                ", neededWithInDate='" + neededWithInDate + '\'' +
                ", neededWithInTime='" + neededWithInTime + '\'' +
                '}';
    }
}
