package net.arksea.config;

import akka.dispatch.OnComplete;
import net.arksea.acache.DataResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.concurrent.Future;

/**
 *
 * Created by xiaohaixing on 2017/7/21.
 */
public abstract class AbstractConfig<T> {
    private static Logger logger = LogManager.getLogger(AbstractConfig.class);
    private final String key;
    private volatile T value;
    private final ConfigService service;

    public AbstractConfig(String key, T defaultValue, ConfigService service) {
        this.key = key;
        this.service = service;
        recover();
        if ( value == null) {
            this.value = defaultValue;
        }
        Future<DataResult<String,String>> f = service.ask(key);
        onCompleted(f);
    }

    public AbstractConfig(String key, ConfigService service) {
        this.key = key;
        this.service = service;
        recover();
        try {
            String str = service.get(key);
            value = parseValue(str);
        } catch (Exception ex) {
            if (value == null) {
                logger.error("从配置服务初始化配置失败: {}", key, ex);
            } else {
                logger.warn("从配置服务更新配置失败: {}, 当前配置: {}", key, value, ex);
            }
        }
    }

    public T get() {
        onCompleted(service.ask(key));
        return value;
    }

    public T syncGet() {
        String str = service.get(key);
        value = parseValue(str);
        return value;
    }

    private void onCompleted(Future<DataResult<String,String>> f) {
        f.onComplete(
            new OnComplete<DataResult<String,String>>() {
                @Override
                public void onComplete(Throwable ex, DataResult<String,String> result) throws Throwable {
                    if (ex == null && result.failed == null) {
                        if (result.data == null) {
                            logger.error("从配置服务取到的配置项为空串：{}", key);
                        } else {
                            try {
                                value = parseValue(result.data);
                            } catch (Throwable ex1) {
                                logger.error("从配置服务取到的配置格式错误：{}={}", key, result.data);
                            }
                        }
                    } else {
                        if (value == null) {
                            logger.error("从配置服务初始化配置失败: {}", key, ex == null ? result.failed : ex);
                        } else {
                            logger.warn("从配置服务更新配置失败: {}, 当前配置: {}", key, value, ex == null ? result.failed : ex);
                        }
                    }
                }
            },
            service.system.dispatcher());
    }


    private void recover() {
        try {
            String str = loadStoredValue();
            if (str != null) {
                this.value = parseValue(str);
            }
        } catch (Exception ex) {
            logger.warn("恢复持久化的配置失败: {}", key, ex);
        }
    }

    protected String loadStoredValue() {

        return null;
    }

    protected abstract T parseValue(String str);
}
