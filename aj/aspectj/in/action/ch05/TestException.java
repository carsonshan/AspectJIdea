package aspectj.in.action.ch05;

/**
 * TestException
 * Just for testing the exception aspect
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/7 下午9:29.
 */
public class TestException {

    public static void main(String[] args) {
        perform();
    }

    public static void perform() {
        Object nullObj = null;
        nullObj.toString();
    }
}
