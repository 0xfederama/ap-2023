import XMLSerializer.XMLSerializer;

public class Main {
    public static void main(String[] args) {
        // Students
        Student stud1 = new Student("Federico", "Ramacciotti", 24, 123456);
        Student stud2 = new Student("Mario", "Rossi", 30, 654321);

        // Non-XMLable object
        String str = "";

        // Teacher
        Teacher teacher = new Teacher("Albert", "Einstein", false);

        // Serialize all the objects
        Object[] arr = { stud1, stud2, str, teacher };
        XMLSerializer.serialize(arr, "fileName.xml");
    }
}
