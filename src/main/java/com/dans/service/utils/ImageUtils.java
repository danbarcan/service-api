package com.dans.service.utils;

import com.dans.service.messaging.Publisher;
import com.dans.service.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageUtils {
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    private ImageUtils() {
    }

    public static byte[] base64StringToByteArray(String base64) {
        byte[] imageByte = null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            imageByte = decoder.decodeBuffer(base64);
        } catch (IOException e) {
            log.error("Error decoding image from Base64: ", e);
        }
        return imageByte;
    }
}
