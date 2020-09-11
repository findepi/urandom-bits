package findepi.math;

import static java.lang.Math.floorDiv;

public class Divide
{
    public static void main(String[] args)
    {
        doIt(13, 10);
        doIt(-13, 10);

        doIt(13, -10);
        doIt(-13, -10);
    }

    private static void doIt(int a, int b)
    {
        System.out.println();
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("a / b = " + a / b);
        System.out.println("floorDiv(a,b) = " + floorDiv(a, b));
        System.out.println("a % b = " + a % b);
        System.out.println("Math.floorMod(a,b) = " + Math.floorMod(a, b));
    }
}
