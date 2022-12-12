package com.uriencedric.authorization.utils.encoder;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uriencedric.authorization.utils.AppConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PBKDF2PasswordEncoder implements PasswordEncoder {

    private final String salt;
    private final SecretKeyFactory factory;

    /**
     * @param salt
     * @throws NoSuchAlgorithmException
     */
    public PBKDF2PasswordEncoder(String salt) throws NoSuchAlgorithmException {
        this.salt = salt;
        this.factory = SecretKeyFactory.getInstance(AppConstants.PBKDF_2_WITH_HMAC_SHA_1);
    }

    /**
     * @param str password to hash
     * @return hash in bytes of the char sequence given
     */
    private byte[] hashPassword(CharSequence str) {
        KeySpec spec = new PBEKeySpec(str.toString().toCharArray(), salt.getBytes(),
                AppConstants.PBKDF2_ITERATION_COUNT, AppConstants.PBKDF2_KEY_LENGTH);

        try {
            return this.factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            log.error("Cannot hash password : " + e.getMessage());
            throw new RuntimeException("Cannot hash password : " + e.getMessage());
        }
    }

    /**
     * @param rawPassword     Password to test match
     * @param encodedPassword Hashed password
     * @return True is password matches, false otherwise
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        byte[] currentEncoded = this.hashPassword(rawPassword);

        String currentEncodedStr = Hex.encodeHexString(currentEncoded);
        return encodedPassword.equals(currentEncodedStr);

    }

    /**
     * @param rawPassword Password to hash
     * @return Hash of the password
     */
    @Override
    public String encode(CharSequence rawPassword) {
        byte[] hash = this.hashPassword(rawPassword);

        return Hex.encodeHexString(hash);
    }
}
