//import Model.Airport;
//import Model.XMLHelper;
//import View.CalculusFrame;
//import View.ObstacleFrame;
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.Then;
//import cucumber.api.java.en.When;
//import org.xml.sax.SAXException;
//
//import javax.swing.*;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.IOException;
//
//import static junit.framework.TestCase.assertTrue;
//
//public class GUIstepDefinitions {
//
//    CalculusFrame calculusFrame;
//    ObstacleFrame obstacleFrame;
//
//    @Given("^The operator is on the calculus page$")
//    public void the_operator_is_on_the_calculus_page() throws ParserConfigurationException, SAXException, IOException {
//        XMLHelper xml = new XMLHelper();
//        Airport airport = xml.readAirport(xml.readAllAirports().get(0) +".xml");
//        calculusFrame = new CalculusFrame(airport.getRunway(airport.getRunwayIds().get(0)), airport.getName(), true);
//    }
//
//    @When("^he inputs (.*) on all of the fields$")
//    public void he_inputs_on_all_of_the_fields(String input){
//        calculusFrame.setBlastAllowance(input);
//        calculusFrame.setCentreLineDist(input);
//        calculusFrame.setPosFromLeft(input);
//        calculusFrame.setPosFromRight(input);
//    }
//
//    @Then("^The application should not crash if Calculate button pressed$")
//    public void The_application_should_not_crash_if_Calculate_button_pressed() {
//        boolean ok = true;
//        try{
//            calculusFrame.pressCalculate();
//            JOptionPane.getRootFrame().dispose();
//        }catch (Exception e){
//            ok = false;
//        }
//        assertTrue("Program crashed", ok);
//    }
//
//    @Given("^The operator is on the new obstacle page$")
//    public void The_operator_is_on_the_new_obstacle_page() {
//       obstacleFrame = new ObstacleFrame(true);
//    }
//
//    @When("^he inputs (.*) on height and width$")
//    public void he_inputs_on_height_and_width(String s) {
//        obstacleFrame.setHeight(s);
//        obstacleFrame.setWidth(s);
//    }
//
//    @Then("^The application should not crash if add button pressed$")
//    public void The_application_should_not_crash_if_button_pressed() {
//        boolean ok = true;
//        try {
//            obstacleFrame.pressAddButton();
//        }catch (Exception e){
//            ok = false;
//        }
//        assertTrue("Application crashed", ok);
//    }
//
//
//
//
//}
