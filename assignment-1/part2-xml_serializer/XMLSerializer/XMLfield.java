package XMLSerializer;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XMLfield {
	public String type();

	public String name() default "";
}
