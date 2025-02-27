package org.example;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadPropertiesTest {
    @Test

    public void testLoadProperties() throws IOException {
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String appConfigPath = rootPath + "parameter.properties";

        System.out.println();

        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));

        List<String> parameterList = Arrays.asList(appProps.getProperty("Parameter").split(","));

        parameterList.forEach(
                parameter -> System.out.println(MessageFormat.format("{0} :=> {1}", parameter, appProps.getProperty(parameter, "")))
        );
        assertEquals("Abgebende Stelle", parameterList.getFirst());
    }
}
