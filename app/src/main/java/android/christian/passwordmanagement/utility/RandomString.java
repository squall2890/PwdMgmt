package android.christian.passwordmanagement.utility;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Christian on 31/03/2018.
 */

public class RandomString {

    public static final String LOWERCASE = "abcdefghilmnopqrstuvz";
    public static final String UPPERCASE = LOWERCASE.toUpperCase();
    public static final String SPECIALCHAR = "+@*#%&()=[]{}-_:;><|,.";
    public static final String DIGITS = "0123456789";
    public static final String LOWER = LOWERCASE + UPPERCASE;
    public static final String MEDIUM = LOWER + DIGITS;
    public static final String HARD = MEDIUM + SPECIALCHAR;

    private final Random random;
    private final char[] symbols;
    private final char[] buf;

    public RandomString(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create random String
     */
    public RandomString(int length, Random random) {

        this(length, random, HARD);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public RandomString(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public RandomString() {
        this(21);
    }

    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}