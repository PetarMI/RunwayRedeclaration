import Model.*;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static junit.framework.TestCase.assertTrue;

public class MathStepDefinitions {

    private Strip strip1, strip2;
    private Runway runway;
    private Values trueValues;
    private String runwayID;
    private Obstacle obs;
    private MathHandler maths;
    private int blastAllowance;


    @Given("^The operator has the values for the runway (.*)$")
    public void The_operator_has_the_values_for_the_runway(String runwayID) {

        this.runwayID = runwayID;
    }

    @And("^The values1 for (.*) are (.*), (.*), (.*), (.*), (.*), (.*) and (.*)$")
    public void The_values_for_Strip1
            (String stripId, String tora, String toda, String asda, String lda, String orientation, String position, String threshold){
        Values origVal = new Values(Integer.valueOf(tora), Integer.valueOf(asda), Integer.valueOf(toda), Integer.valueOf(lda));
        strip1 =  new Strip(stripId, Integer.valueOf(orientation), position, origVal, Integer.valueOf(threshold));
    }

    @And("^The values2 for (.*) are (.*), (.*), (.*), (.*), (.*), (.*) and (.*)$")
    public void The_values_for_Strip2
            (String stripId, String tora, String toda, String asda, String lda, String orientation, String position, String threshold){
        Values origVal = new Values(Integer.valueOf(tora), Integer.valueOf(asda), Integer.valueOf(toda), Integer.valueOf(lda));
        strip2 = new Strip(stripId, Integer.valueOf(orientation), position, origVal, Integer.valueOf(threshold));
        runway = new Runway(runwayID, strip1, strip2);
    }

    @When("^He adds an obstacle (-?\\d+) m from the left and (-?\\d+) m from the right, of height (\\d+) and (-?\\d+) from the centreline$")
    public void He_adds_an_obstacle_(int posLeft, int posRight, int height, int distCentral) {
        obs = new Obstacle("test", 1, height, 1, "test");
        runway.addObstacle(obs, posLeft, posRight,distCentral);
    }

    @And("^with blast allowance (\\d+)$")
    public void with_blast_allowance(int blastAllowance){
        this.blastAllowance = blastAllowance;
    }
    @Then("^The recalculated values for (.*) should be (.*), (.*), (.*), (.*)$")
    public void The_recalculated_values_for_Strip1(String stripId, String tora, String toda, String asda, String lda) {
        runway.recalculateValues(blastAllowance);
        trueValues = new Values(Integer.valueOf(tora), Integer.valueOf(asda), Integer.valueOf(toda), Integer.valueOf(lda));
        System.out.println(runway.getStrip1().getRecVal().getTora());
        System.out.println(runway.getStrip1().getRecVal().getToda());
        System.out.println(runway.getStrip1().getRecVal().getAsda());
        System.out.println(runway.getStrip1().getRecVal().getLda());
        maths = new MathHandler(runway);

        //test each value for the first strip
        assertTrue("Recalculated TORA for the first strip is not correct", trueValues.getTora() == runway.getStrip1().getRecVal().getTora());
        assertTrue("Recalculated TODA for the first strip is not correct", trueValues.getToda() == runway.getStrip1().getRecVal().getToda());
        assertTrue("Recalculated ASDA for the first strip is not correct", trueValues.getAsda() == runway.getStrip1().getRecVal().getAsda());
        assertTrue("Recalculated LDA for the first strip is not correct", trueValues.getLda() == runway.getStrip1().getRecVal().getLda());

        assertTrue("Recalculated values are not the same", trueValues.equals(runway.getStrip1().getRecVal()));


//        assertTrue("Recalculated TORA for the second strip is not correct", trueValues.getTora() == runway.getStrip1().getRecVal().getTora());
//        assertTrue("Recalculated TODA for the second strip is not correct", trueValues.getToda() == runway.getStrip1().getRecVal().getToda());
//        assertTrue("Recalculated ASDA for the second strip is not correct", trueValues.getAsda() == runway.getStrip1().getRecVal().getAsda());
//        assertTrue("Recalculated LDA for the second strip is not correct", trueValues.getLda() == runway.getStrip1().getRecVal().getLda());
    }
}
