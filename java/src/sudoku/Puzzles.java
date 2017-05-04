package sudoku;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/*
 * In the string describing the puzzle, all characters are ignored that
 * are not in [1-9.0].  The '.' and '0' represent an unfilled square.
 * All other characters are there for human readability.
 *
 * For example, the first easy puzzle could be rendered more compactly as:
 * "..3.2.6..9..3.5..1..18.64....81.29..7.......8..67.82....26.95..8..2.3..9..5.1.3..";
 *
 * The solution string is similar, though '.' and '0' would be unexpected
 * in a solution.
 *
 * Unit tests may depend on some of these puzzles so add puzzles to the
 * end of the respective arrays when adding.
 */
public class Puzzles {

    private static final String[][] easy = {
        {   // Source: http://www.norvig.com/sudoku.html 
            " . . 3 | . 2 . | 6 . . " +
            " 9 . . | 3 . 5 | . . 1 " +
            " . . 1 | 8 . 6 | 4 . . " +
            "-------+-------+-------" +
            " . . 8 | 1 . 2 | 9 . . " +
            " 7 . . | . . . | . . 8 " +
            " . . 6 | 7 . 8 | 2 . . " +
            "-------+-------+-------" +
            " . . 2 | 6 . 9 | 5 . . " +
            " 8 . . | 2 . 3 | . . 9 " +
            " . . 5 | . 1 . | 3 . . ",

            " 4 8 3 | 9 2 1 | 6 5 7 " +
            " 9 6 7 | 3 4 5 | 8 2 1 " +
            " 2 5 1 | 8 7 6 | 4 9 3 " +
            "-------+-------+-------" +
            " 5 4 8 | 1 3 2 | 9 7 6 " +
            " 7 2 9 | 5 6 4 | 1 3 8 " +
            " 1 3 6 | 7 9 8 | 2 4 5 " +
            "-------+-------+-------" +
            " 3 7 2 | 6 8 9 | 5 1 4 " +
            " 8 1 4 | 2 5 3 | 7 6 9 " +
            " 6 9 5 | 4 1 7 | 3 8 2 "
        }
    };

    private static final String[][] hard = {
        {   // Source: http://elmo.sbs.arizona.edu/sandiway/sudoku/examples.html
            " . . . | 6 . . | 4 . . " +
            " 7 . . | . . 3 | 6 . . " +
            " . . . | . 9 1 | . 8 . " +
            "-------+-------+-------" +
            " . . . | . . . | . . . " +
            " . 5 . | 1 8 . | . . 3 " +
            " . . . | 3 . 6 | . 4 5 " +
            "-------+-------+-------" +
            " . 4 . | 2 . . | . 6 . " +
            " 9 . 3 | . . . | . . . " +
            " . 2 . | . . . | 1 . . ",

            " 5 8 1 | 6 7 2 | 4 3 9 " +
            " 7 9 2 | 8 4 3 | 6 5 1 " +
            " 3 6 4 | 5 9 1 | 7 8 2 " +
            "-------+-------+-------" +
            " 4 3 8 | 9 5 7 | 2 1 6 " +
            " 2 5 6 | 1 8 4 | 9 7 3 " +
            " 1 7 9 | 3 2 6 | 8 4 5 " +
            "-------+-------+-------" +
            " 8 4 5 | 2 1 9 | 3 6 7 " +
            " 9 1 3 | 7 6 8 | 5 2 4 " +
            " 6 2 7 | 4 3 5 | 1 9 8 "
        },
        {   // Source: Chicago Redeye, April 29th, 2017
            " . . . | . 9 . | . 8 . " +
            " . 7 1 | . 2 . | . . . " +
            " . . . | 3 5 . | 2 . 7 " +
            "-------+-------+-------" +
            " . . 4 | . . 9 | . . . " +
            " . 9 . | 4 1 8 | . 3 . " +
            " . . . | 6 . . | 5 . . " +
            "-------+-------+-------" +
            " 3 . 9 | . 8 5 | . . . " +
            " . . . | . 4 . | 8 5 . " +
            " . 1 . | . 6 . | . . . ",

            " 2 5 3 | 1 9 7 | 4 8 6 " +
            " 4 7 1 | 8 2 6 | 3 9 5 " +
            " 9 8 6 | 3 5 4 | 2 1 7 " +
            "-------+-------+-------" +
            " 6 2 4 | 5 3 9 | 1 7 8 " +
            " 5 9 7 | 4 1 8 | 6 3 2 " +
            " 1 3 8 | 6 7 2 | 5 4 9 " +
            "-------+-------+-------" +
            " 3 4 9 | 2 8 5 | 7 6 1 " +
            " 7 6 2 | 9 4 1 | 8 5 3 " +
            " 8 1 5 | 7 6 3 | 9 2 4 "
        },
        {   // Arto Inkala 2006 via Norvig
            " 8 5 . |. . 2 |4 . . " +
            " 7 2 . |. . . |. . 9 " +
            " . . 4 |. . . |. . . " +
            " ------+------+------" +
            " . . . |1 . 7 |. . 2 " +
            " 3 . 5 |. . . |9 . . " +
            " . 4 . |. . . |. . . " +
            " ------+------+------" +
            " . . . |. 8 . |. 7 . " +
            " . 1 7 |. . . |. . . " +
            " . . . |. 3 6 |. 4 . ",

            " 8 5 9 |6 1 2 |4 3 7 " +
            " 7 2 3 |8 5 4 |1 6 9 " +
            " 1 6 4 |3 7 9 |5 2 8 " +
            " ------+------+------" +
            " 9 8 6 |1 4 7 |3 5 2 " +
            " 3 7 5 |2 6 8 |9 1 4 " +
            " 2 4 1 |5 9 3 |7 8 6 " +
            " ------+------+------" +
            " 4 3 2 |9 8 1 |6 7 5 " +
            " 6 1 7 |4 2 5 |8 9 3 " +
            " 5 9 8 |7 3 6 |2 4 1 "
        },
        {   // Arto Inkala 2010 via Norvig
            " . . 5 |3 . . |. . . " +
            " 8 . . |. . . |. 2 . " +
            " . 7 . |. 1 . |5 . . " +
            " ------+------+------" +
            " 4 . . |. . 5 |3 . . " +
            " . 1 . |. 7 . |. . 6 " +
            " . . 3 |2 . . |. 8 . " +
            " ------+------+------" +
            " . 6 . |5 . . |. . 9 " +
            " . . 4 |. . . |. 3 . " +
            " . . . |. . 9 |7 . . ",

            " 1 4 5 |3 2 7 |6 9 8 " +
            " 8 3 9 |6 5 4 |1 2 7 " +
            " 6 7 2 |9 1 8 |5 4 3 " +
            " ------+------+------" +
            " 4 9 6 |1 8 5 |3 7 2 " +
            " 2 1 8 |4 7 3 |9 5 6 " +
            " 7 5 3 |2 9 6 |4 8 1 " +
            " ------+------+------" +
            " 3 6 7 |5 4 2 |8 1 9 " +
            " 9 8 4 |7 6 1 |2 3 5 " +
            " 5 2 1 |8 3 9 |7 6 4 "
        }
    };

    public static final List<String> easyPuzzles;
    public static final List<String> easySolutions;
    public static final List<String> hardPuzzles;
    public static final List<String> hardSolutions;

    static {
        List<String> e  = new LinkedList<String>();
        List<String> es = new LinkedList<String>();
        for (String[] s: easy) {
            e.add(s[0]); es.add(s[1]);
        }
        easyPuzzles   = Collections.unmodifiableList(e);
        easySolutions = Collections.unmodifiableList(es);

        List<String> h  = new LinkedList<String>();
        List<String> hs = new LinkedList<String>();
        for (String[] s: hard) {
            h.add(s[0]); hs.add(s[1]);
        }
        hardPuzzles   = Collections.unmodifiableList(h);
        hardSolutions = Collections.unmodifiableList(hs);
    }

}
