package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyApp {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        Path startingDir = Path.of(System.getProperty("user.dir"));
        PrintFiles pf = new PrintFiles();
        Files.walkFileTree(startingDir, pf);
    }
}