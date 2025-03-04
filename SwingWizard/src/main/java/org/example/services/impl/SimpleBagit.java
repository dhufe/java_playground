package org.example.services.impl;

import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import gov.loc.repository.bagit.hash.SupportedAlgorithm;

import org.example.services.BagitInterface;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;

public class SimpleBagit implements BagitInterface {

    private final Collection<SupportedAlgorithm> algorithms;

    public SimpleBagit(Collection<SupportedAlgorithm> algorithms) {
        this.algorithms = algorithms;
    }

    public SimpleBagit() {
        this.algorithms = Collections.singletonList(StandardSupportedAlgorithms.SHA256);
    }

    @Override
    public Bag readBag() {
        return null;
    }

    @Override
    public Bag rebagFolder(Path folder) {
        return null;
    }

    @Override
    public Bag createBag(Path srcFolder) throws NoSuchAlgorithmException, IOException {
        return BagCreator.bagInPlace(srcFolder, this.algorithms, false);
    }

    public static Bag createBag(Path srcFolder, Collection<SupportedAlgorithm> algorithms) throws NoSuchAlgorithmException, IOException {
        return BagCreator.bagInPlace(srcFolder, algorithms, false);
    }

}
