import Model.Obstacle;
import Model.Runway;
import Model.Strip;
import Model.Values;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created on 9/3/2015
 */
public class MathTestRecValues {

    //Strip 1
    private Values str1Values;
    private Strip str1;
    //Strip2
    private Values str2Values;
    private Strip str2;
    //Runway and obstacle
    private Runway runway;
    private Obstacle obstacle;
    private Values expectedValues;
    private static final int BLAST_ALLOWANCE = 300;
    private static final int DEFAULT_VALUE = 10;

    @Before
    public void setUp() {
    }

    @Test
    public void landOverBeforeMiddle() {
        //runway specs
        str1Values = new Values(3902, 3902, 3902, 3595);
        str1 = new Strip("09L", 9, "L", str1Values, 306);
        str2Values = new Values(3884, 3884, 3962, 3884);
        str2 = new Strip("27R", 27, "R", str2Values, 0);
        runway = new Runway("09L/27R", str1, str2);
        //obstacle to test
        obstacle = new Obstacle("plane", 4, 12, 5, "");
        runway.addObstacle(obstacle, -50, 3646, 0);
        runway.recalculateValues(BLAST_ALLOWANCE);

        //expected values
        expectedValues = new Values(DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE, 2982);
        expectedValues.setLanding("Land over");
        System.out.println(runway.getStrip1().getRecVal());
        System.out.println(runway.getStrip1().getRecVal().getLda());
        assertTrue(runway.getStrip1().getRecVal().getLda() == expectedValues.getLda());
        assertTrue(runway.getStrip1().getRecVal().getLanding().equals(expectedValues.getLanding()));
    }
}
