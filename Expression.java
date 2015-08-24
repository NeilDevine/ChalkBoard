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

    public class Node  // Represents Nodes withing a given expression (Operators, Variables, Numbers, etc.)
    {
        protected String data;
        protected Node l, r;  // Note: This should be removed soon
        protected ArrayList<Node> children;

        public Node(String d){
            data=d;
            r=l=null;
            children = new ArrayList<Node>();
        }

        protected Integer getPrec(){
            TreeMap<String, Integer> precedence = new TreeMap<String, Integer>();
            precedence.put("+", 1);   // + and - have lower precedence than * and /
            precedence.put("-", 1);
            precedence.put("*", 3);
            precedence.put("/", 4);
            precedence.put("^", 5);
            precedence.put("(", 9);   
            precedence.put(")", 9);
            if(precedence.get(data) == null) return -1;
            return precedence.get(data);
        }

        protected void replace(Node toReplace){
            children.clear();
            children.addAll(toReplace.children);
            data=toReplace.data;
        }

        protected boolean isVal(){
            return getPrec() == -1;
        }

        protected boolean isOp(){
            return getPrec() != -1;
        }

        protected boolean isVar(){
            return data.equals("x");
        }

        // Adds in to this Node
        protected void add(Node in){
            Node a = new Node("+");
            a.children.add(this);
            a.children.add(in);
            replace(a);
            recursiveLevelOps(this);
        }

        // Multiplies a specific value (in) to this Node
        protected void multiply(Node in){
            Node a = new Node("*");
            a.children.add(this);
            a.children.add(in);
            replace(a);
            recursiveLevelOps(this);
        }

        // Puts this Node to the power of a specific value (in)
        protected void power(Node in){
            Node a = new Node("+");
            a.children.add(this);
            a.children.add(in);
            replace(a);
            recursiveLevelOps(this);
        }
    }

    /**
     * Constructor for objects of class Exression
     * Note: This constructor can be made more efficient (possibly to O(N) time)
     */
    public Expression(String e[]){
        int n = rootIndex(e);
        Node d = new Node(e[n]);

        if(d.isVal()){}
        else{
            String[] left = new String[n];
            for(int i=0; i < left.length; i++)
            {
                left[i] = e[i];
            }
            Expression leftEx = new Expression(left);

            d.children.add(leftEx.root);
            String[] right = new String[e.length-n-1];
            for(int j=0; j < right.length; j++)
            {
                right[j] = e[j+n+1];
            }
            Expression rightEx = new Expression(right);

            d.children.add(rightEx.root);
        }
        root=d;
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
        System.out.println(e);
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

    public void printTree(){
        recursivePrintTree2(root);
        System.out.println();
    }

    // OLD VERSION
    private void recursivePrintTree(Node r){
        if(r==null) {}
        else{
            if(r.l!= null && !r.l.isVal() && r.l.getPrec() < r.getPrec()) {
                System.out.print("(");
                recursivePrintTree(r.l);
                System.out.print(")");
            }
            else
                recursivePrintTree(r.l);
            System.out.print(r.data);
            if(r.r!= null && !r.r.isVal() && (r.r.getPrec() < r.getPrec()) || (r.data.equals("-") && !r.r.isVal() && r.r.getPrec() < 3)) {
                System.out.print("(");
                recursivePrintTree(r.r);
                System.out.print(")");
            }
            else
                recursivePrintTree(r.r);
        }
    }

    protected void recursivePrintTree2(Node r){
        if(r==null){}
        else if(r.isOp()){
            for(int i = 0; i < r.children.size(); i++){
                if(r.children.get(i)!= null && !r.children.get(i).isVal() && (r.children.get(i).getPrec() < r.getPrec()) || (r.data.equals("-") && i==1 && !r.children.get(i).isVal() && r.children.get(i).getPrec() < 3)) {
                    System.out.print("(");
                    recursivePrintTree2(r.children.get(i));
                    System.out.print(")");
                }
                else
                    recursivePrintTree2(r.children.get(i));
                if(i!= r.children.size()-1) System.out.print(r.data);
            }
        }
        else
            System.out.print(r.data);
    }

    /**
     * A calculate function which can also show the steps of the calculation
     */
    public double calculate(boolean steps){
        return recursiveCalculate(root, steps);
    }

    protected double recursiveCalculate(Node r, boolean steps){
        if(r.isVal()) {return Double.parseDouble(r.data);}
        else{
            double a = 0;
            if(r.data.equals("+")){
                for(int i=0; i < r.children.size(); i++) a+=recursiveCalculate(r.children.get(i),steps);
            }
            else if(r.data.equals("-")) a = recursiveCalculate(r.children.get(0), steps) - recursiveCalculate(r.children.get(1), steps);
            else if(r.data.equals("*")){
                a=1;
                for(int i=0; i < r.children.size(); i++) a*=recursiveCalculate(r.children.get(i),steps);
            }
            else if(r.data.equals("/")) a = recursiveCalculate(r.children.get(0), steps) / recursiveCalculate(r.children.get(1), steps);
            else if(r.data.equals("^")) a = Math.pow(recursiveCalculate(r.children.get(0), steps),recursiveCalculate(r.children.get(1), steps));
            r.replace(new Node(a + ""));
            if(steps)
                this.printTree();
            return a;
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
                                copy.children.get(i).replace(r.children.get(i).children.get(j));
                                r.children.get(i).children.get(j).replace(copy);
                            }
                            r.replace(r.children.get(i));
                            changes = true;
                        }
                    }
                }
            }
            else if(r.data.equals("-") && r.children.get(1).data.equals("+")){
                r.children.get(1).multiply(new Node("-1"));
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
                        printTree();
                    }
                }
            }

            for(int i = 0; i < r.children.size(); i++) {
                if(recursiveExpand(r.children.get(i))) changes = true;
            }
            return changes;
        }
    }

    public void simplify(){
        recSimplify(root);
    }

    // An unfinished general purpose simplify function
    protected void recSimplify(Node r){
        if(r==null){}
        else{
            
        }
    }

    public void levelOps() {
        recursiveLevelOps(root);
    }

    // Change this so that it doesn't change the order for convenience and make it return a boolean
    protected void recursiveLevelOps(Node rootNode){
        if(rootNode == null) {}
        else if(rootNode.data.equals("*") || rootNode.data.equals("+")){
            for(int i = 0; i < rootNode.children.size(); i++){  // For all of the roots children
                if(rootNode.children.get(i).data.equals(rootNode.data)) {  // If the index child is the same as the parent
                    for(int j = 0; j < rootNode.children.get(i).children.size(); j++) rootNode.children.add(rootNode.children.get(i).children.get(j));
                    rootNode.children.remove(rootNode.children.get(i));
                }
            }
            for(int i = 0; i < rootNode.children.size(); i++) recursiveLevelOps(rootNode.children.get(i));
        }
        else{
            for(int i = 0; i < rootNode.children.size(); i++) recursiveLevelOps(rootNode.children.get(i));
        }
    }

    // This method simplifies Rational Expressions
    protected boolean recSimplifyRational(Node r){
        if(r == null) {return false;}
        else if(r.data.equals("/")){
            boolean changes = false;
            if(r.children.get(0).data.equals("/")){
                Node a = new Node("*");
                a.children.add(r.children.get(0).children.get(1));
                a.children.add(r.children.get(1));
                r.children.set(1, a);
                r.children.set(0, r.children.get(0).children.get(0));
                changes = true;
            }
            if(r.children.get(1).data.equals("/")){
                Node a = new Node("*");
                a.children.add(r.children.get(0));
                a.children.add(r.children.get(1).children.get(1));
                r.children.set(0, a);
                r.children.set(1, r.children.get(1).children.get(0));
                changes = true;
            }
            for(int i = 0; i < r.children.size(); i++){
                if(recSimplifyRational(r.children.get(i))) changes = true;
            }
            return changes;
        }
        else if(r.data.equals("*")){
            for(int i = 0; i < r.children.size(); i++){
                if(r.children.get(i).data.equals("/")){
                    Node a = recCopy(r);
                    a.children.set(i, r.children.get(i).children.get(0));
                    Node d = new Node("/");
                    d.children.add(a);
                    d.children.add(r.children.get(i).children.get(1));
                    r.replace(d);
                    return true;
                }
            }
            boolean changes = false;
            for(int i = 0; i < r.children.size(); i++){
                if(recSimplifyRational(r.children.get(i))) changes = true;
            }
            return changes;
        }
        else{
            boolean changes = false;
            for(int i = 0; i < r.children.size(); i++){
                if(recSimplifyRational(r.children.get(i))) changes = true;
            }
            return changes;
        }
    }

    // An unfinished method for Collecting Like Terms
    protected void recursiveLikeTerms(Node r){
        if(r==null) {}
        else{
            if(r.data.equals("+")){
                for(int i = 0; i < r.children.size(); i++){
                    for(int j = 0; j < r.children.size() && i!=j; j++){
                        if(r.children.get(j).data.equals("*")){
                            for(int k = 0; k < r.children.get(j).children.size(); k++) {
                                if(r.children.get(j).children.get(k).data.equals("^")){

                                }
                                else if(r.children.get(j).children.get(k).data.equals("x")){

                                }
                            }
                        }
                    }
                }
            }
            else if(r.data.equals("-")){

            }
            else if(r.data.equals("*")){

            }
            else if(r.data.equals("/")){

            }
            // This case might not be necessary
            else if(r.data.equals("^")){

            }
        }
    }

    //returns a copy of the given expression
    public Expression copy() {
        return new Expression(recCopy(root));
    }

    protected Node recCopy(Node rootNode) {
        if(rootNode==null) {return null;}
        else{
            Node a = new Node(rootNode.data);
            for(int i = 0; i < rootNode.children.size(); i++) a.children.add(recCopy(rootNode.children.get(i))); // Hopefully not a problem
            return a;
        }
    }

    public boolean equals(Node r){
        return true;
    }

    // Returns true if 'a' is within the tree
    public boolean contains(Node a) {
        return recursiveContains(a, root);
    }

    protected boolean recursiveContains(Node a, Node rootNode) {
        if(rootNode == null) return false;
        if(rootNode.data.equals(a.data))
            return true;
        return recursiveContains(a, rootNode.r) || recursiveContains(a, rootNode.l); 
    }

    public static void test()
    {
        String[] blah = {"(", "1", "+", "x", ")", "*", "(", "1", "+", "x", ")"};
        Expression a = new Expression(blah);

        a.printTree();
        a.expand();

        a.printTree();

    }
}
