/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;
import com.larksuite.appframework.sdk.core.protocol.UploadImageRequest;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.MixUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

/**
 * A tool to manage image keys.
 *
 * Mostly robots use some concrete images, we don't need to upload image every time,
 * that means we can cache the image keys in some persistent storage such as redis.
 *
 * Every concrete image is used in some specific situation, we can name a image like "welcomeCard.banner" which meanings the image is used in a card message as a banner.
 * So we can build a kay-value cache which maps the "imageName" and "imageKey" returned by lark client.
 *
 * Sometimes, we may want to change the image named "welcomeCard.banner" to another that meaning we should rebuild the relation of "imageName to imageKey".
 * Diff the MD5 codes of images is a way to distinguish whether the images are the same.
 * We calculate the MD5 code of a image, then diff the codes between the image and the one who has the same imageName and is already uploaded.
 * However, it's still costly that calculating MD5 code of the image every time we use it.
 * We use a lru cache to reduce duplicated calculation which produces a latent period before changing image operation taking effect.
 *
 */
public class ImageKeyManager {

    private OpenApiClient openApiClient;

    private TokenCenter tokenCenter;

    private App app;

    private ImageKeyStorage imageKeyStorage;

    /**
     * imageName -> md5
     */
    private Cache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public ImageKeyManager(App app, OpenApiClient openApiClient, TokenCenter tokenCenter, ImageKeyStorage imageKeyStorage) {
        this.openApiClient = openApiClient;
        this.tokenCenter = tokenCenter;
        this.app = app;
        this.imageKeyStorage = imageKeyStorage;
    }

    public String registerImage(String imageName, InputStream imageContent) throws LarkClientException {
        if (app.getIsIsv()) {
            throw new UnsupportedOperationException("ISV app should use registerImageIsv");
        }
        return registerImageIsv(imageName, imageContent, null);
    }

    /**
     * register a image for management.
     *
     * @param tenantKey
     * @param imageName user defined unique name
     * @param imageContent
     * @return imageKey
     * @throws LarkClientException
     */
    public String registerImageIsv(String imageName, InputStream imageContent, String tenantKey) throws LarkClientException {

        ImageKeyStorage.ImageInfo imageInfo = imageKeyStorage.loadImageInfo(app.getAppId(), app.getAppShortName(), imageName);

        try {
            if (imageInfo == null) {
                byte[] bytes = streamToBytes(imageContent);
                String md5 = bytesToMd5HexString(bytes);
                return uploadImageAndSave(tenantKey, imageName, bytes, md5);
            }

            boolean isMd5FromCache = true;
            byte[] bytes = null;
            String md5 = cache.getIfPresent(imageName);
            if (md5 == null) {
                bytes = streamToBytes(imageContent);
                md5 = bytesToMd5HexString(bytes);
                isMd5FromCache = false;
            }

            if (md5.equals(imageInfo.getMd5())) {
                if (!isMd5FromCache) {
                    cache.put(imageName, md5);
                }
                return imageInfo.getImageKey();
            }

            if (bytes == null) {
                bytes = streamToBytes(imageContent);
            }

            // md5 is from cache, but not matched, recalculate it
            if (isMd5FromCache) {
                md5 = bytesToMd5HexString(bytes);
            }

            return uploadImageAndSave(tenantKey, imageName, bytes, md5);

        } catch (IOException ioe) {
            throw new LarkClientException(LarkClientException.UNKNOWN_EXCEPTION_ERR_CODE, "registerImage exception", ioe);
        }
    }


    public String getImageKey(String imageName) {
        ImageKeyStorage.ImageInfo imageInfo = imageKeyStorage.loadImageInfo(app.getAppId(), app.getAppShortName(), imageName);
        if (imageInfo == null) {
            return null;
        }
        return imageInfo.getImageKey();
    }

    private byte[] streamToBytes(InputStream is) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            MixUtils.copyStream(is, bos);
            return bos.toByteArray();
        }
    }

    private static String bytesToMd5HexString(byte[] bytes) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            // wont happen
        }

        byte[] digest = md.digest(bytes);
        StringBuilder sb = new StringBuilder(32);
        for (byte b : digest) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }


    private String uploadImageAndSave(String tenantKey, String imageName, byte[] bytes, String md5) throws LarkClientException {

        String tenantAccessToken = tokenCenter.getTenantAccessToken(tenantKey);
        if (tenantAccessToken == null) {
            throw new LarkClientException("cannot obtain tenantAccessToken by tenantKey "+ tenantKey);
        }

        UploadImageRequest req = new UploadImageRequest();
        req.setOriginName(imageName);
        req.setImageFile(new ByteArrayInputStream(bytes));

        String imageKey = openApiClient.uploadImage(tenantAccessToken, req).getData().getImageKey();

        ImageKeyStorage.ImageInfo ii = new ImageKeyStorage.ImageInfo();
        ii.setImageKey(imageKey);
        ii.setMd5(md5);
        ii.setCreateTime(System.currentTimeMillis());
        imageKeyStorage.persistImageInfo(app.getAppId(), app.getAppShortName(), imageName, ii);

        cache.put(imageName, md5);
        return imageKey;
    }
}
