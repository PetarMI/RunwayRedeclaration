import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class OperatorStepDef {



    @Given("^Operator (.*) wants to import information in the system$")
    public void operator_wants_to_import_information_in_the_system
            (String operatorName) throws Throwable {

    }

    @When("^Ground crew staff sends information about obstacle: " +
            "position = (\\d+), height = (\\d+), runway = (\\d+)$")
    public void Ground_crew_staff_sends_information_about_obstacle_position_height_runway_
            (int position, int height, int runway) throws Throwable {


    }

    @Then("^System can import XML files containing input (.*)$")
    public void system_can_import_XML_files_containing_input_quickly
            (String expectedResult) throws Throwable {
    }

}
