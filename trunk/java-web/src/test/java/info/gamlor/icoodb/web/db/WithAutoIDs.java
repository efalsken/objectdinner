package info.gamlor.icoodb.web.db;

/**
 * @author roman.stoffel@gamlor.info
 * @since 10.08.2010
 */
public class WithAutoIDs {
    @AutoIncrement
    private int generatedIds;
    private int othterInt;

    public int getGeneratedIds() {
        return generatedIds;
    }

    public int getOthterInt() {
        return othterInt;
    }
}
