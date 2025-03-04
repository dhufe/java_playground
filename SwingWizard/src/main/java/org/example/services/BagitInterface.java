package org.example.services;

import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.hash.SupportedAlgorithm;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public interface BagitInterface {

    Bag readBag();

    Bag rebagFolder(Path folder);

    Bag createBag(Path srcFolder) throws NoSuchAlgorithmException, IOException;

    // Bag createBag(Path srcFolder, Path dstFolder, Collection<SupportedAlgorithm> algorithms) throws NoSuchAlgorithmException, IOException;
}
