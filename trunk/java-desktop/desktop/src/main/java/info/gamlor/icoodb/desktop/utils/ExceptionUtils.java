package info.gamlor.icoodb.desktop.utils;

/**
 * @author roman.stoffel@gamlor.info
 * @since 19.07.2010
 */
public final class ExceptionUtils {
    private ExceptionUtils(){}

    public static <TException extends Throwable,TReturnValue> TReturnValue reThrow(TException exception){
        return ExceptionUtils.<RuntimeException,TReturnValue>reThrowInternal(exception);
    }

    private static <TException extends Throwable,TReturnValue> TReturnValue reThrowInternal(Throwable exception) throws TException {
        throw (TException) exception;
    }
}
