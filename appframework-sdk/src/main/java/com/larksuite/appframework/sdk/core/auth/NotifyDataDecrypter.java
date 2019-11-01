/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.auth;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class NotifyDataDecrypter {

    private byte[] key;

    public NotifyDataDecrypter(String keyStr) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // won't happen
        }
        key = digest.digest(keyStr.getBytes(StandardCharsets.UTF_8));
    }

    public String decrypt(String base64) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {

        byte[] decode = Base64.getDecoder().decode(base64);

        Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");

        byte[] iv = new byte[16];
        System.arraycopy(decode, 0, iv, 0, 16);

        byte[] data = new byte[decode.length - 16];
        System.arraycopy(decode, 16, data, 0, data.length);

        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

        return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
    }
}