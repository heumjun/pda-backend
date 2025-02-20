package com.denso.pdabackend.utils;

/**
 * 파일 관련 util class
 */
public final class DensoFileUtils {

    /**
     * 이미지 파일 여부
     * @param contentType
     * @return
     */
    public static boolean isImage(String contentType){
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"));
    }

    /**
     * 확장자가 이미지 파일 여부
     * @param fileName
     * @return
     */
    public static boolean hasValidImageExtension(String fileName){
        if(fileName==null) return false;
        String lowerCaseFile = fileName.toLowerCase();

        return lowerCaseFile.endsWith(".jpg") || lowerCaseFile.endsWith(".jpeg") ||
            lowerCaseFile.endsWith(".png") || lowerCaseFile.endsWith(".gif");
    }
}
