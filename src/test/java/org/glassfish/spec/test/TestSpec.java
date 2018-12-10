/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.spec.test;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import org.glassfish.spec.Artifact;
import org.glassfish.spec.Metadata;
import org.glassfish.spec.Spec;
import org.junit.Assert;


/**
 *
 * @author Romain Grecourt
 */
public abstract class TestSpec extends Spec {

    public TestSpec(
            Artifact artifact,
            String specVersion,
            String newSpecVersion,
            String specImplVersion,
            String implVersion,
            String newImplVersion,
            String specBuild,
            String implBuild,
            String apiPackage,
            String implNamespace,
            String jarType,
            boolean nonFinal) {
        super();
        setArtifact(artifact);
        setSpecVersion(specVersion);
        setNewSpecVersion(newSpecVersion);
        setSpecImplVersion(specImplVersion);
        setImplVersion(implVersion);
        setNewImplVersion(newImplVersion);
        setSpecBuild(specBuild);
        setImplBuild(implBuild);
        setApiPackage(apiPackage);
        setImplNamespace(implNamespace);
        setJarType(jarType);
        setNonFinal(nonFinal);
    }

    public abstract String getExpectedBundleVersion();
    public abstract String getExpectedBundleSpecVersion();
    public abstract String getExpectedBundleSymbolicName();
    public abstract String getExpectedJarExtensionName();
    public abstract String getExpectedJarImplementationVersion();
    public abstract String getExpectedJarSpecificationVersion();
    public abstract String getJarPath();

    public Spec getSpec(){
        return this;
    }

    private static void positive(String key, String expected, String value){
        String msg = "Testing "+key;
        Assert.assertNotNull(msg,value);
        Assert.assertEquals(msg,expected,value);
    }

    public void assertMetadata(){
        Assert.assertNotNull(getMetadata());
        positive(
                Metadata.BUNDLE_SYMBOLIC_NAME,
                getExpectedBundleSymbolicName(),
                getMetadata().getBundleSymbolicName());
//        positive(
//                Metadata.BUNDLE_SPEC_VERSION,
//                getExpectedBundleSpecVersion(),
//                getMetadata().getBundleSpecVersion());
        positive(
                Metadata.BUNDLE_VERSION,
                getExpectedBundleVersion(),
                getMetadata().getBundleVersion());
        positive(
                Metadata.JAR_EXTENSION_NAME,
                getExpectedJarExtensionName(),
                getMetadata().getJarExtensionName());
        positive(
                Metadata.JAR_SPECIFICATION_VERSION,
                getExpectedJarSpecificationVersion(),
                getMetadata().getJarSpecificationVersion());
        positive(
                Metadata.JAR_IMPLEMENTATION_VERSION,
                getExpectedJarImplementationVersion(),
                getMetadata().getjarImplementationVersion());
    }

    public void assertMetadataFromJar() {
        try {
            File f = new File(getJarPath());
            Assert.assertTrue(
                    "test that " 
                    + f.getCanonicalPath() 
                    + " exists",
                    f.exists());
            read(new JarFile(f));
            assertMetadata();
        } catch (IOException ioe) {
            Assert.fail(ioe.getMessage());
        }
    }
}
