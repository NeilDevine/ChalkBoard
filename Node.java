import java.util.ArrayList;
import java.util.TreeMap;
public class Node extends Expression // Represents Nodes within a given expression (Operators, Variables, Numbers, etc.)
{
    protected String data;
    protected ArrayList<Node> children;
    protected Node parent;

    public Node(String d){
        data=d;
        children = new ArrayList<Node>();
        parent = null;
        //root = super.getRoot();
    }

    public Node(String d, Node p){
        data=d;
        children = new ArrayList<Node>();
        parent = p;
    }

    protected Integer getPrec(){
        TreeMap<String, Integer> precedence = new TreeMap<String, Integer>();
        precedence.put("+", 1);   
        precedence.put("-", 1);
        precedence.put("*", 3);
        precedence.put("/", 4);
        precedence.put("^", 5);
        precedence.put("(", 9);   
        precedence.put(")", 9);
        if(precedence.get(data) == null) return -1;
        return precedence.get(data);
    }

    protected boolean isVal(){
        try{
            Double.parseDouble(data);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    protected boolean isOp(){
        return getPrec() != -1;
    }

    protected boolean isVar(){
        return this instanceof Variable;
    }

    // Adds in to this Node
    protected Add add(Node in){
        Add a = new Add();
        a.addChild(this);
        a.addChild(in);
        a = (Add)a.levelN();
        //a.recursiveLevelOps();
        return a;
    }

    // Multiplies a specific value (in) to this Node
    protected Mult multiply(Node in){
        Mult a = new Mult();
        a.addChild(this);
        a.addChild(in);
        a =(Mult)a.levelN();
        //this.recursiveLevelOps();
        return a;
    }

    protected void replaceChildAt(int index, Node toReplace){
        toReplace.parent = this;
        //if(childAt(index) == main){
        //    super.setRoot(toReplace);
        //}
        children.set(index,toReplace);
    }

    // Divides a specific value (in) to this Node
    protected Div divide(Node in){
        Div a = new Div();
        a.addChild(this);
        a.addChild(in);
        return a;
    }

    // Puts this Node to the power of a specific value (in)
    protected Exp power(Node in){
        Exp a = new Exp();
        a.addChild(this);
        a.addChild(in);
        return a;
    }

    protected Node addChild(Node in){ 
        children.add(in);
        in.parent = this;
        return in;
    }

    protected void addChildren(ArrayList<Node> in){
        children.addAll(in);
        for(int i = 0; i < in.size(); i++){
            in.get(i).parent = this;
        }
    }

    protected Node childAt(int i){ return children.get(i); }

    protected Node removeAt(int i) { return children.remove(i); }

    protected boolean removeChild(Node a) { return children.remove(a); }

    protected boolean is(String a) { return data.equals(a); }

    public String toString(){
        if(data==null){return "";}
        else if(isOp()){
            String s = "";
            for(int i = 0; i < children.size(); i++){
                if(childAt(i)!= null && childAt(i).isOp() && (childAt(i).getPrec() < getPrec()) || (data.equals("-") && i==1 && childAt(i).isOp() && childAt(i).getPrec() < 3)) {
                    s = s + "(" + childAt(i).toString() + ")";
                }
                else
                    s = s + childAt(i).toString();
                if(i!= children.size()-1) s = s + data;
            }
            return s;
        }
        else if(is("")){
            return childAt(0).toString();
        }
        else
            return data;
    }

    protected Node recNegateN(){
        if(this==null){return null;}
        else{
            Node n = recCopy(this);
            for(int i = 0; i < n.children.size(); i++){
                n.replaceChildAt(i,n.childAt(i).recNegateN());
            }
            if(n.is("-")) {
                Mult newNode2 = n.childAt(1).multiply(new Value("-1"));
                Add newNode = n.childAt(0).add(newNode2);
                return newNode;
            }
            return n;
        }
    }

    // Deprecated sort of
    protected boolean recSubtraction(){
        if(this==null){ return false;}
        else{
            boolean changes = false;
            if(data.equals("+")){
                for(int i = 0; i < children.size(); i++){
                    if(childAt(i).data.equals("*")){
                        if(childAt(i).children.contains("-1")){
                            childAt(i).children.remove("-1");
                            //if(childAt(i).children.size() == 1) subtract(childAt(i).childAt(0));
                            //else subtract(childAt(i));
                            children.remove(i);
                            changes = true;
                        }
                    }
                }
            }
            for(int i = 0; i < children.size(); i++){
                if(childAt(i).recSubtraction()) changes = true;
            }
            return changes;
        }
    }

    // Note: Call on main
    protected Node recSimplifyRationalN(){
        if(this.isVal() || this.isVar()) { return this; }
        else{
            Node n = recCopy(this);
            for(int i = 0; i < n.children.size(); i++){
                n.replaceChildAt(i,n.childAt(i).recSimplifyRationalN());
            }
            if(n.is("/")){
                if(n.childAt(0).is("/")){
                    Mult a = new Mult();
                    a.addChild(n.childAt(0).childAt(1));
                    a.addChild(n.childAt(1));
                    n.replaceChildAt(1, a);
                    n.replaceChildAt(0, n.childAt(0).childAt(0));
                    return n;
                }
                if(n.childAt(1).is("/")){
                    Mult a = new Mult();
                    a.addChild(n.childAt(0));
                    a.addChild(n.childAt(1).childAt(1));
                    n.replaceChildAt(0, a);
                    n.replaceChildAt(1, n.childAt(1).childAt(0));
                    return n;
                }
            }
            else if(n.is("*")){
                for(int j = 0; j < n.children.size(); j++){
                    if(n.childAt(j).is("/")){
                        Node a = recCopy(n);
                        a.replaceChildAt(j, n.childAt(j).childAt(0));
                        Node d = new Div();
                        d.addChild(a);
                        d.addChild(n.childAt(j).childAt(1));
                        return d;
                    }
                }
            }
            return n;
        }
    }

    protected Node recCopy(Node rootNode) {
        if(rootNode==null) {return null;}
        else{
            Node a = node(rootNode.data);
            for(int i = 0; i < rootNode.children.size(); i++) 
                a.addChild(recCopy(rootNode.childAt(i))); // Hopefully not a problem
            return a;
        }
    }

    // Deprecated
    protected Node removeConstants(){
        Node n = recCopy(this);
        for(int i = 0; i < children.size(); i++){
            if(childAt(i).isVal()){ 
                return n.removeAt(i);
            }
        }
        return n;
    }

    protected Node removeConstant(){
        for(int i = 0; i < children.size(); i++){
            if(childAt(i).isVal()){ 
                return removeAt(i);
            }
        }
        return null;
    }

    // Note: start at main
    protected Node foldConstants(){
        if(this.isVal() || this.isVar()) { return this; }
        else{

            Node copy = recCopy(this);
            Double v = null;

            try{
                double a = recCalculate(copy,false).getVal();
                return new Value(a + "");
            } catch(NumberFormatException e){
                // Do Nothing
            }

            copy = recCopy(this); // reset copy just in case
            for(int i = 0; i < copy.children.size(); i++){
                copy.replaceChildAt(i,copy.childAt(i).foldConstants());
            }

            switch(this.getClass().getName()){
                case("Add"):

                double sum = 0;
                for(int i = 0; i < copy.children.size(); i++){
                    if(copy.childAt(i).isVal()){
                        Value n = (Value)copy.childAt(i);
                        sum += n.getVal();
                        copy.removeAt(i);
                        i--;
                    }
                }
                if(sum != 0){
                    copy.addChild(new Value(sum + ""));
                }
                if(copy.children.size() == 1) return copy.childAt(0);
                break;

                case("Mult"):

                double product = 1;
                for(int i = 0; i < copy.children.size(); i++){
                    if(copy.childAt(i).isVal()){
                        Value n = (Value)copy.childAt(i);
                        if(n.getVal() == 1){
                            copy.removeAt(i);
                            i--;
                            continue;
                        }
                        product *= n.getVal();
                        copy.removeAt(i);
                        i--;
                    }
                }
                if(product == 0){
                    return new Value("0");
                }
                if(product != 1){
                    copy.addChild(new Value(product + ""));
                }
                else if(product == 1){

                }
                if(copy.children.size() == 1) return copy.childAt(0);
                break;

                // Forgetting about these for now, possibly irrelevant
                case("Exp"):
                if(copy.childAt(0).isVal()){
                    Value a = (Value)copy.childAt(0);
                    if(a.val == 0 || a.val == 1){ // 0^x OR 1^x
                        return new Value(a.val + "");
                    }

                }
                if(copy.childAt(1).isVal()){
                    Value a = (Value)copy.childAt(1);
                    if(a.val == 0){ // x^0
                        return new Value("1");
                    }
                    else if(a.val == 1){ //x^1
                        return copy.childAt(0);
                    }

                }
                break;

                case("Div"):
                // Check for either Values or Mults
                break;
            }
            return copy;
        }
    }

    public boolean structureEquals(Node in){
        if(getClass().getName().equals(in.getClass().getName())){
            switch(getClass().getName()){
                case("Add"): case("Mult"):
                if(children.size() != in.children.size()) return false;
                ArrayList<Integer> nots = new ArrayList<Integer>();
                for(int i = 0; i < children.size(); i++){
                    boolean b = false;
                    innerloop:
                    for(int j = 0; j < in.children.size(); j++){
                        if(!nots.contains(j) && childAt(i).structureEquals(in.childAt(j))){
                            nots.add(j);
                            b = true;
                            break innerloop;
                        }
                    }
                    if(!b) return false;
                }
                return true;

                case("Exp"): case("Div"):
                return childAt(0).structureEquals(in.childAt(0)) &&
                childAt(1).structureEquals(in.childAt(1));

                case("Variable"):
                return data.equals(in.data);

                case("Value"): 
                return ((Value)this).getVal() == ((Value)in).getVal();
            }
        }
        return false;
    }

    protected Node levelN(){
        if(this.isVal() || this.isVar()) { return this; }
        else{
            Node n = recCopy(this);
            for(int i = 0; i < n.children.size(); i++){
                n.replaceChildAt(i,n.childAt(i).levelN());
            }
            if(n.is("*") || n.is("+")){
                for(int i = 0; i < n.children.size(); i++){  // For all of the roots children
                    if(n.childAt(i).is(n.data)) {  // If the index child is the same as the parent
                        n.addChildren(n.childAt(i).children);
                        n.removeAt(i);
                        i--;
                    }
                }
            }
            return n;
        }
    }

    protected Node combineTermsN(){
        if(this.isVal() || this.isVar()) { return this; }
        Node n = recCopy(this);
        for(int i = 0; i < n.children.size(); i++)
            n.replaceChildAt(i,n.childAt(i).simplifyN());

        while(true){
            Node a = recCopy(n);
            n = n.setupN();
            if(n.structureEquals(a)) break;
        }

        switch(getClass().getName()){
            case("Add"):
            for(int i = 0; i < n.children.size(); i++){
                Node first = recCopy(n.childAt(i));
                Value const1 = (Value)first.removeConstant();
                for(int j = 0; j < n.children.size(); j++){
                    if(i!=j){
                        Node second = recCopy(n.childAt(j));
                        Value const2 = (Value)second.removeConstant();
                        if(first.structureEquals(second) && !first.isVal()){
                            Add a = const1.add(const2);
                            first.addChild(a);
                            n.replaceChildAt(i,first.simplifyN());
                            n.removeAt(j);
                            //i--;
                        }
                    }
                }
            }
            break;

            case("Mult"):

            for(int i = 0; i < n.children.size(); i++){ // Go through once
                if(!n.childAt(i).isVal()){
                    for(int j = 0; j < n.children.size(); j++){ // Twice
                        if(i!=j && !n.childAt(j).isVal()){
                            if(n.childAt(i).childAt(0).structureEquals(n.childAt(j).childAt(0))){
                                Add a = n.childAt(i).childAt(1).add(n.childAt(j).childAt(1));
                                n.childAt(i).replaceChildAt(1,a.simplifyN());
                                n.removeAt(j);
                            }
                        }
                    }
                }
            }
            break;
        }
        return n;
    }

    protected Node setupN(){
        if(this.isVal() || this.isVar()) { return this; }
        else{
            Node n = recCopy(this);
            for(int i = 0; i < n.children.size(); i++){
                n.replaceChildAt(i,n.childAt(i).setupN());
            }
            if(n instanceof Add){
                // All children must be either Mults or constants
                for(int i = 0; i < n.children.size(); i++){
                    //n.replaceChildAt(i,n.childAt(i).setupN());
                    if(!n.childAt(i).isVal() && !n.childAt(i).is("*")){
                        Mult a = n.childAt(i).multiply(new Value("1"));
                        n.replaceChildAt(i,a);
                    }
                }
            }
            else if(n instanceof Mult){
                // All children must be either Pows or constants
                for(int i = 0; i < n.children.size(); i++){
                    //n.replaceChildAt(i,n.childAt(i).setupN());
                    if(!n.childAt(i).isVal() && !n.childAt(i).is("^")){
                        Exp a = n.childAt(i).power(new Value("1"));
                        n.replaceChildAt(i,a);
                    }
                }
            }

            return n;
        }
    }

    protected Node simplifyN(){
        if(this.isVal() || this.isVar()) { return this; }
        Node n = recCopy(this);
        for(int i = 0; i < children.size(); i++) 
            n.replaceChildAt(i,childAt(i).simplifyN());

        Node a;
        do{
            a = recCopy(n);
            n = n.recNegateN();
            //System.out.println("After negate: " + n);
            n = n.levelN();
            //System.out.println("After level: " + n);
            n = n.recSimplifyRationalN();
            //System.out.println("After rational: " + n);
            n = n.combineTermsN();
            //System.out.println("After combine: " + n);
            n = n.foldConstants();
            //System.out.println("After fold: " + n);
        }while(!a.structureEquals(n));

        return n;
    }
}