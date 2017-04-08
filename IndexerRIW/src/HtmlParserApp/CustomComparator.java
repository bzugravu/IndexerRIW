package HtmlParserApp;

import java.util.Comparator;

public class CustomComparator implements Comparator<ResultDocument> {
    @Override
    public int compare(ResultDocument o1, ResultDocument o2) {
        return o1.distance > o2.distance ? -1 : 1;
    }
}