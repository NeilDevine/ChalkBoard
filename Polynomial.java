
/**
 * A Polynomial here is defined as an expression which includes basic operators and non-negative integer exponents on variables where no variables are divised
 * 
 * @author (Neil Devine) 
 * @version (a version number or a date)
 */
public class Polynomial extends AlgebraicExpression
{

    /**
     * Constructor for objects of class AnalyticalExpression
     */
    public Polynomial(String[] e)
    {
        super(e);
    }

    // Returns the degree of the Polynomial
    private int degree() {

        return recursiveDegree(root);
    }

    private int recursiveDegree(Node rootNode) {
        if(rootNode.l.equals("x")) {
            if(rootNode.equals("^")) return Integer.parseInt(rootNode.r.data);
            return 1;
        }
        return Math.max(recursiveDegree(rootNode.r), recursiveDegree(rootNode.l));
    }

    // Factors the Polynomial
    private void factor() {

    }

    public void simplify() {
        // Start at the bottom of the tree (CHECK)
        // Combine like terms
        // Get rid of parentheses
        // Combine like terms again

        recursiveSimplify(root);
    }

    public void recursiveSimplify(Node rootNode) {
        if(rootNode == null) return;
        if(rootNode.l.isOp()) recursiveSimplify(rootNode.l);
        if(rootNode.r.isOp()) recursiveSimplify(rootNode.r);
        if(getPrec(rootNode.data) < 2) recursiveCombineLikeTerms(rootNode);
        
    }

    public void recursiveCombineLikeTerms(Node rootNode) {
        if(rootNode == null) return;
        
    }
}
