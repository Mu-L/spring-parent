package com.emily.infrastructure.logback.test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.net.SimpleSocketServer;
import org.slf4j.LoggerFactory;

/**
 * @author :  Emily
 * @since :  2023/10/18 1:44 PM
 */
public class ServerAppender {
    public static void main(String[] args) {

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        SimpleSocketServer server = new SimpleSocketServer(lc, 8100);
        server.start();
    }
}
