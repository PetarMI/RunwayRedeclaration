import Model.Obstacle;
import Model.Runway;
import Model.Strip;
import Model.Values;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MathStepDefinitions {

    private Strip strip1, strip2;
    private Runway runway;
    private Values trueValues;
    private String runwayID;
    private Obstacle obs;

    @Given("^The operator has the values for the runway (.*)$")
    public void The_operator_has_the_values_for_the_runway(String runwayID) {
//        this.runwayID = runwayID;
    }

    @And("^The values1 for (.*) are (.*), (.*), (.*), (.*), (.*) and (.*)$")
    public void The_values_for_Strip1
            (String stripId, String tora, String toda, String asda, String lda, String orientation, String position){
        Values origVal = new Values(Integer.valueOf(tora), Integer.valueOf(asda), Integer.valueOf(toda), Integer.valueOf(lda));
        strip1 = new Strip(stripId, Integer.valueOf(orientation), position, origVal);
    }

    @And("^The values2 for (.*) are (.*), (.*), (.*), (.*), (.*) and (.*)$")
    public void The_values_for_Strip2
            (String stripId, String tora, String toda, String asda, String lda, String orientation, String position){
        Values origVal = new Values(Integer.valueOf(tora), Integer.valueOf(asda), Integer.valueOf(toda), Integer.valueOf(lda));
        strip2 = new Strip(stripId, Integer.valueOf(orientation), position, origVal);
        runway = new Runway(runwayID, strip1, strip2);
    }

    @When("^He adds an obstacle (\\d+) m from the threshold of height (\\d+) m$")
    public void He_adds_an_obstacle_m_from_the_threshold_of_height_m(int position, int height) {
//        obs = new Obstacle("test", 1, height, 1, "test");
    }

    @Then("^The recalculated values for (.*) should be (.*), (.*), (.*), (.*)$")
    public void The_recalculated_values_for_Strip1(String stripId, String tora, String toda, String asda, String lda) {
//        trueValues = new Values(tora,asda, toda, lda);
//        assertTrue(runway.getStrip1().getRecVal().equals(trueValues));
    }
}
