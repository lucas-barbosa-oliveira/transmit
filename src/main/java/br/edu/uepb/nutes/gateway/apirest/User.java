package br.edu.uepb.nutes.gateway.apirest;

public abstract class  User {
	private String name;
	private int gender;
	private long dateOfBirth;
	private double height;
	private String email;
	private String password;
	private int groupId;	
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String name, int gender, long dateOfBirth, double height, String email, String password, int groupId) {
		super();
		this.name = name;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.height = height;
		this.email = email;
		this.password = password;
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public long getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(long dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

}
