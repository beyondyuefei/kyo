package  com.ruijie.framework.kyo.common.utils;

import java.util.Random;

/**
 * random utils
 */
public class RandomUtils {

    /**
     * character table
     */
    private static final char[] CHAR_TABLE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * number table
     */
    private static final char[] NUM_TABLE = "0123456789".toCharArray();

    /**
     * number table
     */
    private static final char[] NUM_CHAR_TABLE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * genernate random string	
     * @param length length
     * @return random string
     */
    public static String randomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int pos = random.nextInt(CHAR_TABLE.length);
            sb.append(CHAR_TABLE[pos]);
        }

        return sb.toString();
    }

    /**
     * genernate random num  
     * @param length length
     * @return random string
     */
    public static String randomNum(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int pos = random.nextInt(NUM_TABLE.length);
            sb.append(NUM_TABLE[pos]);
        }

        return sb.toString();
    }

    /**
     * random byte	
     * @return random byte
     */
    public static byte randomByte() {
        Random random = new Random();
        return (byte) random.nextInt();
    }

    /**
     * genernate random code  
     * @param length length
     * @return random string
     */
    public static String randomCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int pos = random.nextInt(NUM_CHAR_TABLE.length);
            sb.append(NUM_CHAR_TABLE[pos]);
        }

        return sb.toString();
    }
    
    public static void main(String[] args) {
    	System.out.println(randomNum(6));
    }

}
