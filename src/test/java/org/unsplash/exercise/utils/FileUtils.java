package org.unsplash.exercise.utils;

import org.unsplash.exercise.LoggingManager;

import java.io.File;

public class FileUtils {

    private static final Class<?> LOGGER = FileUtils.class;

    private static final int WAIT_TIME = 60;

    public static boolean isFileDownloaded(String downloadPath, String photoId) {

        WaitUtils.sleep(LOGGER, 2);

        for (int iterationTime = 0; iterationTime <= WAIT_TIME; iterationTime++) {

            File dir = new File(downloadPath);
            File[] dirContents = dir.listFiles();
            if (dirContents != null) {
                for (File dirContent : dirContents) {
                    if (dirContent.getName().contains(photoId)) {
                        return true;
                    }
                }
            } else {
                WaitUtils.sleep(LOGGER, 3);
            }
        }
        LoggingManager.logInfo(LOGGER, "File was not found in: " + photoId);
        return false;
    }
}
