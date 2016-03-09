package aspectj.trace.code.app;

/**
 * 测试应用程序
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午4:38.
 */
public class App {

    private void foo2() {
        foo(11, "foo2");
    }

    private void foo(int number, String name) {
        System.out.println("Inside App->foo(), number = " + number + ", name = " + name);
    }

    private void bar() {
        foo(123, "Like");
        System.out.println("Call bar()......");
        foo2();
    }

    public static void main(String[] args) {
        App app = new App();
        app.bar();
        app.foo(123, "Like");
    }
}