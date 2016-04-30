package aspectj.trace.util;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Libgen
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 4/30/16 9:42 AM.
 */
public class Libgen {

    Logger logger = Logger.getLogger(getClass());

    /**
     * 下载torrent
     */
    private void download() {
        String fileName = "/Volumes/Mac/Libgen/repository_torrent/magnet-links.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String cmd;
                line = line.split("magnet")[0];
                line = line.trim();
                line = "http://libgen.io/repository_torrent/" + line;
                cmd = "wget -P /Volumes/Mac/Libgen/repository_torrent " + line;
                ExecUtil.exec(cmd);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载最新的torrent
     */
    private void compare() {
        String fileName = "/Volumes/Mac/Libgen/repository_torrent/magnet-links.txt";
        String fileNameNew = "/Volumes/Mac/Libgen/repository_torrent/magnet-links-new.txt";

        Map<String, Boolean> oldMap = new HashMap<String, Boolean>();
        try {
            // 查找不同的
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.split("magnet")[0];
                line = line.trim();
                oldMap.put(line, true);
            }
            // 查找不同然后下载
            reader = new BufferedReader(new FileReader(fileNameNew));
            while ((line = reader.readLine()) != null) {
                String cmd;
                line = line.split(".torrent")[0];
                line = line.trim();
                line += ".torrent";
                if (!oldMap.containsKey(line)) {
                    logger.debug(line);
                    line = "http://libgen.io/repository_torrent/" + line;
                    cmd = "wget -P /Volumes/Mac/Libgen/repository_torrent " + line;
                    ExecUtil.exec(cmd);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Libgen libgen = new Libgen();
        libgen.compare();
    }
}
