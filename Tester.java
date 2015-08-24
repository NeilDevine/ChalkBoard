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
    private JTextField inputText1 = new JTextField(25);

    private JButton calculateButton1 = new JButton("Calculate");
    private JButton calculateButton2 = new JButton("Calculate with steps");
    private JButton calculateButton3 = new JButton("Expand");


    /**
     * Constructor for objects of class MainWindow
     */
    public Tester()
    {
        super("Derivative Calculator");

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
        p.add(calculateButton1);

        constraints.gridx = 10;
        constraints.gridy = 2;
        p.add(calculateButton2);

        constraints.gridx = 0;
        constraints.gridy = 2;
        p.add(calculateButton3);

        calculateButton1.addActionListener(this);
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
     *The main method.
     */
    public static void main(String[] args)
    { 
        new Tester();//Reads method main()
    }

    /* 
     *What the button does.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String fx = inputText1.getText();
        if(e.getActionCommand().equals("Calculate"))
            System.out.println(new Expression(Expression.parse(fx)).calculate(false));
        else if(e.getActionCommand().equals("Calculate with steps"))
            System.out.println(new Expression(Expression.parse(fx)).calculate(true));
        else{
            Expression a = new Expression(Expression.parse(fx));
            a.expand();
            a.printTree();
        }
        System.out.println();
    }
}
