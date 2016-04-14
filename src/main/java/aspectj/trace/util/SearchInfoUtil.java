package aspectj.trace.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syc on 4/13/16.
 */
public class SearchInfoUtil {

    public enum Method{
        FUZZY,
        CLEAR
    }

    public String str;
    public List<Pair<String,Method>> path;

    public SearchInfoUtil(){
        path = new ArrayList<Pair<String, Method>>();
    }
}
