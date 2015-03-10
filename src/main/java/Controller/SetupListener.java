package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;

/**
 * Created by Edward on 10/03/2015.
 */
//Purpose - to make ActionListeners that can be passed to the view then told what GUI object to use afterwards
public abstract class SetupListener implements ActionListener{
    public abstract void useThis(Object[] o);
    public abstract void actionPerformed(ActionEvent e);
}
