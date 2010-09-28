package info.gamlor.icoodb.desktop.logic;

/**
 * @author roman.stoffel@gamlor.info
 * @since 26.07.2010
 */
public interface BusinessValidator<T> {
    public boolean isValid(T value);
}
