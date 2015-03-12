import Model.Obstacle;
import Model.Runway;
import Model.Strip;
import Model.Values;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created on 08/03/2015
 */
public class MathTestRecalculation {

    //Strip 1
    private Values str1Values;
    private Strip str1;
    //Strip2
    private Values str2Values;
    private Strip str2;
    //Runway and obstacle
    private Runway runway;
    private Obstacle obstacle;
    private static final int BLAST_ALLOWANCE = 300;

    @Before
    public void setUp() {
        str1Values = new Values(3660, 3660, 3660, 3353);
        str1 = new Strip("09L", 9, "L", str1Values, 306);

        str2Values = new Values(3660, 3660, 3660, 3660);
        str2 = new Strip("27R", 27, "R", str2Values, 0);

        runway = new Runway("09L/27R", str1, str2);
    }

    @Test
    public void testCentrelineWithin(){
        //only care about width
        obstacle = new Obstacle("plane", 4, 10, 5, "");
        runway.addObstacle(obstacle, -50, 3646, 75, "Above");
        runway.recalculateValues(BLAST_ALLOWANCE);
        assertFalse(runway.getStrip1().getOrigVal().equals(runway.getStrip1().getRecVal()));
        assertFalse(runway.getStrip2().getOrigVal().equals(runway.getStrip2().getRecVal()));
    }

    @Test
    public void testCentrelineOn(){
        //only care about width
        obstacle = new Obstacle("plane", 4, 10, 5, "");
        runway.addObstacle(obstacle, -50, 3646, 77, "Above");
        runway.recalculateValues(BLAST_ALLOWANCE);
        assertFalse(runway.getStrip1().getOrigVal().equals(runway.getStrip1().getRecVal()));
        assertFalse(runway.getStrip2().getOrigVal().equals(runway.getStrip2().getRecVal()));
    }

    @Test
    public void testCentrelineBeyond(){
        //only care about width
        obstacle = new Obstacle("plane", 4, 10, 5, "");
        runway.addObstacle(obstacle, -50, 3646, 78, "Above");
        runway.recalculateValues(BLAST_ALLOWANCE);
        assertTrue(runway.getStrip1().getOrigVal().equals(runway.getStrip1().getRecVal()));
        assertTrue(runway.getStrip2().getOrigVal().equals(runway.getStrip2().getRecVal()));
    }

    @Test
    public void testStripendWithin(){
        //only care about length
        obstacle = new Obstacle("plane", 5, 10, 4, "");
        runway.addObstacle(obstacle, -50, 3646, 20, "Above");
        runway.recalculateValues(BLAST_ALLOWANCE);
        assertFalse(runway.getStrip1().getOrigVal().equals(runway.getStrip1().getRecVal()));
        assertFalse(runway.getStrip2().getOrigVal().equals(runway.getStrip2().getRecVal()));
    }

    @Test
    public void testStripendLeftOn(){
        //only care about length
        obstacle = new Obstacle("plane", 5, 10, 4, "");
        runway.addObstacle(obstacle, -368, 3646, 20, "Above");
        runway.recalculateValues(BLAST_ALLOWANCE);
        assertFalse(runway.getStrip1().getOrigVal().equals(runway.getStrip1().getRecVal()));
        assertFalse(runway.getStrip2().getOrigVal().equals(runway.getStrip2().getRecVal()));
    }

    @Test
    public void testStripendRightOn(){
        //only care about length
        obstacle = new Obstacle("plane", 5, 10, 4, "");
        runway.addObstacle(obstacle, 3200, -62, 20, "Above");
        runway.recalculateValues(BLAST_ALLOWANCE);
        assertFalse(runway.getStrip1().getOrigVal().equals(runway.getStrip1().getRecVal()));
        assertFalse(runway.getStrip2().getOrigVal().equals(runway.getStrip2().getRecVal()));
    }

    @Test
    public void testStripendLeftBeyond(){
        //only care about length
        obstacle = new Obstacle("plane", 5, 10, 4, "");
        runway.addObstacle(obstacle, -369, 3646, 20, "Below");
        runway.recalculateValues(BLAST_ALLOWANCE);
        assertTrue(runway.getStrip1().getOrigVal().equals(runway.getStrip1().getRecVal()));
        assertTrue(runway.getStrip2().getOrigVal().equals(runway.getStrip2().getRecVal()));
    }

    @Test
    public void testStripendRightBeyond(){
        //only care about length
        obstacle = new Obstacle("plane", 5, 10, 4, "");
        runway.addObstacle(obstacle, 3300, -63, 20, "Below");
        runway.recalculateValues(BLAST_ALLOWANCE);
        assertTrue(runway.getStrip1().getOrigVal().equals(runway.getStrip1().getRecVal()));
        assertTrue(runway.getStrip2().getOrigVal().equals(runway.getStrip2().getRecVal()));
    }
}
