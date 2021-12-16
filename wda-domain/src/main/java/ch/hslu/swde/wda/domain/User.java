package ch.hslu.swde.wda.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "userdata")
public class User implements Serializable {

	private static final long serialVersionUID = 9172780623102366628L;
	private static final int FIRSTNAMELENGTH = 1;
	private static final int LASTNAMELENGTH = 5;

	@Id
	private String userid;

	private String firstname;
	private String lastname;
	private String userpwd;

	public User() {
	}

	public User(String firstname, String lastname, String userpwd) {

		// userid created by using first letter of firstname and 5 letters of lastname
		userid = (firstname.substring(0, FIRSTNAMELENGTH) + lastname.substring(0, LASTNAMELENGTH)).toLowerCase();
		this.firstname = firstname;
		this.lastname = lastname;
		this.userpwd = userpwd;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String firstname, String lastname) {
		this.userid = (firstname.substring(0, FIRSTNAMELENGTH) + lastname.substring(0, LASTNAMELENGTH)).toLowerCase();
	}
	
	public void setUseridWithIterator(String firstname, String lastname, int i) {
		this.userid = (firstname.substring(0, FIRSTNAMELENGTH) + lastname.substring(0, LASTNAMELENGTH)).toLowerCase() + i;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (!userid.equals(user.userid)) return false;
		if (!firstname.equals(user.firstname)) return false;
		if (!lastname.equals(user.lastname)) return false;
		return userpwd.equals(user.userpwd);
	}

	@Override
	public int hashCode() {
		int result = userid.hashCode();
		result = 31 * result + firstname.hashCode();
		result = 31 * result + lastname.hashCode();
		result = 31 * result + userpwd.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "User [UserId=" + userid + ", Firstname=" + firstname + ", Lastname=" + lastname + "]";
	}
}
