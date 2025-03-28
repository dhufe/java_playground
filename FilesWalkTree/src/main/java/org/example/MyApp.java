package org.example;

import de.dit.api.dimag.controlfile.ControlfileController;
import de.dit.api.dimag.controlfile.extended.types.VerzeichnungseinheitExtended;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyApp {

    private static final Logger logger = LogManager.getLogger( MyApp.class.getSimpleName() );

    public static void main(String[] args) throws IOException {
        Path startingDir = Path.of(System.getProperty("user.dir") , "testdata");
        logger.info("Starting directory: {}", startingDir);
        PrintFiles pf = new PrintFiles();
        Files.walkFileTree(startingDir, pf);

        VerzeichnungseinheitExtended obj = pf.getVerz();
        ControlfileController ctrl = new ControlfileController();
        System.out.println(ctrl.getAsXml(obj));
    }
}