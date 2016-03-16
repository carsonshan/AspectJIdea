package aspectj.trace.code.app;

/**
 * 测试应用程序
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午4:38.
 */
public class App {

    public void A1() {
        System.out.println("A1");
        A2();
        B1();
        B2();
        B3();
    }

    public void A2() {
        System.out.println("A2");
        A3();
        B4();
    }

    public void A3() {
        System.out.println("A3");
        A4();
        B5();
    }

    public void A4() {
        System.out.println("A4");
        A5();
        B6();
    }

    public void A5() {
        System.out.println("A5");
        A6();
        B7();
    }

    public void A6() {
        System.out.println("A6");
        A7();
        B8();
    }

    public void A7() {
        System.out.println("A7");
        A8();
        B9();
    }

    public void A8() {
        System.out.println("A8");
        A9();
        B10();
    }

    public void A9() {
        System.out.println("A9");
        A10();
        B1();
    }

    public void A10() {
        System.out.println("A10");
        B2();
    }

    public void B1() {
        System.out.println("B1");
    }

    public void B2() {
        System.out.println("B2");
    }

    public void B3() {
        System.out.println("B3");
    }

    public void B4() {
        System.out.println("B4");
    }

    public void B5() {
        System.out.println("B5");
    }

    public void B6() {
        System.out.println("B6");
    }

    public void B7() {
        System.out.println("B7");
    }

    public void B8() {
        System.out.println("B8");
    }

    public void B9() {
        System.out.println("B9");
    }

    public void B10() {
        System.out.println("B10");
    }

    public static void main(String[] args) {
        App app = new App();
        app.A1();
    }
}