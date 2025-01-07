package org.mots.utils;

import java.io.File;


import java.io.File;

public class PathUtil {
    private static String projectDir;

    static {
        try {
            // Получаем путь к JAR-файлу
            String jarPath = new File(PathUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
            // Получаем родительскую директорию (директорию проекта)
            projectDir = new File(jarPath).getParentFile().getParent(); // Поднимаемся на два уровня
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProjectDir() {
        //return projectDir;
        return  "C:\\Projects\\Java\\LearnWords\\MesMots";
    }
}

