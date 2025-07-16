public class triangle {
    public static void printTriangle(int row) {
        for (int i = 0; i < row; i++) {
            String s = "";
            for (int j = 0; j <= i; j++) {
                s += '*';
            }
            System.out.println(s);
        }
    }
    public static void main(String[] arg) {
        printTriangle(5);
    }
}
