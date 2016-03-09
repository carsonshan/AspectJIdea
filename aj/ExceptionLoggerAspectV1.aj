package aspectj.in.action.ch05.aspect;

import org.aspectj.lang.Signature;

/**
 * ExceptionLoggerAspectV1
 * 捕获异常信息的aspect
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/7 下午9:25.
 */
public aspect ExceptionLoggerAspectV1 {
    // Traced method
    pointcut exceptionLogMethods()
            : call(* *.*(..)) && !within(ExceptionLoggerAspectV1);

    after() throwing(Throwable ex): exceptionLogMethods() {
        Signature sig = thisJoinPointStaticPart.getSignature();
        System.err.println("Exception logger aspect ["
                + sig.getDeclaringType().getName() + "."
                + sig.getName() + "]");
        ex.printStackTrace(System.err);
    }
}
