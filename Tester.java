import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 * Just the main window with actions and other fun stuff.
 * 
 * @author (Neil Devine) 
 * @version (Very very early stages of development)
 */
public class Tester extends JFrame implements ActionListener
{
    private JLabel inputLabel1 = new JLabel("Enter in an Expression: ");
    private JLabel inputLabel2 = new JLabel("Test Maths Class calculate: ");
    private JLabel inputLabel3 = new JLabel("Test Expression Class calculate: ");
    private JLabel inputLabel4 = new JLabel("Simplify method: ");
    private JTextField inputText1 = new JTextField(25);

    private JButton calculateButton1 = new JButton("Fold Constants");
    private JButton calculateButton2 = new JButton("Calculate with steps");
    private JButton calculateButton3 = new JButton("Simplify");
    private JButton calculateButton4 = new JButton("Combine Like Terms");

    /**
     * Constructor for objects of class MainWindow
     */
    public Tester()
    {
        super("ChalkBoard - Testing");

        JPanel p = new JPanel(new GridBagLayout());
        JPanel a = new JPanel();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(30, 30, 30, 30);

        //add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 0;     
        p.add(inputLabel1, constraints);

        constraints.gridx = 1;
        p.add(inputText1, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        //constraints.gridwidth = 3;

        //constraints.anchor = GridBagConstraints.CENTER;
        p.add(calculateButton2);

        constraints.gridx = 10;
        constraints.gridy = 2;
        p.add(calculateButton3);

        calculateButton2.addActionListener(this);
        calculateButton3.addActionListener(this);

        //set border for the panel
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "String Calculator Panel"));
        a.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Expression Calculator Panel"));
        //add the panel to this frame
        add(p);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /* 
     *What the button does.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String fx = inputText1.getText();
        Expression a;
        switch(e.getActionCommand()){
            case("Fold Constants"):
            a = new Expression(Expression.parse(fx));
            a.root = a.root.recNegateN();
            a.root = a.root.levelN();
            a.root = a.root.foldConstants();
            System.out.println(a.root);
            break;

            case("Calculate with steps"):
            a = new Expression(Expression.parse(fx));
            System.out.println("You entered: " + a.root);
            System.out.println(a.calculate(true));
            break;

            case("Combine Like Terms"):
            a = new Expression(Expression.parse(fx));

            System.out.println("Original: " + a.root);

            a.root = a.root.recNegateN();
            System.out.println("After negated: " + a.root);
            a.root = a.root.levelN();
            System.out.println("After leveled Ops: " + a.root);
            a.root = a.root.combineTermsN();
            System.out.println("After Combined Terms: " + a.root);
            break;

            case("Simplify"):
            a = new Expression(Expression.parse(fx));
            System.out.println("You entered: " + a.root);
            a.root = a.root.simplifyN();
            System.out.println("Simplified: " + a.root);
            break;
        }

        System.out.println();
    }

    public static void main(){
        Tester test = new Tester();
    }
}
