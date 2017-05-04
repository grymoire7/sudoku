package sudoku;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StringUtils {

    public static String center(String s, int size) {
        return center(s, size, ' ');
    }

    public static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        String sep = String.join("", Collections.nCopies( (size - s.length())/2 , ""+pad));
        sb.append(sep).append(s);
        sep = String.join("", Collections.nCopies( size - sb.length(), ""+pad));
        sb.append(sep);

        return sb.toString();
    }

    public static List<String> longestList(Collection<List<String>> slist) {

        Optional<List<String>> longest = slist.stream()
                .sorted( (e1, e2) -> -Integer.compare(e1.size(), e2.size()) )
                .findFirst();

        return longest.get();
    }

    public static String longestString(Collection<String> slist) {

        Optional<String> longest = slist.stream()
                .sorted( (e1, e2) -> -Integer.compare(e1.length(), e2.length()) )
                .findFirst();

        return longest.get();
    }
}
