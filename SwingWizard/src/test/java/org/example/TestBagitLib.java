package org.example;

import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import gov.loc.repository.bagit.hash.SupportedAlgorithm;
import org.example.services.impl.SimpleBagit;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestBagitLib {

    @Test
    public void testBagitImplWithoutMeta() throws NoSuchAlgorithmException, IOException {
        Collection<SupportedAlgorithm> hash_alg = Collections.singletonList(StandardSupportedAlgorithms.MD5);
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        Path srcFolder = Paths.get(rootPath, "test_data");
        System.out.println(srcFolder.toAbsolutePath());
        Bag testbag = SimpleBagit.createBag(srcFolder, hash_alg);
        assertNotNull (testbag);
    }
}
