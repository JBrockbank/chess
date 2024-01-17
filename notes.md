# My notes
X.java contains X class

public class SimpleJavaClass {

    private static int x;
    private int a;
    default visibility is package private


    public static void main(String[] args) {
        x = 12;
        a = 13; - Doesn't work
        
        System.out.println("Hello from SimpleJavaClass");

        for (int i = 0; i < args.length; ++i) {
            System.out.println(args[i]);
        }
        
        for (String a : args) {
            System.out
        }

    }

}

