import java.util.ArrayList;
/**
 * This class represents Mathematical Monomial Terms
 * 
 * @author (Neil Devine) 
 * @version (a version number or a date)
 */
public class Term extends Expression
{
    private Node root;
    private Node base;
    private ArrayList<Node> coefs;
    /**
     * Constructor for objects of class Term
     */
    public Term(Node in)
    {
        root = in;
        base = findBase(root);
        addCoefs();
    }

    public Node findBase(Node r){
        if(r==null){ return null;}
        else{
            if(r.data.equals("*")){
                return r;
            }
            if(r.isVar()) return r;
            return r;
        }
    }

    public void addCoefs(){
        try{ 
            super.recursiveCalculate(root, false);
            coefs.add(root);
        }
        catch(Exception e){
            if(root.data.equals("*")){
                for(int i = 0; i < root.children.size(); i++){
                    try{ 
                        recursiveCalculate(root.children.get(i),false);
                        coefs.add(root.children.get(i));
                    }
                    catch(Exception w){
                        coefs.add(root.children.get(i));
                    }
                }
            }
            else if(root.data.equals("^") || root.data.equals("/")){
                coefs.add(root);
            }
        }
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public Node add(Term a)
    {
        return null;
    }

    public Node multiply(Term a){
        return null;
    }

    public Node divide(Term a){
        return null;
    }
}
