

public class MainTest {

    public static void main(String[] args) throws Exception {
        float posInfty = Float.POSITIVE_INFINITY;
        float negInfty = Float.NEGATIVE_INFINITY;
        System.out.println(posInfty > negInfty);
        System.out.println(negInfty < posInfty);
        System.out.println(0 < posInfty);
        System.out.println(0 > negInfty);
        System.out.println(posInfty == posInfty);
        System.out.println(negInfty == negInfty);
        System.out.println(1 / 0f);
        System.out.println(-1 / 0f);
        System.out.println(0 / 0f);

    }

}
