package View;

import Exceptions.PositiveOnlyException;
import Model.*;
import javafx.application.Platform;
import org.controlsfx.control.NotificationPane;

import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class CalculusFrame extends JFrame{

    //public static final int WIDTH = 675;
    public static final int WIDTH = 1200;
    //public static final int HEIGHT = 400;
    public static final int HEIGHT = 700;
    private XMLHelper xmlHelper;
    private final Runway runway;
    private final String airport;
    private JPanel mainPane;
    private JButton changeRunwayButton;
    private JComboBox obstaclesComboBox;
    private JButton newObstacleButton;
    private JButton topDownViewButton;
    private JButton sideViewButton;
    private JButton calculateButton;
    private JFormattedTextField posFromLeftText;
    private JFormattedTextField blastAllowanceFormattedTextField;
    private JFormattedTextField centreJFormattedTextField;
    private JLabel origTora2;
    private JLabel recTora2;
    private JLabel origToda2;
    private JLabel recToda2;
    private JLabel origAsda2;
    private JLabel recAsda2;
    private JLabel origLda2;
    private JLabel recLda2;
    private JLabel origTora1;
    private JLabel recTora1;
    private JLabel origToda1;
    private JLabel recToda1;
    private JLabel origAsda1;
    private JLabel recAsda1;
    private JLabel origLda1;
    private JLabel recLda1;
    private JPanel strip2Panel;
    private JPanel strip1Panel;
    private JTextField posFromRightText;
    private JComboBox<String> centrelinePosComboBox;
    private JLabel str1Landing;
    private JLabel str1Takeoff;
    private JLabel str2Landing;
    private JLabel str2Takeoff;
    private JPanel infoPane;
    private JPanel displayPane;
    private JPanel calcPane;
    private JPanel viewPane;
    private JButton compassHeadingButton;
    private JButton str1SideOnButton;
    private JButton str2SideOnButton;
    private JPanel notifPane;
    private JOptionPane optionsPane;
    private boolean testable;
    private ThreeDVisuals threeD;
    private NotifBoard fxNotif;

    public CalculusFrame(Runway runway, String airport, boolean testable) {
        this.runway = runway;
        this.airport = airport;
        this.testable = testable;
        this.doInitializations();
        this.setListeners();
        this.setProperties();
    }


    //TODO: restrict input to Integer (or throw errors?)
    private void doInitializations() {
        xmlHelper = new XMLHelper();
//        threeD = new ThreeDVisuals();
        fxNotif = new NotifBoard();
//        notifPane.add(fxNotif, BorderLayout.CENTER);
        //Creates the menubar
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.setToolTipText("Exit application");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenuItem exportItem = new JMenuItem("Export");
        exportItem.setMnemonic(KeyEvent.VK_P);
        exportItem.setToolTipText("Export the calculation as a .txt");
        exportItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = JOptionPane.showInputDialog(null,
                        "Enter a custom name to save the configuration",
                        "Export Configuration",
                        JOptionPane.QUESTION_MESSAGE);
                try
                {
                    PrintHelper.print(runway, airport, filename);
                }
                catch (FileNotFoundException exp)
                {
                    JOptionPane.showMessageDialog(CalculusFrame.this, "Invalid file name.");
                }
                catch (IOException exc)
                {
                    JOptionPane.showMessageDialog(CalculusFrame.this, "Could not create file.\nTryAgain");
                }
            }
        });

        file.add(exportItem);
        file.add(exitItem);
        menubar.add(file);

        //Calculations option
        JMenu calculations = new JMenu("Calculations");
        file.setMnemonic(KeyEvent.VK_F);

        ButtonGroup group = new ButtonGroup();
        JMenuItem simpleCalcsItem = new JRadioButtonMenuItem("Simple calculations");
        simpleCalcsItem.setMnemonic(KeyEvent.VK_E);
        simpleCalcsItem.setToolTipText("Calculate without width and length");
        simpleCalcsItem.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    runway.setSimpleCalculations();
                }
            }
        });
        JMenuItem complexCalcsItem = new JRadioButtonMenuItem("Complex calculations");
        complexCalcsItem.setMnemonic(KeyEvent.VK_P);
        complexCalcsItem.setToolTipText("Calculate with width and length");
        complexCalcsItem.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    runway.setComplexCalculations();
                }
            }
        });

        group.add(simpleCalcsItem);
        group.add(complexCalcsItem);
        complexCalcsItem.setSelected(true);
        calculations.add(simpleCalcsItem);
        calculations.add(complexCalcsItem);

        //calculation breakdown
        JMenuItem viewBreakdown = new JMenuItem("View breakdown");
        viewBreakdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BreakdownFrame(runway.getStrip1().viewCalculationBreakdown(),
                        runway.getStrip2().viewCalculationBreakdown());
            }
        });
        calculations.addSeparator();
        calculations.add(viewBreakdown);

        menubar.add(calculations);

        this.setJMenuBar(menubar);

        optionsPane = new JOptionPane();
        this.posFromLeftText.setUI(new HintTextField("Position from Left"));
        this.posFromRightText.setUI(new HintTextField("Position from Right"));
        this.blastAllowanceFormattedTextField.setUI(new HintTextField("Blast Allowance"));
        this.centreJFormattedTextField.setUI(new HintTextField("Centreline distance"));
        this.centrelinePosComboBox.addItem("Above");
        this.centrelinePosComboBox.addItem("Below");

        Values origValues = runway.getStrip1().getOrigVal();
        this.origTora1.setText(String.valueOf(origValues.getTora()));
        this.origToda1.setText(String.valueOf(origValues.getToda()));
        this.origAsda1.setText(String.valueOf(origValues.getAsda()));
        this.origLda1.setText(String.valueOf(origValues.getLda()));

        origValues = runway.getStrip2().getOrigVal();
        this.origTora2.setText(String.valueOf(origValues.getTora()));
        this.origToda2.setText(String.valueOf(origValues.getToda()));
        this.origAsda2.setText(String.valueOf(origValues.getAsda()));
        this.origLda2.setText(String.valueOf(origValues.getLda()));

        this.strip1Panel.setBorder(BorderFactory.createTitledBorder((runway.getStrip1().getStripId())));
        this.strip2Panel.setBorder(BorderFactory.createTitledBorder((runway.getStrip2().getStripId())));
        this.updateObstacleList();
    }

    private void updateRecValues() {
        Values recValues = runway.getStrip1().getRecVal();
        this.recTora1.setText(String.valueOf(recValues.getTora()));
        this.recToda1.setText(String.valueOf(recValues.getToda()));
        this.recAsda1.setText(String.valueOf(recValues.getAsda()));
        this.recLda1.setText(String.valueOf(recValues.getLda()));
        this.str1Landing.setText(recValues.getLanding());
        this.str1Takeoff.setText(recValues.getTakeoff());

        recValues = runway.getStrip2().getRecVal();
        this.recTora2.setText(String.valueOf(recValues.getTora()));
        this.recToda2.setText(String.valueOf(recValues.getToda()));
        this.recAsda2.setText(String.valueOf(recValues.getAsda()));
        this.recLda2.setText(String.valueOf(recValues.getLda()));
        this.str2Landing.setText(recValues.getLanding());
        this.str2Takeoff.setText(recValues.getTakeoff());
    }

    private void setListeners() {
        changeRunwayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BeginFrame();
                CalculusFrame.this.dispose();
            }
        });
        newObstacleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ObstacleFrame obstacleFrame = new ObstacleFrame(false);
                obstacleFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        CalculusFrame.this.updateObstacleList();
                        CalculusFrame.this.obstaclesComboBox.setSelectedIndex(obstaclesComboBox.getItemCount()-1);
                    }
                });
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Obstacle obs = (Obstacle)obstaclesComboBox.getSelectedItem();
                try {
                    int posFromRight = Integer.parseInt(posFromRightText.getText());
                    int posFromLeft = Integer.parseInt(posFromLeftText.getText());
                    int centrelineDist = Integer.parseInt(centreJFormattedTextField.getText());
                    if (centrelineDist < 0)
                    {
                        throw new PositiveOnlyException();
                    }
                    int blastAllowance = Integer.parseInt(blastAllowanceFormattedTextField.getText());
                    runway.addObstacle(obs, posFromLeft, posFromRight, centrelineDist,
                            centrelinePosComboBox.getSelectedItem().toString());
                    runway.recalculateValues(blastAllowance);
                    CalculusFrame.this.updateRecValues();

//                    viewPane.remove(threeD);
//                    threeD = new ThreeDVisuals();
//                    viewPane.add(threeD);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Platform.setImplicitExit(false);
                            fxNotif.addNotif(new Notif(Notif.SYNC_TITLE, Notif.SYNC_IMAGE));
                            threeD.init(runway);
                            NotificationPane notificationPane = new NotificationPane();
                            notificationPane.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
                            notificationPane.setText("Do you want to save your password?");
                            notificationPane.show();
                        }
                    });
                }catch (NumberFormatException e1){
                    if(!testable) {
                        optionsPane.showMessageDialog(CalculusFrame.this, "One or more inputted values are not accepted.");
                        e1.printStackTrace();
                    }
                }
                //TODO remove print stack trace
                catch (PositiveOnlyException e1) {
                    if(!testable) {
                        JOptionPane.showMessageDialog(CalculusFrame.this, "Distance from centreline must be greater than 0.");
                        e1.printStackTrace();
                    }
                }

            }
        });

        topDownViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                threeD.setCompassOrientation(false);
            }
        });

        str1SideOnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                threeD.setHorizontalRotation(ThreeDVisuals.PLAIN_ANGLE);
                threeD.setVerticalRotation(ThreeDVisuals.SIDE_VIEW_ANGLE);
                threeD.setZRotation(ThreeDVisuals.PLAIN_ANGLE);
            }
        });

        str2SideOnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                threeD.setHorizontalRotation(ThreeDVisuals.PLAIN_ANGLE);
                threeD.setVerticalRotation(ThreeDVisuals.SIDE_SECOND_VIEW_ANGLE);
                threeD.setZRotation(ThreeDVisuals.PLAIN_ANGLE);
            }
        });

        compassHeadingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                threeD.setCompassOrientation(true);
            }
        });

    }

    private void updateObstacleList(){
        List<Obstacle> obstacles = xmlHelper.readObstacles();
        obstaclesComboBox.removeAllItems();
        for(Obstacle o : obstacles){
            System.out.println(o);
            obstaclesComboBox.addItem(o);
        }
    }

    private void setProperties() {
        this.setTitle(airport);
        this.setSize(WIDTH, HEIGHT);
        //TODO: Minimum/Maximum size or don't allow it to be resized?
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    public void pressCalculate(){
        calculateButton.doClick();
    }

    public void setPosFromLeft(String s){
        posFromLeftText.setText(s);
    }
    public void setPosFromRight(String s){
        posFromRightText.setText(s);
    }
    public void setBlastAllowance(String s){
        blastAllowanceFormattedTextField.setText(s);
    }

    public void setCentreLineDist(String s){
        centreJFormattedTextField.setText(s);
    }

}
