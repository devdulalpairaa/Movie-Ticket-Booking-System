package com.movieticket.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Very small console logging utility. Every major operation in the
 * application routes through here so that actions are always visible in the
 * console, in addition to the Swing GUI.
 */
public final class Logger {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Logger() {
    }

    public static void info(String message) {
        System.out.println("[" + timestamp() + "] [INFO] " + message);
    }

    public static void warn(String message) {
        System.out.println("[" + timestamp() + "] [WARN] " + message);
    }

    public static void error(String message) {
        System.out.println("[" + timestamp() + "] [ERROR] " + message);
    }

    public static void action(String message) {
        System.out.println("[" + timestamp() + "] >> " + message);
    }

    private static String timestamp() {
        return LocalDateTime.now().format(TS);
    }
}
