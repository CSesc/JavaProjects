package entities;

public class HospitalBed {

    String hospitalName;
    String contact;
    String totalAvailability;
    String vacant;
    String address;
    String lastUpdateTime;

    public HospitalBed(String hospitalName, String contact, String totalAvailability, String vacant){
        this.hospitalName = hospitalName;
        this.contact = contact;
        this.totalAvailability = totalAvailability;
        this.vacant = vacant;

    }

    public HospitalBed(String hospitalName,String address, String contact, String totalAvailability, String vacant,String lastUpdateTime){
        this.hospitalName = hospitalName;
        this.address = address;
        this.contact = contact;
        this.totalAvailability = totalAvailability;
        this.vacant = vacant;
        this.lastUpdateTime = lastUpdateTime;

    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getContact() {
        return contact;
    }

    public String getTotalAvailability() {
        return totalAvailability;
    }

    public String getVacant() {
        return vacant;
    }

    public String getAddress() {
        return address;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
}
