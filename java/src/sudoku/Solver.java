package sudoku;
/**
 *  Sudoku solver
 *
 *  This is an interpretation of Peter Norvig's sudoku solver (python) for java.
 *
 *      Norvig notation            | Java notation
 *     ----------------------------+------------------------------------
 *      parse_grid                 | createBoard
 *      grid_values                | parseGrid
 *      values                     | board
 *
 *  Some vocabulary:
 *
 *  A sudoku board has 81 square, 9 rows, 9 columns, and 9 blocks of 9 squares.
 *
 *  Peers for a square are all squares that are members of the units that this
 *  square is a member of.
 *
 *  Units are those blocks in a sudoku puzzle that must contain all the digits 1-9.
 *  That is, rows, columns, and blocks.
 *
 *  @author Tracy Atteberry
 *  @ref    Norvig's solver:   http://www.norvig.com/sudoku.html
 *  @ref    Annotated Norvig:  https://medium.com/towards-data-science/peter-norvigs-sudoku-solver-25779bb349ce
 *  @ref    Board examples:    http://elmo.sbs.arizona.edu/sandiway/sudoku/examples.html
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solver {

    static final int                        SIZE = 9;
    static List<Character>                digits = Arrays.asList('1','2','3','4','5','6','7','8','9');
    static List<Character>                  rows = Arrays.asList('A','B','C','D','E','F','G','H','I');
    static List<Character>                  cols = digits;
    static List<String>                  squares = Solver.cross(Solver.rows, Solver.cols);
    static List<List<String>>           unitList = Solver.unitListBuilder();
    static Map<String, List<List<String>>> units = Solver.unitsBuilder();
    static Map<String, List<String>>       peers = Solver.peersBuilder();

    /* Builds a list of peers for each square. */
    private static Map<String, List<String>> peersBuilder() {
        Map<String, List<String>> peers = new HashMap<String, List<String>>();

        for (String s: squares) {
            List<List<String>> sunits = units.get(s);
            HashSet<String> hs = new HashSet<String>();
            for (List<String> u: sunits) {
                for (String t: u) {
                    if (!t.equals(s))
                        hs.add(t);
                }
            }
            List<String> ls = new ArrayList<String>();
            ls.addAll(hs);
            peers.put(s, ls);
        }

        return peers;
    }

    /* Builds a list of units for each square. */
    private static Map<String, List<List<String>>> unitsBuilder() {
        Map<String, List<List<String>>> units = new HashMap<String, List<List<String>>>();

        for (String s: squares) {
            unitList.stream()
                    .filter(u -> u.contains(s))
                    .forEach(u -> {
                        if (!units.containsKey(s)) {
                            units.put(s, new ArrayList<List<String>>() );
                        }
                        units.get(s).add(u);
                    });
        }
        return units;
    }

    /* Builds a list of all units for the sudoku board. */
    private static List<List<String>> unitListBuilder() {
        List<List<String>> unitList = new ArrayList<List<String>>();
        List<String> u;

        // row units
        for (Character r : rows) {
            u = new ArrayList<String>();
            for (Character c : cols) {
                u.add(""+r+c);
            }
            unitList.add(u);
        }

        // column units
        for (Character c : cols) {
            u = new ArrayList<String>();
            for (Character r : rows) {
                u.add(""+r+c);
            }
            unitList.add(u);
        }

        //  [cross(rs, cs) for rs in ('ABC','DEF','GHI') for cs in ('123','456','789')])
        List<List<Character>> rowunits = new LinkedList<List<Character>>(Arrays.asList(
                new LinkedList<Character>(Arrays.asList('A', 'B', 'C')),
                new LinkedList<Character>(Arrays.asList('D', 'E', 'F')),
                new LinkedList<Character>(Arrays.asList('G', 'H', 'I'))
                ));
        List<List<Character>> colunits = new LinkedList<List<Character>>(Arrays.asList(
                new LinkedList<Character>(Arrays.asList('1', '2', '3')),
                new LinkedList<Character>(Arrays.asList('4', '5', '6')),
                new LinkedList<Character>(Arrays.asList('7', '8', '9'))
                ));

        for (List<Character> rl : rowunits) {
            for (List<Character> cl : colunits) {
                u = Solver.cross( rl, cl );
                unitList.add(u);
            }
        }

        return unitList;
    }

    /*
     * Parse the puzzle definition string into a grid map.
     * Map each square to a value.
     * e.g.  "A1" -> "1", "A2" -> ".", etc.
     *
     * This method is called grid_values in Norvig's code.
     */
    public static Map<String, List<String>> parseGrid(String puzzle) {
        if (null == puzzle)
            return null;

        List<String> in = Arrays.asList(puzzle.split("(?!^)")).stream()  // create a character stream
                .filter(c -> c.matches("[\\d\\.]"))                      // grab digits and '.'s
                .collect(Collectors.toList());                           // reassemble a list

        assert(in.size() == SIZE*SIZE);

        Map<String, List<String>> grid = IntStream.range(0, squares.size()).boxed()
                .collect(Collectors.toMap(i -> squares.get(i), i -> new LinkedList<String>(Arrays.asList(in.get(i))) ));

        return grid;
    }

    /**
     *  Convert grid to a map of possible values, {square: digits}, or
     *  return null if a contradiction is detected.
     *
     *  The Map<String, List<String>> represent our working board.  It's a map
     *  between grid squares (e.g. A2, B7) to a list of possible values for
     *  that square (e.g. ["2", "5", "8" ]).
     */
    public static Map<String, List<String>> createBoard(Map<String, List<String>> grid) {
        if (null == grid)
            return null;

        Map<String, List<String>> board = new HashMap<String, List<String>>();

        // Initialize board values to all possibilities.
        for (String s: squares) {
            board.put(s, new LinkedList<String>(Arrays.asList("1","2","3","4","5","6","7","8","9")));
        }

        // assign values from input grid
        for (Map.Entry<String, List<String>> entry : grid.entrySet()) {
            String square = entry.getKey();
            String digit = entry.getValue().get(0);
            if ( Character.isDigit(digit.charAt(0)) ) {
                board = assign(board, square, digit);
                if ( null == board)
                    break;
            }
        }

        return board;
    }

    /**
     * Eliminate all the other values (except d) from board[s] and propagate.
     * Return values, except return null if a contradiction is detected.
     */
    public static Map<String, List<String>> assign(Map<String, List<String>> board, String s, String d) {
        // System.out.println("Assigning "+d+" to "+s);
        for (String e: board.get(s)) {
            if (!e.equals(d)) {               // eliminate all except d
                // System.out.println("    Tossing: " + e);
                board = eliminate(board, s, e);
                if (null == board)
                    break;
            }
        }
        return board;
    }

    /* Eliminate digit from square and propagate the changes. */
    public static Map<String, List<String>> eliminate(Map<String, List<String>> inBoard, String s, String d) {

        Map<String, List<String>> board = DeepClone.deepClone(inBoard);

        // If the square doesn't contain d then it's already been eliminated.  Bail early.
        if (!board.get(s).contains(d))
            return board;

        // Eliminate d from the square s.
        board.get(s).remove(d);

        // If the square s has no values then contradiction, return null.
        if (0 == board.get(s).size())
            return null;

        // If the square s has only one value then eliminate that value from all peer squares.
        if (1 == board.get(s).size()) {
            // System.out.println(s + " is down to 1 value: " + board.get(s).get(0));
            String d2 = board.get(s).get(0);
            for (String s2: peers.get(s)) {
                board = eliminate(board, s2, d2);
                if (null == board)
                    return null;
            }
        }

        // If a unit u of s is reduced to only one place for d then assign it there. (recursive call)
        for (List<String> u: units.get(s)) {
            u.size();
            //    dplaces = [s for s in u if d in values[s]]
            List<String> dplaces = new ArrayList<String>();
            for (String s2: u) {                    // check every square in this unit
                List<String> dlist = board.get(s2); // list of digits for this square on the board
                if (dlist.contains(d)) {            // if the digit is a possibility the increment dplaces
                    dplaces.add(s2);
                }
            }

            // If a unit u of s has no places for d then contradiction. Return null.
            if (0 == dplaces.size()) {
                return null;
            }
            else if (1 == dplaces.size()) {
                //    # d can only be in one place in unit; assign it there
                //    if not assign(values, dplaces[0], d):
                //        return False
                board = assign(board, dplaces.get(0), d);
                if (null == board)
                    return null;
            }
        }
        return board;
    }

    public static Map<String, List<String>> solve(String grid) {
        return search( createBoard( parseGrid(grid) ) );
    }

    /* Search for a solution. */
    public static Map<String, List<String>> search(Map<String, List<String>> board) {

        if (null == board)  // failed earlier
            return null;

        // Check for solved state.
        boolean solved = true;
        for (List<String> slist: board.values()) {
            if (slist.size() != 1) {
                solved = false;
                break;
            }
        }
        if (solved)
            return board;  // Solved!

        // Chose the unfilled square s with the fewest possibilities
        Optional<Entry<String, List<String>>> a = board.entrySet().stream()
            .filter(map -> map.getValue().size() > 1)
            .sorted( (e1, e2) -> e1.getValue().size() - e2.getValue().size() )
            // .peek(s -> System.out.println(s) )
            .findFirst();
        String sprop = a.get().getKey();  // square to propagate
        // System.out.println("Should be small (2 or 3): " + a.get().getValue()); // check on sort

        // try all possibilities for this square
        Map<String, List<String>> b;
        for (String d: a.get().getValue()) {
            b = search( assign(board, sprop, d) );
            if (null != b)
                return b;  // return first (hopefully only) successful search
        }
        return null;       // search failed
    }

    public static void display(Map<String, List<String>> board) {
        if (null == board) {
            System.out.println("No solution!  Something went wrong.");
            return;
        }

        List<String> slist = StringUtils.longestList((board.values()));
        String ls = String.join("", slist);
        int width = 1 + ls.length();
        String sep = String.join("", Collections.nCopies(width*3 + 1, "-"));

        System.out.print("\n");
        for (Character r: rows) {
            System.out.print(" ");
            for (Character c: cols) {
                String key = "" + r + c;
                String cell = StringUtils.center(String.join("", board.get(key)), width);
                System.out.print(cell);
                if (c == '3' || c == '6')
                    System.out.print("| ");
            }
            System.out.print("\n");
            if (r == 'C' || r == 'F')
                System.out.print( String.join("+", Collections.nCopies(3, sep)) + "\n");
        }
        System.out.print("\n");
    }

    public static List<String> cross(List<Character> a, List<Character> b) {
        List<String> c = new ArrayList<>();
        a.forEach( s -> b.forEach( t -> c.add( "" + s + t ) ) );
        return c;
    }

    public static void main(String[] args) {

        // TODO: Add puzzle input code.
        String inputPuzzle = Puzzles.hardPuzzles.get(3);
        String targetSolution = Puzzles.hardSolutions.get(3);

        System.out.println("Input puzzle:");
        Solver.display(Solver.parseGrid(inputPuzzle));

        Map<String, List<String>> solution = Solver.solve(inputPuzzle);

        System.out.println("Found solution:");
        Solver.display(solution);

        if ( TestSudoku.boardEquality(solution, Solver.parseGrid(targetSolution)) ) {
            System.out.println("Found solution matches the target solution!");
        }
        else {
            System.out.println("Yikes! Found solution does not matche the target solution!");
        }

        // Display test for intermediate boards.  Useful in debugging.
        // solution.get("B2").add("5");
        // solution.get("B2").add("7");
        // System.out.println("Display test:");
        // Solver.display(solution);
    }
}
