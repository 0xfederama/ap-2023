package XMLSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

			Map<Class<?>, ArrayList<AnnotatedField>> introspectedClasses = new HashMap<>();

			for (Object obj : arr) {
				Class<?> objClass = obj.getClass();
				String className = objClass.getSimpleName();

				// If not annotated, print notXMLable
				if (!objClass.isAnnotationPresent(XMLable.class)) {
					writer.write("\t<notXMLable />\n");
					System.out.println("\nCLASS: " + className + " is not an XMLable");
					continue;
				}

				// If the class has not been introspected, introspect it
				ArrayList<AnnotatedField> fields;
				if (!introspectedClasses.containsKey(objClass)) {
					System.out.println("\nCLASS: " + className + " not introspected yet");
					fields = introspectClass(objClass);
					introspectedClasses.put(objClass, fields);
				} else {
					System.out.println("\nCLASS: " + className + " already introspected");
					fields = introspectedClasses.get(objClass);
				}

				// Create the string to print it only if everything succeeds
				try {
					StringBuilder str = new StringBuilder("");
					str.append("\t<" + className + ">\n");

					// Serialize the objects in the class
					str.append(getFields(obj, fields));

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
	 * Introspects a class and returns a list of annotated fields.
	 *
	 * @param objClass the class to introspect
	 * @return list of AnnotatedField objects for the annotated fields in the class
	 */
	private static ArrayList<AnnotatedField> introspectClass(Class<?> objClass) {
		ArrayList<AnnotatedField> fields = new ArrayList<>();

		for (Field field : objClass.getDeclaredFields()) {
			// Grant access to private field
			field.setAccessible(true);

			if (field.isAnnotationPresent(XMLfield.class)) {
				XMLfield annotation = field.getAnnotation(XMLfield.class);

				// If the name is annotated get that, otherwise get the field name
				String annotationName = annotation.name();
				String fieldName = annotationName.equals("") ? field.getName() : annotationName;

				// Get the type of the annotation
				String fieldType = annotation.type();

				// Add the AnnotatedField object to the annotated fields
				fields.add(new AnnotatedField(field, fieldName, fieldType));
			}
		}

		return fields;
	}

	/**
	 * Read object and, for each field, write the xml line for it.
	 *
	 * @param obj    Object
	 * @param fields List of introspected annotated fields
	 * @return a string for the fields of the object obj
	 * @throws IllegalAccessException
	 */
	private static String getFields(Object obj, ArrayList<AnnotatedField> fields) throws IllegalAccessException {
		StringBuilder str = new StringBuilder();

		for (AnnotatedField annotatedField : fields) {

			Field field = annotatedField.getField();
			String name = annotatedField.getName();
			String type = annotatedField.getType();

			System.out.println("Field: " + name);

			// Append field name and type
			str.append("\t\t<" + name);
			str.append(" type=\"" + type + "\">");

			// Get field value
			Object fieldValue = field.get(obj);
			String value = fieldValue == null ? "" : fieldValue.toString();
			str.append(value);

			// Close the line
			str.append("</" + name + ">\n");
		}

		return str.toString();
	}
}
