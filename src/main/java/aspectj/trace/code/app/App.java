package aspectj.trace.code.app;

/**
 * 测试应用程序
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午4:38.
 */
public class App {

    public void A1() {
        System.out.println("In A1");
        A2();
        B1();
        B2();
        B3();
    }

    public void A2() {
        System.out.println("In A2");
        A3();
        B4();
    }

    public void A3() {
        System.out.println("In A3");
        A4();
        B5();
    }

    public void A4() {
        System.out.println("In A4");
        A5();
        B6();
    }

    public void A5() {
        System.out.println("In A5");
        A6();
        B7();
    }

    public void A6() {
        System.out.println("In A6");
        A7();
        B8();
    }

    public void A7() {
        System.out.println("In A7");
        A8();
        B9();
    }

    public void A8() {
        System.out.println("In A8");
        A9();
        B10();
    }

    public void A9() {
        System.out.println("In A9");
        A10();
        B1();
    }

    public void A10() {
        System.out.println("In A10");
        B2();
    }

    public void B1() {
        System.out.println("In B1");
    }

    public void B2() {
        System.out.println("In B2");
    }

    public void B3() {
        System.out.println("In B3");
    }

    public void B4() {
        System.out.println("In B4");
    }

    public void B5() {
        System.out.println("In B5");
    }

    public void B6() {
        System.out.println("In B6");
    }

    public void B7() {
        System.out.println("In B7");
    }

    public void B8() {
        System.out.println("In B8");
    }

    public void B9() {
        System.out.println("In B9");
    }

    public void B10() {
        System.out.println("In B10");
        C1();
        C2();
        C3();
        C4();
        C5();
    }

    public void C1() {
        System.out.println("In C1");
        C2();
        C3();
        C4();
        C5();
    }

    public void C2() {
        System.out.println("In C2");
        C3();
        C4();
        C5();
    }

    public void C3() {
        System.out.println("In C3");
        C4();
        C5();
    }

    public void C4() {
        System.out.println("In C4");
        C5();
    }

    public void C5() {
        System.out.println("In C5");
    }

    public static void main(String[] args) {
        App app = new App();
        app.A1();
    }
}