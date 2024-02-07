import XMLSerializer.XMLable;
import XMLSerializer.XMLfield;

@XMLable
public class Student {

	@XMLfield(type = "String")
	private String firstname;

	@XMLfield(type = "String", name = "surname")
	private String lastname;

	@XMLfield(type = "int")
	private int age;

	public int matricola; // field not tagged

	public Student(String firstname, String lastname, int age, int matricola) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.age = age;
		this.matricola = matricola;
	}
}