package models;

import java.io.Serializable;


public class People implements Comparable, Serializable {
	private static final long serialVersionUID = -5405295132454676045L;

	public static final String ZONE_SCHEMA_ATTRIBUTE = "employeetype";
	public static final String DISPLAYNAME_SCHEMA_ATTRIBUTE = "cn";
	public static final String LASTNAME_SCHEMA_ATTRIBUTE = "sn";
	public static final String FIRSTSURNAME_SCHEMA_ATTRIBUTE = "givenname";


	private String uid;

	private String firstName;
	private String lastName;
	private String email;
	private String employeetype;
	private String displayName;
	private String ou;
	private String title;
	private String userpassword;
	private String employeeNumber;


	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployerNumber(String employerNumber) {
		this.employeeNumber = employerNumber;
	}

	public String getEmployeetype() {
		return employeetype;
	}

	public void setEmployeetype(String employeetype) {
		this.employeetype = employeetype;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return uid + " - " + firstName + " " + lastName + " <" + email + ">";
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getOu() {
		return ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	public boolean isActive(){
		return ( userpassword != null) && (!userpassword.isEmpty());
	}

	@Override
	public int compareTo(Object o) {
		People p = (People) o;

		if ( this.getLastName() == null )
			return (p.getLastName() == null)?0:-1;

		if (this.getLastName().compareTo(p.getLastName()) == 0){
			if (this.getFirstName() == null)
				return ( p.getFirstName() == null)?0:-1;
			return this.getFirstName().compareTo(p.getFirstName());
		}
		return this.getLastName().compareTo(p.getLastName());
	}


}
