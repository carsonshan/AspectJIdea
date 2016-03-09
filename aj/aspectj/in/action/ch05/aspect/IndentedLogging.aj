package aspectj.in.action.ch05.aspect;

/**
 * IndentedLogging
 * 控制缩进
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/7 下午10:01.
 */
public abstract aspect IndentedLogging {
    protected int _indentationLevel = 0;

    protected abstract pointcut loggedOperations();

    before(): loggedOperations() {
        _indentationLevel++;
    }

    after(): loggedOperations() {
        _indentationLevel--;
    }

    before():
            call(* java.io.PrintStream.println(..)) &&
            within(IndentedLogging+) {
        for (int i = 0, spaces = _indentationLevel * 4;
             i < spaces; ++i) {
            System.out.print(" ");
        }
    }
}