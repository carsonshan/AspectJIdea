package aspectj.in.action.ch05.aspect;

import org.apache.log4j.Logger;
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

    before(): traceMethods() {
        Signature sig = thisJoinPointStaticPart.getSignature();
        _logger.info(sig.getDeclaringType().getName() + " -> " + sig.getName() + " : Entering");
    }
}
