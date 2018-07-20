package net.arksea.config.server.login;

import net.arksea.config.server.dao.UserDao;
import net.arksea.config.server.entity.User;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/11/1.
 */
@Component
@Transactional
public class LoginService {

    private static Logger logger = LogManager.getLogger(LoginService.class);
    @Autowired
    private UserDao userDao;
    private SecureRandom secureRandom;
    private final SecretKeyFactory secretKeyFactory;
    private final int KEY_ITERATION_COUNT = 10000;
    private final int KEY_BITS = 256;

    public LoginService() {
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("create SecureRandom failed", e);
        }
    }

    public boolean login(LoginInfo info) {
        try {
            List<User> rows = userDao.findByName(info.getName());
            if (rows.size() > 0) {
                User u = rows.get(0);
                String pwdhash = hashPassword(info.getPassword(), u.getSalt());
                boolean succeed = slowEquals(pwdhash.getBytes(), u.getPassword().getBytes());
                logger.info("User login, userName={}, succeed={}", u.getName(), succeed);
                return succeed;
            }
        } catch (Exception ex) {
            logger.warn("User login failed, userName={}", info.getName(), ex);
        }
        return false;
    }

    /**
     * 比较的时间与密码长度不为线性关系
     * @param a
     * @param b
     * @return
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) diff |= a[i] ^ b[i];
        return diff == 0;
    }

    public SignupStatus signup(SignupInfo info) {
        if (userDao.existsByName(info.getName())) {
            return SignupStatus.USERNAME_EXISTS;
        }
        try {
            String salt = createSalt();
            String pwdHash = hashPassword(info.getPassword(), salt);
            User user = new User();
            user.setName(info.getName());
            user.setEmail(info.getEmail());
            user.setPlainPassword(info.getPassword());
            user.setPassword(pwdHash);
            user.setSalt(salt);
            Date today = new Date();
            user.setRegisterDate(today);
            user.setLastLogin(today);
            userDao.save(user);
            logger.info("SignUp succeed， name={}, email={}", info.getName(), info.getEmail());
            return SignupStatus.SUCCEED;
        } catch (Exception ex) {
            logger.warn("SignUp failed: name={}, email={}" + info.getName(), info.getEmail(), ex);
            return SignupStatus.FAILED;
        }
    }

    private String createSalt() {
        byte bytes[] = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.encodeBase64String(bytes);
    }

    private String hashPassword(String pwd, String salt) throws Exception {
        final KeySpec keySpec = new PBEKeySpec(pwd.toCharArray(), Base64.decodeBase64(salt), KEY_ITERATION_COUNT, KEY_BITS);
        final SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        byte[] bytes = secretKey.getEncoded();
        return Base64.encodeBase64String(bytes);
    }
}
