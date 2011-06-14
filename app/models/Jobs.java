package models;


import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class Jobs {
	private List members;
	private List ou;
	private String cn;

	public List getMembers() {
		return members;
	}

	public void setMembers(Enumeration members) {
		setMembers(Collections.list(members));
	}

	public void setMembers(List members) {
		this.members = members;
	}

	public List getOu() {
		return ou;
	}

	public void setOu(Enumeration ou) {
		setOu(Collections.list(ou));
	}

	public void setOu(List ou) {
		this.ou = ou;
	}

	
	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

}