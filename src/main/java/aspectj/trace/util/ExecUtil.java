package aspectj.trace.util;

import java.io.IOException;

/**
 * 调用命令行工具
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 4/13/16 11:46 PM.
 */
public class ExecUtil {

    /**
     * 执行命令
     *
     * @param command
     * @throws IOException
     * @throws InterruptedException
     */
    public static void exec(String command) {
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(command);
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "Error");
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "Output");
            errorGobbler.start();
            outputGobbler.start();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
