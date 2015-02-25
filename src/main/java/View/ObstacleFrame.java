package View;

import Model.Obstacle;
import Model.XMLHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObstacleFrame extends JFrame{

    public static final int WIDTH = 150;
    public static final int HEIGHT = 300;
    
    private JPanel mainPane;
    private JTextField nameTextField, heightTextField, widthTextField;
    private JTextArea descriptionTextArea;
    private JButton addButton;

    public ObstacleFrame(){
        this.doInitializations();
        this.setListeners();
        this.setProperties();
    }

    private void doInitializations() {
        this.nameTextField.setUI(new HintTextFieldUI("Name", true));
        this.heightTextField.setUI(new HintTextFieldUI("Height", true));
        this.widthTextField.setUI(new HintTextFieldUI("Width", true));
        this.descriptionTextArea.setUI(new HintTextFieldUI("Description", true));

    }

    //TODO: Should I add length to the form? If we are not using it anymore it should be removed from the obstacle class
    //TODO: Also, manage input errors somehow
    private void setListeners() {
        this.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameTextField.getText();
                    int height = Integer.parseInt(heightTextField.getText());
                    int width = Integer.parseInt(widthTextField.getText());
                    String description = descriptionTextArea.getText();
                    XMLHelper xmlHelper = new XMLHelper();
                    xmlHelper.addObstacleXML(new Obstacle(name, width, height , 0, description));
                    ObstacleFrame.this.dispose();
                }catch (NumberFormatException e1){
                    e1.printStackTrace();
                }
            }
        });
    }

    private void setProperties() {
        this.setTitle("Add obstacle");
        this.setSize(WIDTH, HEIGHT);
        this.setContentPane(mainPane);
        this.setVisible(true);
    }

}
