package org.example;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import de.dit.api.dimag.controlfile.VerzObjExtended;
import de.dit.api.dimag.controlfile.Verzeichnungseinheit;
import de.dit.api.dimag.controlfile.extended.types.VerzeichnungseinheitExtended;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PrintFiles extends SimpleFileVisitor<Path> {

    private static final Logger logger = LogManager.getLogger(PrintFiles.class.getSimpleName());

    public VerzeichnungseinheitExtended getVerz() {
        return verz;
    }

    private VerzeichnungseinheitExtended verz = new VerzeichnungseinheitExtended();
    private VerzObjExtended child = null;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.getFileName().toString().equals(".DS_Store")) {
            logger.debug("Ignoring file: {} ", file);
        } else if (attrs.isSymbolicLink()) {
            logger.debug("Symbolic link: {} ", file);
        } else if (attrs.isRegularFile()) {
            VerzObjExtended obj = new VerzObjExtended();
            obj.setFile(file);
            obj.setFilepath(file.toString());
            obj.setSftpDateiname(file.getFileName().toString());
            if (this.child != null) {
                child.getVerzObj().add(obj);
            } else {
                verz.getVerzObj().add(obj);
            }
            logger.debug("Regular file: {} ", file);
        } else {
            logger.debug("Unrecognized file: {} ", file);
        }

        logger.debug("{} {} bytes", file.toAbsolutePath(), attrs.size());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        if ( child == null ) {
            child = new VerzObjExtended();
        }
        logger.debug("Pre visit directory: {} ", dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        if ( child != null ) {
            verz.getVerzObj().add(this.child);
            this.child = null;
        }

        logger.debug("Post visit directory: {} ", dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        logger.error(exc.getMessage());
        return FileVisitResult.CONTINUE;
    }
}
