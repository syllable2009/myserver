package com;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 2019-04-16 18:29
 **/
public class ImageMimeTypeHelper {

    // 可接受的头像图片文件
    private static List<String> PHOTO_TYPE;
    private static List<String> PHOTO_MIME;

    // 可接受的预览文件id
    private static List<String> previewTypes;
    private static Map<String, String> mimeTypes = new HashMap<>();

    static {
        PHOTO_MIME = Arrays.asList(ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_BMP.getMimeType(), ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType(), ContentType.IMAGE_SVG.getMimeType());
        PHOTO_TYPE = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "svg");
        previewTypes = Arrays.asList("pdf", "jpeg", "jpe", "jpg", "png", "gif", "svg", "bmp");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("jpe", "image/jpeg");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("svg", "image/svg+xml");
        mimeTypes.put("bmp", "image/bmp");
        mimeTypes.put("pdf", "application/pdf");
    }

    public static boolean isPhotoExtension(String fileName) {
        String ext = FilenameUtils.getExtension(fileName);
        if (StringUtils.isNotEmpty(ext)) return PHOTO_TYPE.contains(ext);
        return false;
    }

    public static boolean isPhotoMime(String contentType) {
        if (StringUtils.isNotEmpty(contentType)) return PHOTO_MIME.contains(contentType);
        return false;
    }

    public static String getMimeTypeByFileName(String fileName) {
        String ext = FilenameUtils.getExtension(fileName);
        if (ext == null) {
            return null;
        }
        return mimeTypes.get(ext);
    }

    public static boolean isFileSupportPreview(String fileName) {
        String ext = FilenameUtils.getExtension(fileName);
        if (ext == null) {
            return false;
        }
        return previewTypes.contains(ext);
    }

    public static String getMimeTypeByExtension(String extension) {
        return mimeTypes.get(extension);
    }
}
