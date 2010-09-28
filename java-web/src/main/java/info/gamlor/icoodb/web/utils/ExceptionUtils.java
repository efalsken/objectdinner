package info.gamlor.icoodb.web.utils;

/**
 * @author roman.stoffel@gamlor.info
 * @since 03.08.2010
 */
public class ExceptionUtils { 
    private ExceptionUtils(){}

    public static <TException extends Throwable,TReturnValue> TReturnValue reThrow(TException exception){
        return ExceptionUtils.<RuntimeException,TReturnValue>reThrowInternal(exception);
    }

    private static <TException extends Throwable,TReturnValue> TReturnValue reThrowInternal(Throwable exception) throws TException {
        throw (TException) exception;
    }
}
