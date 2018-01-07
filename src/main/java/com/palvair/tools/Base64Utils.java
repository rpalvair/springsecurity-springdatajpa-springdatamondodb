package com.palvair.tools;

import javax.xml.bind.DatatypeConverter;

public final class Base64Utils {


    public static String toBase64(byte[] content) {
        return DatatypeConverter.printBase64Binary(content);
    }

    public static byte[] fromBase64(String content) {
        return DatatypeConverter.parseBase64Binary(content);
    }

}
