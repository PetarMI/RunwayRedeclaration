import Controller.XMLHelper;
import Model.Obstacle;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class XMLStepDefinitions {

    XMLHelper xml;
    ArrayList<Obstacle> obstacles;
    Obstacle obs1, obs2;

    @Given("^A list of predefined obstacles exists$")
    public void a_list_of_predefined_obstacles_exists() {
        File f = new File(XMLHelper.OBSTACLE_FILE_NAME);
        assertTrue("obstacles file doesn't exist in the classpath", f.exists());
    }

    @When("^Operator opens the program$")
    public void operator_opens_the_program(){
        xml = new XMLHelper();

    }

    @Then("^The program should read the data from the XML file$")
    public void the_program_should_read_the_data_from_the_XML_file() {
        assertTrue("could not read obstacles XML", xml.readObstacles()!= null);
    }

    @Given("^There are some airport xml files cached$")
    public void There_are_some_airport_xml_files_cached() {
        File f = new File(XMLHelper.AIRPORTS_DIRECTORY);
        assertTrue("airports directory doesn't exist", f.exists()&& f.isDirectory());
        assertTrue("airports directory is empty", f.list().length != 0);
    }

    @Then("^The program should read the runway information for all the airports$")
    public void The_program_should_read_the_runway_information_for_all_the_airports()  {
        boolean ok = true;
        try {
            xml.readAllAirports();
        } catch (Exception e) {
            ok = false;
        }
        assertTrue("some files couldn't be read", ok);
    }

    @Given("^The ground crew report obstacles: (.*) with length (\\d+), width (\\d+), height (\\d+) with (.*)$")
    public void The_ground_crew_report_obstacles_with_length_width_height_with
            (String name, int l, int w,int h, String descr){
        obs1 = new Obstacle(name, w,h, l, descr);

    }

    @And("^(.*) with length (\\d+) width (\\d+) height (\\d+) (.*)$")
    public void car_with_length_width_height(String name, int l, int w,int h, String descr){
        obs2 =new Obstacle(name, w,h, l, descr);
    }

    @When("^The operator adds them to the predefined list of obstacles$")
    public void The_operator_adds_them_to_the_predefined_list_of_obstacles(){
        obstacles = new ArrayList<Obstacle>();
        obstacles.add(obs1);
        obstacles.add(obs2);
    }

    @Then("^The program should save them correctly$")
    public void The_program_should_save_them(){
        xml = new XMLHelper();
        assertTrue("obstacle list could not be saved", xml.createObstacleXML(obstacles));
        ArrayList<Obstacle> savedObs = xml.readObstacles();
        assertTrue("saved and read obstacles do not match", savedObs.equals(obstacles));
    }
}
