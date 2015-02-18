package Model;

/**
 * Created by PetarMI on 17.02.2015.
 * only 90% sure we should have this class
 */
//TODO
    //implement Observer pattern so to change the view
public class Model
{
    private Airport airport;
    private XMLHelper xmlhelper;

    public Model()
    {
        this.xmlhelper = new XMLHelper();
    }

}
