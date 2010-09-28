package info.gamlor.icoodb.web.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.web.util.HtmlUtils;

import java.beans.PropertyDescriptor;

import static info.gamlor.icoodb.web.utils.ExceptionUtils.reThrow;

/**
 * @author roman.stoffel@gamlor.info
 * @since 17.08.2010
 */
public class EncodeHtml {
    public static <T>  T encode(T obj){
        Class type = obj.getClass();
        final PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(type);
        for (PropertyDescriptor property : properties) {
            encodeSpringProperties(obj, property);
        }
        return obj;
    }

    private static <T> void encodeSpringProperties(Object obj, PropertyDescriptor property) {
        if(isStringProperty(property)){
            copyAndEncode(obj, property);
        }
    }

    private static void copyAndEncode(Object obj, PropertyDescriptor property) {
        try {
            String value = (String)property.getReadMethod().invoke(obj);
            String encodedValue = HtmlUtils.htmlEscape(value);
            property.getWriteMethod().invoke(obj, encodedValue);
        } catch (Exception e) {
            reThrow(e);
        }
    }

    private static boolean isStringProperty(PropertyDescriptor property) {
        return property.getPropertyType().equals(String.class);
    }
}
