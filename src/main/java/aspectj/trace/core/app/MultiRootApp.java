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
        A3();
    }

    public void A2() {
        System.out.println("In A2");
    }

    public void A3() {
        System.out.println("In A3");
    }

    public void A4() {
        System.out.println("In A4");
    }

    public static void main(String[] args) {
        MultiRootApp multiRootApp = new MultiRootApp();
        multiRootApp.A1();
        multiRootApp.A1();
    }
}
