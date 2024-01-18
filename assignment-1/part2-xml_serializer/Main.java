public class Main {
    public static void main(String[] args) {
        // Student
        Student stud = new Student("Federico", "Ramacciotti", 24, 123456);

        // Non-XMLable object
        String str = "";

        // Teacher
        Teacher teacher = new Teacher("Robert", "Oppenheimer", false);

        // Serialize all the objects
        Object[] arr = { stud, str, teacher };
        XMLSerializer.serialize(arr, "fileName.xml");
    }
}
