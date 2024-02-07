import XMLSerializer.XMLable;
import XMLSerializer.XMLfield;

@XMLable
public class Teacher {

	@XMLfield(type = "String")
	private String firstname;

	@XMLfield(type = "String", name = "surname")
	private String lastname;

	@XMLfield(type = "boolean")
	private boolean inactivity;

	public Teacher(String firstname, String lastname, boolean inactivity) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.inactivity = inactivity;
	}
}
