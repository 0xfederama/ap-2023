import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class XMLSerializer {

	/**
	 * Serialize the objects in arr to the file fileName.
	 * Objects classes must be annotated with XMLable.
	 * Objects fields must be annotated with XMLFields.
	 *
	 * @param arr
	 * @param fileName
	 */
	public static void serialize(Object[] arr, String fileName) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)))) {

			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.write("<Objects>\n");

			for (Object obj : arr) {
				Class<?> objClass = obj.getClass();
				String className = objClass.getSimpleName();

				System.out.println("\nCLASS: " + className);

				// If not annotated, print notXMLable
				if (!objClass.isAnnotationPresent(XMLable.class)) {
					writer.write("\t<notXMLable />\n");
					System.out.println("Not an XMLable");
					continue;
				}

				// Create the string to print it only if everything succeeds
				try {
					StringBuilder str = new StringBuilder("");
					str.append("\t<" + className + ">\n");

					// Serialize the objects in the class
					str.append(getFields(obj));

					// Append end of class and write to file
					str.append("\t</" + className + ">\n");
					writer.write(str.toString());
				} catch (Exception e) {
					System.out.println("Impossible to write class or field to file");
					e.printStackTrace();
				}
			}

			writer.write("</Objects>");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nWrote objects to XML file");
		}
	}

	/**
	 * Read object and, for each field, write the xml line for it.
	 *
	 * @param obj
	 * @return a string for the fields of the object obj
	 * @throws IllegalAccessException
	 */
	private static String getFields(Object obj) throws IllegalAccessException {
		Class<?> objClass = obj.getClass();
		StringBuilder str = new StringBuilder();

		for (Field field : objClass.getDeclaredFields()) {
			System.out.println("Field: " + field.getName());

			// Grant access to private fields
			field.setAccessible(true);

			if (field.isAnnotationPresent(XMLfield.class)) {
				XMLfield annotation = field.getAnnotation(XMLfield.class);

				// If the name is annotated print that, otherwise print the field name
				String annotationName = annotation.name();
				String fieldName = annotationName.equals("") ? field.getName() : annotationName;
				str.append("\t\t<" + fieldName);

				// Get field type
				String fieldType = annotation.type();
				str.append(" type=\"" + fieldType + "\">");

				// Get field value
				Object fieldValue = field.get(obj);
				String value = fieldValue == null ? "" : fieldValue.toString();
				str.append(value);

				// Close the line
				str.append("</" + fieldName + ">\n");
			}

			// Remove access to private field
			field.setAccessible(false);
		}

		return str.toString();
	}
}
