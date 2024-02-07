package XMLSerializer;

import java.lang.reflect.Field;

public class AnnotatedField {
	private final Field field;
	private final String name;
	private final String type;

	public AnnotatedField(Field field, String name, String type) {
		this.field = field;
		this.name = name;
		this.type = type;
	}

	public Field getField() {
		return field;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

}
