package wechat_clj;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JavaHelper {

    public static String SHA1(String str) {
        try {
            MessageDigest digest = java.security.MessageDigest
                .getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexStr = new StringBuffer();

            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
