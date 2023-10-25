package cypher.enforcers.injectors;

import cypher.enforcers.annotations.SimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

/*
 This class is used to inject services into an object. Used for
 dependency injection.
 */
public class Injector {

    /**
     * Attempt to inject a service into an object.
     *
     * @param object The object.
     * @param service The service that needs to be injected.
     * @param <T> The type of object.
     * @param <Q> The type of services.
     * @return True if the service was injected or is not
     * already null, false if no service is found in the object.
     */
    public <T, Q> boolean injectServicesInto(T object, Q service) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(SimpleService.class)) {
                field.setAccessible(true);

                if (!Objects.isNull(field.get(object))) {
                    continue;
                }

                if (!field.getType().isAssignableFrom(service.getClass())) {
                    return false;
                }

                field.set(object, service);
                return true;
            }
        }

        return false;
    }

}
