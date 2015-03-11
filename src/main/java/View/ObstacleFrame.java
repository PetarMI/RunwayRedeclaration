package View;

import Controller.SetupListener;
import Exceptions.FieldEmptyException;
import Exceptions.PositiveOnlyException;
import Model.Obstacle;
import Model.XMLHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

public class ObstacleFrame extends JFrame{

    public static final int WIDTH = 150;
    public static final int HEIGHT = 300;

    private JTextField nameTextField, heightTextField, widthTextField, lengthTextField;
    private JTextArea descriptionTextArea;
    private JButton addButton;
    private JPanel mainPane;
    private boolean testable;

    public ObstacleFrame(boolean testable){
        this.testable = testable;
        this.doInitializations();
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
    public void setListeners(SetupListener listener1, WindowAdapter listener2) {
        listener1.useThis(new Object[]{nameTextField, heightTextField, widthTextField, lengthTextField, descriptionTextArea});
        this.addButton.addActionListener(listener1);
        this.addWindowListener(listener2);
    }

    private void setProperties() {
        this.setTitle("Add obstacle");
        this.setSize(WIDTH, HEIGHT);
        //TODO: Minimum/Maximum size or don't allow it to be resized?
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPane);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
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
