public class Complex {
    private double numR;
    private double numI;

    public Complex(double r, double i) {
        numR = r;
        numI = i;
    }

    public String get() {
        return numR + "+" + numI + "i";
    }

    public Complex add(Complex other) {
        return new Complex(this.numR + other.numR, this.numI + other.numI);
    }

    public Complex multiply(Complex other) {
        double newReal = this.numR * other.numR - this.numI * other.numI;
        double newImaginary = this.numR * other.numI + this.numI * other.numR;
        return new Complex(newReal, newImaginary);
    }

    public String toString() {
        return numR + "+" + numI + "i";
    }

    public double getMagnitude() {
        return Math.sqrt(numR * numR + numI * numI);
    }

    public double getReal() {
        return numR;
    }

    public double getImaginary() {
        return numI;
    }

    public static void main(String[] args) {
        Complex c = new Complex(1, 1);
        Complex cC = new Complex(1, 1);
        c = c.multiply(c);
        c = c.add(cC);
        System.out.println(c);
    }

    public static int runIterations(Complex c) {
        Complex cConst = c;
        int i = 0;
        //System.out.println(c);
        //System.out.println("Starting |z| = " + c.getMagnitude());
        while (c.getMagnitude() < 2 && i < 150) {
            c = c.multiply(c);
            c = c.add(cConst);
            i++;
        }
        return i-1;
    }
}
