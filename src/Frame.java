import javax.swing.JFrame;
import java.util.ArrayList;

public class Frame extends JFrame {
    Frame() {
        ArrayList<Complex> complexNumbers = new ArrayList<>();
         double increment = 0.00625;
        //increment = 0.003125;
        //increment = .0125;

        for (double real = -2; real < 2; real += increment) {
            for (double imaginary = -2; imaginary < 0; imaginary += increment) {
                Complex c = new Complex(real, imaginary);
                if (c.getMagnitude() < 2.0d) {

                    complexNumbers.add(c);
                }
            }
        }

        //System.out.println(complexNumbers.size());
        setTitle("Mandel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new Panel(complexNumbers));
        pack();
        setVisible(true);
    }
}