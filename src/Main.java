
public class Main {
    public static void main(String[] args) {
        long start = System.nanoTime();
        new Frame();
        long end = (System.nanoTime());
        System.out.println(end-start);
    }
}