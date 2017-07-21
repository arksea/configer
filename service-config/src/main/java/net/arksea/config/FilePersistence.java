package net.arksea.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

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
    private long lastUpdateTime;
    private long MIN_SAVE_INTERVAL = 10_000; //最小存储操作的间隔，防止频繁的存储操作带来的IO压力

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
            long now = System.currentTimeMillis();
            if (now - lastUpdateTime > MIN_SAVE_INTERVAL) {
                synchronized (this) {
                    if (now - lastUpdateTime > MIN_SAVE_INTERVAL) {
                        lastUpdateTime = System.currentTimeMillis();
                        save();
                    }
                }
            }
        }
    }

    private void load() {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            int lineNo = 0;
            for (String str : list) {
                ++lineNo;
                int n = str.indexOf('=');
                if (n >= 0) {
                    String key = str.substring(0, n).trim();
                    String value = str.substring(n + 1);
                    if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
                        logger.warn("配置文件{}, 第{}行格式错误", file.getPath(), lineNo);
                    } else {
                        configMap.put(key, value);
                    }
                } else {
                    logger.warn("配置文件{}, 第{}行格式错误", file.getPath(), lineNo);
                }
            }
        } catch (IOException ex) {
            logger.warn("读取配置文件失败: {}", file.getPath(), ex);
        }
    }

    private void save() {
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> e : configMap.entrySet()) {
                sb.append(e.getKey()).append("=").append(e.getValue()).append("\n");
            }
            Files.write(file.toPath(), sb.toString().getBytes("UTF-8"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (IOException ex) {
            logger.warn("保存配置文件失败: {}", file.getPath(), ex);
        }
    }
}
