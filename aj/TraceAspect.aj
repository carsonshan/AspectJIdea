package aspectj.in.action.ch05.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

/**
 * TraceAspect
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午8:13.
 */
public aspect TraceAspect {

    private Logger _logger = Logger.getLogger("trace");

    pointcut traceMethods()
            : execution(* *.*(..)) && !within(TraceAspect);

    /**
     * We use the !execution(String *.toString())pointcut to avoid
     * the recursion that will be caused by the execution of the toString()
     * methods. Without this pointcut, the logger will prepare the parameter
     * string in createParameter- Message() when it calls toString() for each
     * object. However, when toString() executes, it first attempts to log the
     * operation, and the logger will prepare a parameter string for it again
     * when it calls toString() on the same object, and so on, causing an infinite
     * recursion. By avoiding the join points for toString() execution, we avoid
     * infinite recursion, leading to a stack overflow. Note that the !within(TraceAspectV1)
     * pointcut is not sufficient here because it will only capture the calls to toString()
     * methods; the execution of the methods will still be advised.
     */
    before(): traceMethods() && !execution(String *.toString()) {
        Signature sig = thisJoinPointStaticPart.getSignature();
        System.err.println("Entering ["
                + sig.getDeclaringType().getName() + "."
                + sig.getName() + "]"
                + createParameterMessage(thisJoinPoint));
    }

    /**
     * 格式化参数信息
     *
     * @param joinPoint
     * @return
     */
    private String createParameterMessage(JoinPoint joinPoint) {
        StringBuffer paramBuffer = new StringBuffer("\n\t[This: ");
        paramBuffer.append(joinPoint.getThis());
        Object[] arguments = joinPoint.getArgs();
        paramBuffer.append("]\n\t[Args: (");
        for (int length = arguments.length, i = 0; i < length; ++i) {
            Object argument = arguments[i];
            paramBuffer.append(argument);
            if (i != length - 1) {
                paramBuffer.append(',');
            }
        }
        paramBuffer.append(")]");
        return paramBuffer.toString();
    }
}
