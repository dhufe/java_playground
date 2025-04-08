package org.example.model;

import de.dit.api.dimag.controlfile.VerzObjExtended;

import java.nio.file.Path;
import java.util.Optional;

public class ObjectFactory {

    public static VerzObjExtended createVerzObjExtended(ObjectType objectType, Optional<Path> path) {
        VerzObjExtended _obj = new VerzObjExtended();
        if (path.isPresent()) {
            Path p = path.get();
            _obj.setFilepath(p.toAbsolutePath().getParent().toString());
            _obj.setDateiname(p.getFileName().toString());
        }

        switch (objectType) {
            case FileObject:
                _obj.setTyp("F");
                break;
            case RepresentationObject:
                _obj.setTyp("R");
                break;
            case StructObject:
                _obj.setTyp("S");
                break;
            case InformationObject:
                _obj.setTyp("IO");
                break;
            case MetaDataObject:
                _obj.setTyp("X");
                break;
            case IngestListObject:
                _obj.setTyp("I");
                break;
            case ProtocolObject:
                _obj.setTyp("P");
                break;
            case DocumentObject:
                _obj.setTyp("D");
                break;
            default:
                break;
        }
        return _obj;
    }
}
