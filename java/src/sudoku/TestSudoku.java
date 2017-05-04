package sudoku;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.Ignore;

public class TestSudoku {
    
    @Test
    public void testStatics() {
        assertTrue( 81 == Solver.units.size() );
        assertTrue( 81 == Solver.peers.size() );
        assertTrue( 20 == StringUtils.center("howdy", 20, '*').length() );
        assertTrue( 20 == StringUtils.center("a", 20, '*').length() );
        assertTrue( 20 == StringUtils.center("ab", 20, '*').length() );

        Map<String, String> m = Solver.squares.stream().collect(Collectors.toMap(k -> k, v -> v));
        assert(m.get("A1").equals("A1"));
        assert(m.get("B8").equals("B8"));
    }

    @Test
    public void testParseGrid() {
        String p = Puzzles.easyPuzzles.get(0);
        Map<String, List<String>> g = Solver.parseGrid(p);
        assertTrue( "9".equals(g.get("B1").get(0)) );
        assertTrue( ".".equals(g.get("A1").get(0)) );
        assertTrue( "5".equals(g.get("I3").get(0)) );

        p = Puzzles.hardPuzzles.get(0);
        Map<String, List<String>> h = Solver.parseGrid(p);
        assertTrue( "7".equals(h.get("B1").get(0)) );
        assertTrue( "2".equals(h.get("I2").get(0)) );
        assertTrue( "9".equals(h.get("H1").get(0)) );
        assertTrue( "3".equals(h.get("E9").get(0)) );
    }

    @Test
    public void testCreateBoard() {

        // First the easy way.
        String p = Puzzles.easyPuzzles.get(0);
        String s = Puzzles.easySolutions.get(0);

        Map<String, List<String>> g = Solver.parseGrid(p);
        Map<String, List<String>> b = Solver.createBoard(g);

        assertTrue( b.get("A1").contains("4") );
        assertTrue( b.get("B1").contains("9") );
        assertTrue( "9".equals(b.get("B1").get(0)) );

        // Turns out the easy puzzle is solved after creation process.
        // It's just that easy.
        assertTrue(boardEquality(b, Solver.parseGrid(s)));

        // Now the hard way.
        p = Puzzles.hardPuzzles.get(0);
        s = Puzzles.hardSolutions.get(0);

        g = Solver.parseGrid(p);
        b = Solver.createBoard(g);

        assertTrue( b.get("B1").contains("7") );
        assertTrue( 1 == b.get("B1").size() );   // and only 7

        assertTrue( b.get("A1").containsAll( Arrays.asList("1", "2", "3", "5", "8") ));
        assertTrue( 5 == b.get("A1").size() );   // and only members of that list
    }

    @Ignore // hard to test in isolation
    @Test
    public void testAssign() {
        fail("Not yet implemented");
    }

    @Ignore // hard to test in isolation
    @Test
    public void testEliminate() {
        fail("Not yet implemented");
    }
    
    public static List<String> diffBoard(Map<String, List<String>> a, Map<String, List<String>> b) {
        List<String> diff = new LinkedList<String>();
        Solver.squares.stream().forEach( s -> {
            if ( !( a.get(s).containsAll( b.get(s) )) || !( b.get(s).containsAll( a.get(s) )) ) {
                diff.add(  s + ": " + a.get(s) + " != " + b.get(s) );
            }
        });
        return diff;
    }
    
    public static boolean boardEquality(Map<String, List<String>> a, Map<String, List<String>> b) {
        List<String> diff = diffBoard(a, b);
        
        // If there is a difference then display it before we fail.
        if (diff.size() > 0) {
            System.out.println("Board diff:");
            diff.stream().forEach(s -> System.out.println(s));
        }
        return ( 0 == diff.size() );
    }

    @Test
    public void testSolve() {
        
        Map<String, List<String>> solution;
        Iterator<String> pIt, sIt;

        // Test solutions for our easy puzzles.
        pIt = Puzzles.easyPuzzles.iterator();
        sIt = Puzzles.easySolutions.iterator();
        while (pIt.hasNext() && sIt.hasNext()) {
            String p = pIt.next();
            String s = sIt.next();
            solution = Solver.solve(p);
            assertTrue(boardEquality(solution, Solver.parseGrid(s)));
        }

        // Test solutions for our hard puzzles.
        pIt = Puzzles.hardPuzzles.iterator();
        sIt = Puzzles.hardSolutions.iterator();
        while (pIt.hasNext() && sIt.hasNext()) {
            String p = pIt.next();
            String s = sIt.next();
            solution = Solver.solve(p);
            assertTrue(boardEquality(solution, Solver.parseGrid(s)));
        }
    }

    @Ignore // Solve just calls search, so this is covered.
    @Test
    public void testSearch() {
        fail("Not yet implemented");
    }

    @Test
    public void testCross() {
        List<String> c = Solver.cross(Solver.rows, Solver.cols);
        assertTrue(81 == c.size());
        assertTrue(c.get(0).equals("A1"));
        assertTrue(c.get(1).equals("A2"));
        assertTrue(c.get(80).equals("I9"));
    }

}
