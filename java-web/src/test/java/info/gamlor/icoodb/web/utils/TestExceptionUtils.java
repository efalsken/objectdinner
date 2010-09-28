package info.gamlor.icoodb.web.utils;

import org.testng.annotations.Test;

/**
 * @author roman.stoffel@gamlor.info
 * @since 03.08.2010
 */
public class TestExceptionUtils {

    @Test(expectedExceptions = ExpectedException.class)
    public void throwsException(){
        noExceptionDeclared();
    }

    private void noExceptionDeclared() {
        try{
            exceptionDeclared();
        } catch(Exception e){
            ExceptionUtils.reThrow(e);
        }
    }

    private void exceptionDeclared() throws ExpectedException {
        throw new ExpectedException();
    }


    private static class ExpectedException extends Exception{

    }
}
