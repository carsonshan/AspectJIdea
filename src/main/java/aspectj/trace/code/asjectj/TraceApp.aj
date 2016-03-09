package aspectj.trace.code.asjectj;

/**
 * TraceApp Demo
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午4:40.
 */
public aspect TraceApp extends IndentedLogging {

    // 拦截 bar 的执行
    pointcut barPoint(): execution(* bar());

    // 拦截 foo 的执行
    pointcut fooPoint(): execution(* foo());

    // cflow的参数是一个pointcut
    pointcut barCFlow(): cflow(barPoint()) && !within(TraceApp);

    // 获取bar流程内的foo的方法的调用
    pointcut fooInBar(): barCFlow() && fooPoint();

    // 获取main函数的执行
    pointcut mainPoint(): execution(* main(..));

    protected pointcut loggedOperations():
//            (execution(* *.*(..))
//            || execution(*.new(..)))
            cflow(mainPoint()) && !within(IndentedLogging+);

    before(): loggedOperations() {
        String methodName = thisJoinPoint.getStaticPart().getSignature().toString();
        String sourceLine = thisJoinPoint.getStaticPart().getSourceLocation().toString();
        if (!methodName.contains("java")) {
            if (this.caller != null && !this.caller.contains("init")) {
                System.out.print(this.caller + " ");
                System.out.println("-->" + methodName + " at " + sourceLine);
            }
        }
    }
}