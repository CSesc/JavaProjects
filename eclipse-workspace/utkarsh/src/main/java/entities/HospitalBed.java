package entities;

public class HospitalBed {
    public HospitalBed(String hospitalName, String contact, String phone, String normalBeds, String oxygenBeds,
			String address, String foundUseful, String notes, String comments, String votes, String totalBeds,
			String lastUpdateTime) {
		super();
		this.hospitalName = hospitalName;
		this.contact = contact;
		Phone = phone;
		NormalBeds = normalBeds;
		OxygenBeds = oxygenBeds;
		Address = address;
		this.foundUseful = foundUseful;
		this.notes = notes;
		this.comments = comments;
		this.votes = votes;
		TotalBeds = totalBeds;
		this.lastUpdateTime = lastUpdateTime;
	}

	String hospitalName;
    public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getNormalBeds() {
		return NormalBeds;
	}
	public void setNormalBeds(String normalBeds) {
		NormalBeds = normalBeds;
	}
	public String getOxygenBeds() {
		return OxygenBeds;
	}
	public void setOxygenBeds(String oxygenBeds) {
		OxygenBeds = oxygenBeds;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getFoundUseful() {
		return foundUseful;
	}
	public void setFoundUseful(String foundUseful) {
		this.foundUseful = foundUseful;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getVotes() {
		return votes;
	}
	public void setVotes(String votes) {
		this.votes = votes;
	}
	public String getTotalBeds() {
		return TotalBeds;
	}
	public void setTotalBeds(String totalBeds) {
		TotalBeds = totalBeds;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	String contact="NA";
    String Phone="NA";
    String NormalBeds="NA";
    String OxygenBeds="NA";
    String Address="NA";
    String foundUseful="NA";
    String notes="NA";
    String comments="NA";
    String votes="NA";
    String TotalBeds="NA";
    String lastUpdateTime="NA";



}
