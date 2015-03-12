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

    @When("^He adds an obstacle (-?\\d+) m from the left and (-?\\d+) m from the right, of width (\\d+), height (\\d+), length (\\d+) and (-?\\d+) above the centreline$")
    public void He_adds_an_obstacle_(int posLeft, int posRight, int width, int height, int length, int distCentral) {
        obs = new Obstacle("test", width, height, length, "test");
        runway.addObstacle(obs, posLeft, posRight,distCentral);
    }

    @And("^with blast allowance (\\d+)$")
    public void with_blast_allowance(int blastAllowance){
        this.blastAllowance = blastAllowance;
    }

    @Then("^The recalculated values for (.*) should be (\\d+), (\\d+), (\\d+), (\\d+)$")
    public void The_recalculated_values_for_Strip1(String stripId, String tora, String toda, String asda, String lda) {
        runway.recalculateValues(blastAllowance);
        Values recValues;
        if (runway.getStrip1().getStripId().equals(stripId)) {
            recValues = runway.getStrip1().getRecVal();
        } else {
            recValues = runway.getStrip2().getRecVal();
        }
        trueValues = new Values(Integer.valueOf(tora), Integer.valueOf(asda), Integer.valueOf(toda), Integer.valueOf(lda));

        //test each value
        assertTrue("Recalculated TORA for " + stripId + " is " + recValues.getTora(), trueValues.getTora() == recValues.getTora());
        assertTrue("Recalculated TODA for " + stripId + " is " + recValues.getToda(), trueValues.getToda() == recValues.getToda());
        assertTrue("Recalculated ASDA for " + stripId + " is " + recValues.getAsda(), trueValues.getAsda() == recValues.getAsda());
        assertTrue("Recalculated LDA for "  + stripId + " is " + recValues.getLda(), trueValues.getLda()   == recValues.getLda());

        //test it all together
        assertTrue("Recalculated values are not the same", trueValues.equals(recValues));
    }

    @And("^For (.*) should be (\\d+),(\\d+), (\\d+), (\\d+)$")
    public void For_R_should_be_(String stripId, int tora, int toda, int asda, int lda)  {
        trueValues = new Values(tora, asda, toda, lda);
        Values recValues ;
        if(runway.getStrip1().getStripId().equals(stripId)){
            recValues = runway.getStrip1().getRecVal();
        }
        else{
            recValues = runway.getStrip2().getRecVal();
        }
        //test each value
        assertTrue("Recalculated TORA for "+ stripId +" is "+ recValues.getTora(), trueValues.getTora() == recValues.getTora());
        assertTrue("Recalculated TODA for "+ stripId +" is "+ recValues.getToda(), trueValues.getToda() == recValues.getToda());
        assertTrue("Recalculated ASDA for "+ stripId +" is "+ recValues.getAsda(), trueValues.getAsda() ==recValues.getAsda());
        assertTrue("Recalculated LDA for "+ stripId  +" is "+ recValues.getLda(), trueValues.getLda() == recValues.getLda());

        //test it all together
        assertTrue("Recalculated values are not the same", trueValues.equals(recValues));
    }
}
