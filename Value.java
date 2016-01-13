
/**
 * Write a description of class Value here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Value extends Node
{
    protected double val;
    /**
     * Constructor for objects of class Value
     */
    public Value(String data)
    {
        super(data);
        val = Double.parseDouble(data);
    }
    
    protected double getVal(){return val;}
}
