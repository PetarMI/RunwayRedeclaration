import Model.Obstacle;
import Model.Runway;
import Model.Strip;
import Model.Values;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class RunwayTests {
    //Strip 1
    private Values str1Values = new Values(3660, 3660, 3660, 3353);
    private Strip str1 = new Strip("09L", 9, "L", str1Values, 306);
    //Strip2
    private Values str2Values = new Values(3660, 3660, 3660, 3660);
    private Strip str2 = new Strip("27R", 27, "R", str2Values, 0);
    //Runway and obstacle
    private Runway runway = new Runway("09L/27R", str1, str2);
    ;
    private Obstacle obstacle = new Obstacle("potato", 15, 5, 10, "big potato");

    @Test
    public void testDistanceOnCentreline() {
        this.runway.addObstacle(this.obstacle, 322, 3103, 0, Runway.OBSTACLE_ABOVE);
        assertEquals(this.runway.getDistanceFromCentrelineFor3D(), 0);
    }

    @Test
    public void testDistanceAboveCentrelineClose() {
        this.runway.addObstacle(this.obstacle, 322, 3103, 1, Runway.OBSTACLE_ABOVE);
        assertEquals(this.runway.getDistanceFromCentrelineFor3D(), 1);
    }

    @Test
    public void testDistanceBelowCentrelineClose() {
        this.runway.addObstacle(this.obstacle, 322, 3103, 1, Runway.OBSTACLE_BELOW);
        assertEquals(this.runway.getDistanceFromCentrelineFor3D(), -1);
    }

    @Test
    public void testDistanceAboveCentreline() {
        this.runway.addObstacle(this.obstacle, 322, 3103, 50, Runway.OBSTACLE_ABOVE);
        assertEquals(this.runway.getDistanceFromCentrelineFor3D(), 50);
    }

    @Test
    public void testDistanceBelowCentreline() {
        this.runway.addObstacle(this.obstacle, 322, 3103, 50, Runway.OBSTACLE_BELOW);
        assertEquals(this.runway.getDistanceFromCentrelineFor3D(), -50);
    }

    @Test
    public void testCompassHeadingLowRunway()
    {
        this.runway = new Runway("01L/19R", str1, str2);
        assertEquals (this.runway.getCompassHeading(), 80);
    }

    @Test
    public void testCompassHeadingCloseZeroAbove()
    {
        this.runway = new Runway("08L/26R", str1, str2);
        assertEquals (this.runway.getCompassHeading(), 10);
    }

    @Test
    public void testCompassHeadingCloseZeroBelow()
    {
        this.runway = new Runway("10L/28R", str1, str2);
        assertEquals (this.runway.getCompassHeading(), -10);
    }

    @Test
     public void testCompassHeadingHighRunway()
    {
        this.runway = new Runway("17L/35R", str1, str2);
        assertEquals (this.runway.getCompassHeading(), -80);
    }

    @Test
    public void testCompassHeadingVertical()
    {
        this.runway = new Runway("18L/36R", str1, str2);
        assertEquals (this.runway.getCompassHeading(), -90);
    }
}
