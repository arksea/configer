package net.arksea.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 * Created by xiaohaixing on 2017/7/21.
 */
public class FilePersistence implements IConfigPersistence {
    private static Logger logger = LogManager.getLogger(FilePersistence.class);
    private final File file;
    private final Map<String,String> configMap = new ConcurrentHashMap<>();

    public FilePersistence(String filePath)  {
        file = new File(filePath);
        File dir = file.getParentFile();
        if (file.exists()) {
            load();
        } else if (!dir.exists()){
            try {
                Files.createDirectories(dir.toPath());
            } catch (IOException ex) {
                logger.warn("创建配置文件目录失败: {}", dir.getPath(), ex);
            }
        }
    }

    public String read(String key) {
        return configMap.get(key);
    }

    public void update(String key, String value) {
        String old = configMap.put(key, value);
        if (old == null || old!=value && !old.equals(value)) {
            save();
        }
    }

    private void load() {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            String key = null;
            StringBuilder value = new StringBuilder();
            for (String str : list) {
                int n = str.indexOf("->");
                if (n >= 0) {
                    if (key != null) {
                        configMap.put(key, value.toString());
                        value.setLength(0);
                    }
                    key = str.substring(0, n).trim();
                } else {
                    value.append(str).append('\n');
                }
            }
        } catch (IOException ex) {
            logger.warn("读取配置文件失败: {}", file.getPath(), ex);
        }
    }

    private synchronized void save() {
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> e : configMap.entrySet()) {
                sb.append(e.getKey()).append(" ->\n").append(e.getValue());
                if (!e.getValue().endsWith("\n")) {
                    sb.append("\n");
                }
            }
            Files.write(file.toPath(), sb.toString().getBytes("UTF-8"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (IOException ex) {
            logger.warn("保存配置文件失败: {}", file.getPath(), ex);
        }
    }
}
