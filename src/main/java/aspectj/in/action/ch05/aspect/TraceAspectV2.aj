package aspectj.in.action.ch05.aspect;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.Signature;

/**
 * TraceAspectV2
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/7 下午8:31.
 */
public aspect TraceAspectV2 {
    // Obtaining the logger object
    private Logger _logger = Logger.getLogger("trace");

    // Initializing the log level
    TraceAspectV2() {
        _logger.setLevel(Level.ALL);
    }

    pointcut traceMethods(): (execution(* *.*(..))||execution(*.new(..)))&&!within(TraceAspectV2);

    before():traceMethods(){
        Signature sig = thisJoinPointStaticPart.getSignature();
        _logger.info(sig.getDeclaringType().getName() + sig.getName() + "Entering");
    }
}
