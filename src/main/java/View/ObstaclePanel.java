package View;

import Controller.SetupListener;
import Exceptions.FieldEmptyException;
import Exceptions.PositiveOnlyException;
import Model.Obstacle;
import Model.XMLHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObstaclePanel extends JPanel{

    public static final int WIDTH = 150;
    public static final int HEIGHT = 300;

    private JTextField nameTextField, heightTextField, widthTextField, lengthTextField;
    private JTextArea descriptionTextArea;
    private JButton addButton;
    private boolean testable;

    public ObstaclePanel(boolean testable, SetupListener al1){
        this.testable = testable;
        this.doInitializations();
        this.setListeners(al1);
        this.setProperties();
    }

    private void doInitializations() {
        this.nameTextField.setUI(new HintTextField("Name"));
        this.heightTextField.setUI(new HintTextField("Height"));
        this.lengthTextField.setUI(new HintTextField("Length"));
        this.widthTextField.setUI(new HintTextField("Width"));
        this.descriptionTextArea.setUI(new HintTextField("Description"));

    }

    //TODO: Length added to form, needs to be implemented in here
    //TODO: Also, manage input errors somehow
    private void setListeners(SetupListener listener1) {
        listener1.useThis(new Object[]{nameTextField, heightTextField, widthTextField, lengthTextField, descriptionTextArea});
        this.addButton.addActionListener(listener1);
    }

    private void setProperties() {
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
    }

    public void setName(String s) {
        this.nameTextField.setText(s);
    }

    public void setHeight(String s) {
        this.heightTextField.setText(s);
    }

    public void setWidth(String s) {
        this.widthTextField.setText(s);
    }

    public void pressAddButton() {
        this.addButton.doClick();
    }
}
