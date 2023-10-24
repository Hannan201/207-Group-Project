package cypher.enforcers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 This annotation is used to mark a declared field as a simple service, so that
 it can be injected to make work for Dependency Injection. All fields
 with this annotation will be managed by another class and singletons are used
 to avoid excess memory usage. This means if two classes have the same type of
 declared field with this annotation, they both will share the same instance.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleService {

}
