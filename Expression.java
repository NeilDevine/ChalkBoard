import java.util.*;
/**
 * This class represents any given mathematical expression with numbers and variables
 * 
 * @author (Neil Devine) 
 * @version (a version number or a date)
 */
public class Expression
{
    protected Node root;
    /**
     * Constructor for objects of class Exression
     * Note: This constructor can be made more efficient (possibly to O(N) time)
     */
    public Expression(String e[]){
        root = fillTree(e);
    }

    public Node fillTree(String e[]){
        int n = rootIndex(e);
        Node d = node(e[n]);
        if(d.isVal() || d.isVar()){}
        else{
            String[] left = new String[n];
            for(int i=0; i < left.length; i++)
            {
                left[i] = e[i];
            }
            d.addChild(fillTree(left));

            String[] right = new String[e.length-n-1];
            for(int j=0; j < right.length; j++)
            {
                right[j] = e[j+n+1];
            }
            d.addChild(fillTree(right));
        }
        return d;
    }

    public Node getRoot(){
        return root;
    }

    public void setRoot(Node r){
        this.root = r;
    }

    public Expression() {}

    public Expression(Node rootNode){ root = rootNode; }

    public static String[] parse(String e)
    {
        e = e.replace(" ", "");
        if(e.substring(0,1).equals("+"))
        {
            e = "0" + e;
        }
        for(int i = e.length()-1; i >= 0; i--)
        {
            if(i > 0 && ((e.charAt(i) == '(' && (Character.isDigit(e.charAt(i-1)) || e.charAt(i-1) == ')')) || (e.charAt(i-1) == ')' && Character.isDigit(e.charAt(i)))))
            {
                e = e.substring(0, i) + "*" + e.substring(i);
            }
            if(i > 0 && i < e.length() - 1 &&  e.charAt(i) == '-' && (Character.isDigit(e.charAt(i-1)) || e.charAt(i-1) == 'x'))
            {
                e = e.substring(0,i) + "- " + e.substring(i+1);
            }
        }
        for(int i = 0; i < e.length(); i++)
        {
            if(i < e.length()-1 && e.charAt(i) != ' ' && e.charAt(i) != '.' && e.charAt(i+1) != '.'  && e.charAt(i) != '-' && i < e.length()-1 && (!Character.isDigit(e.charAt(i)) || !Character.isDigit(e.charAt(i+1))))
            {
                e = e.substring(0,i+1) + " " + e.substring(i+1);
            }
        }
        //System.out.println(e);
        return e.split(" ");
    }

    private int rootIndex(String[] e){
        int n = 0;
        int min = 99999;
        int index=0;
        for(int i= 0; i < e.length; i++){
            if(getPrec(e[i]) == -1) {}
            else if(e[i].equals("(")){
                n+=10;
            }
            else if(e[i].equals(")")){
                n-=10;
            }
            else if(getPrec(e[i]) + n <= min){
                min = getPrec(e[i]) + n;
                index = i;
            }
        }

        if(min == 99999){
            for(int j = 0; j < e.length; j++){
                if(getPrec(e[j]) == -1) {return j;}
            }
        }
        return index;
    }

    public Integer getPrec(String op){
        TreeMap<String, Integer> precedence = new TreeMap<String, Integer>();
        precedence.put("+", 1);   // + and - have lower precedence than * and /
        precedence.put("-", 1);
        precedence.put("*", 3);
        precedence.put("/", 4);
        precedence.put("^", 5);
        precedence.put("(", 9);   
        precedence.put(")", 9);
        if(precedence.get(op) == null) return -1;
        return precedence.get(op);
    }

    /**
     * A calculate function which can also show the steps of the calculation
     */
    public double calculate(boolean steps){
        return recCalculate(root, steps).getVal();
    }

    protected Value recCalculate(Node r, boolean steps){
        if(r.isVal()) {return (Value)r;}
        else{
            if(r.isVar()){
                //System.out.println("Please Enter a numerical expression");
                throw new NumberFormatException();
            }
            for(int i = 0; i < r.children.size(); i++){
                r.replaceChildAt(i, recCalculate(r.childAt(i),steps));
            }
            double a = 0;
            if(r.is("+")){
                for(int i=0; i < r.children.size(); i++) a+=((Value)r.childAt(i)).getVal();
            }
            else if(r.is("-")) a = ((Value)r.childAt(0)).getVal() - ((Value)r.childAt(1)).getVal();
            else if(r.is("*")){
                a=1;
                for(int i=0; i < r.children.size(); i++) a*=((Value)r.childAt(i)).getVal();
            }
            else if(r.is("/")) a = ((Value)r.childAt(0)).getVal() / ((Value)r.childAt(1)).getVal();
            else if(r.is("^")) a = Math.pow(((Value)r.childAt(0)).getVal(),((Value)r.childAt(1)).getVal());
            //r.replace(node((a + "")));
            //r = node(a + "");
            if(steps)
                System.out.println(root);
            return new Value(a + "");
        }
    }
    
    /**
     * Expands a given expression
     * 
     * @return   Whether any changes were possible
     */
    public boolean expand(){
        boolean c = false;
        while(recursiveExpand(root)) {c=true;}
        return c;
    }

    //Forgetting about this function for the moment (also, it's really an expand method anyway)
    protected boolean recursiveExpand(Node r){
        if(r==null) { return false;}
        else{
            boolean changes = false;

            if(r.data.equals("*") || r.data.equals("/")){
                for(int i = 0; i < r.children.size(); i++){
                    if(r.children.get(i).getPrec() < 3 && r.children.get(i).isOp()){
                        if(r.data.equals("*") || i == 0){
                            for(int j = 0; j < r.children.get(i).children.size(); j++){
                                Node copy = recCopy(r);
                                copy.replaceChildAt(i,(r.children.get(i).children.get(j)));
                                r.children.get(i).replaceChildAt(j,copy);
                            }
                            //r.replace(r.children.get(i));
                            r = r.childAt(i);
                            changes = true;
                        }
                    }
                }
            }
            else if(r.data.equals("-") && r.children.get(1).data.equals("+")){
                r.children.get(1).multiply(new Value("-1"));
                r.data = "+";
                changes = true;
            }
            else if(r.data.equals("^")){
                if(r.children.get(0).equals("^")){
                    r.children.get(0).children.get(1).multiply(r.children.get(1));
                    changes=true;
                }
                else if(r.children.get(1).isVal()){
                    double a = Double.parseDouble(r.children.get(1).data);
                    if(a > 1){
                        r.children.remove(1);
                        for(int i = 0; i < a-1; i++) r.children.add(r.children.get(0));
                        r.data = "*";
                        changes = true;
                        //printTree();
                    }
                }
            }

            for(int i = 0; i < r.children.size(); i++) {
                if(recursiveExpand(r.children.get(i))) changes = true;
            }
            return changes;
        }
    }

    // Deprecated
    protected boolean recSubtraction(Node r){
        if(r==null){ return false;}
        else{
            boolean changes = false;
            if(r.data.equals("+")){
                for(int i = 0; i < r.children.size(); i++){
                    if(r.children.get(i).data.equals("*")){
                        if(r.children.get(i).children.contains("-1")){
                            r.children.get(i).children.remove("-1");
                            //if(r.children.get(i).children.size() == 1) r.subtract(r.children.get(i).children.get(0));
                            //else r.subtract(r.children.get(i));
                            r.children.remove(i);
                            changes = true;
                        }
                    }
                }
            }
            for(int i = 0; i < r.children.size(); i++){
                if(recSubtraction(r.children.get(i))) changes = true;
            }
            return changes;
        }
    }

    //returns a copy of the given expression
    public Expression copy() {
        return new Expression(recCopy(root));
    }

    protected Node recCopy(Node rootNode) {
        if(rootNode==null) {return null;}
        else{
            Node a = node(rootNode.data);
            for(int i = 0; i < rootNode.children.size(); i++) a.children.add(recCopy(rootNode.children.get(i))); // Hopefully not a problem
            return a;
        }
    }

    public Node node(String a){
        if(new Expression().getPrec(a) != -1){
            if(a.equals("+")) return new Add();
            else if(a.equals("*")) return new Mult(a);
            else if(a.equals("-")) return new Add(a);
            else if(a.equals("/")) return new Div(a);
            else if(a.equals("^")) return new Exp(a);
            else return new Operator(a);
        }
        else{
            try{
                Double.parseDouble(a);
                return new Value(a);
            }
            catch(Exception exception) {return new Variable(a);}
        }
    }

    public static void test()
    {
        String[] one = {"a", "*", "(", "b", "/", "c", ")", "*", "(", "d", "/", "e", ")", "*", "f"};
        String[] two = {"x", "*", "y", "*", "z", "*", "1", "*", "10", "*", "2"};
        Expression a = new Expression(one);
        Expression b = new Expression(two);
        System.out.println("Original: " + a.root);
        Node n = a.root.levelN();
        System.out.println("After levelOps: " + n);
    }
}
