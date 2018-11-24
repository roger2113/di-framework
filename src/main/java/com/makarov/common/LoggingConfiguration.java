package com.makarov.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LoggingConfiguration {

    static {
        InputStream stream = LoggingConfiguration.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
