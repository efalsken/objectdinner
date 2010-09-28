package info.gamlor.icoodb.web.utils;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertFalse;

/**
 * @author roman.stoffel@gamlor.info
 * @since 17.08.2010
 */
public class TestEncodeHtml {

    @Test
    public void returnsBean(){
        Bean bean = createBean();
        Bean encoded = EncodeHtml.encode(bean);
        assertNotNull(encoded);
    }
    @Test
    public void isEncoded(){
        Bean bean = createBean();
        Bean encoded = EncodeHtml.encode(bean);
        assertEncoded(encoded.getStrValue1());
        assertEncoded(encoded.getStrValue2());
    }

    private void assertEncoded(String property) {
        assertFalse(property.contains("<"));
        assertFalse(property.contains("<"));
    }

    private Bean createBean() {
        return new Bean();
    }

    public class Bean{
        private String strValue1 = "value 1 <html> <html/> <hi>";
        private String strValue2 =  "<html> <html/> <hi>";
        private int intValue = 2;

        public String getStrValue1() {
            return strValue1;
        }

        public void setStrValue1(String strValue1) {
            this.strValue1 = strValue1;
        }

        public String getStrValue2() {
            return strValue2;
        }

        public void setStrValue2(String strValue2) {
            this.strValue2 = strValue2;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }
    }
}
