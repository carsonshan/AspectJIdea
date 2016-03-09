package aspectj.trace.code.asjectj;

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
    protected String caller;// 方法调用者

    before(): loggedOperations() {
        _indentationLevel++;
        this.caller = thisJoinPoint.getStaticPart().getSignature().getName();
    }

    after(): loggedOperations() {
        _indentationLevel--;
        this.caller = thisJoinPoint.getStaticPart().getSignature().getName();
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