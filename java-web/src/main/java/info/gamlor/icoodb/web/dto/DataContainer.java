package info.gamlor.icoodb.web.dto;

import info.gamlor.icoodb.web.utils.EncodeHtml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author roman.stoffel@gamlor.info
 * @since 10.08.2010
 */
public class DataContainer<T> {
    private List<T> rows = new ArrayList<T>();
    private int results = 0;

    public DataContainer(List<T> data) {
        this.rows.addAll(data);
        results = rows.size();
    }

    public static <T> DataContainer<T> createUnEncoded(T data){
        return new DataContainer(Arrays.asList(data));
    }
    public static <T> DataContainer<T> create(List<T> data){
        for (T t : data) {
            EncodeHtml.encode(t);
        }
        return new DataContainer(data);
    }

    public List<T> getRows() {
        return rows;
    }

    public int getResults() {
        return results;
    }
}
