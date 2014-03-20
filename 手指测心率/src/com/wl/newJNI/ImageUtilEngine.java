/**
 * Name        : ImageUtilEngine.java
 * Copyright   : Copyright (c) Tencent Inc. All rights reserved.
 * Description : TODO
 */

package com.wl.newJNI;

/**
 * @author wulei
 */
public class ImageUtilEngine {

    static {
        System.loadLibrary("ImageUtilEngine");
    }

    public native int[] decodeYUV420SP(byte[] buf, int width, int heigth);
}
