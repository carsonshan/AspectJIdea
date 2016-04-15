package aspectj.trace.core.app;

/**
 * MultiRootApp
 * 有多个根节点
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 4/14/16 9:44 PM.
 */
public class MultiRootApp {

    public void A1() {
        System.out.println("In A1");
        A2();
        A2();
    }

    public void A2() {
        A3();
        A4();
        System.out.println("In A2");
    }

    public void A3() {
        A5();
        A6();
        System.out.println("In A3");
    }

    public void A4() {
        A5();
        A6();
        System.out.println("In A4");
    }

    public void A5() {
        System.out.println("In A5");
    }

    public void A6() {
        System.out.println("In A6");
    }

    public static void main(String[] args) {
        MultiRootApp multiRootApp = new MultiRootApp();
        multiRootApp.A1();
    }
}
