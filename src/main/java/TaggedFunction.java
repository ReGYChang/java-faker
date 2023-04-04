import java.lang.reflect.Field;

/**
 * @author regy
 */
public interface TaggedFunction {
    Object generate(Field field) throws Exception;
}
