package org.example;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import de.dit.api.dimag.controlfile.VerzObjExtended;
import de.dit.api.dimag.controlfile.extended.types.VerzeichnungseinheitExtended;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.ObjectFactory;
import org.example.model.ObjectType;

@Getter
public class PrintFiles extends SimpleFileVisitor<Path> {

    private static final Logger logger = LogManager.getLogger(PrintFiles.class.getSimpleName());

    private final VerzObjExtended verz;

    PrintFiles() {
        this.verz = ObjectFactory.createVerzObjExtended(ObjectType.RepresentationObject, Optional.empty());
    }


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (attrs.isSymbolicLink()) {
            logger.debug("Symbolic link: {} ", file);
        } else if (attrs.isRegularFile()) {
            logger.debug("Regular file: {} ", file);
        } else {
            logger.debug("Unrecognized file: {} ", file);
        }

        if ((!VerzeichnungseinheitExtended.ignoredFileNames.contains(file.getFileName().toString())) &&
                (!file.getFileName().toString().equals(".DS_Store"))) {
            logger.debug("Creating new object using ObjectFactory.");
            VerzObjExtended obj = ObjectFactory.createVerzObjExtended(ObjectType.FileObject, Optional.of(file));

            LocalDateTime localDateTime = attrs.lastModifiedTime()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            logger.debug("Setting modification timestamp.");
            obj.setAenderungsDatum(localDateTime.format(formatter));
            verz.getVerzObj().add(obj);
        }
        logger.debug("{} {} bytes", file.toAbsolutePath(), attrs.size());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        logger.debug("Pre visit directory: {} ", dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        logger.debug("Post visit directory: {} ", dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        logger.error(exc.getMessage());
        return FileVisitResult.CONTINUE;
    }
}
