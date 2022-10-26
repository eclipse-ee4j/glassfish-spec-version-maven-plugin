/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation. All rights reserved.
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

package org.glassfish.spec.maven;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.glassfish.spec.Artifact;
import org.glassfish.spec.Metadata;
import org.glassfish.spec.Spec;

/**
 *
 * Check a set of spec artifact in a staging directory.
 * @author Romain Grecourt
 */
@Mojo(name = "check-distribution",
      requiresProject = true,
      defaultPhase = LifecyclePhase.PACKAGE)
public final class CheckDistributionMojo extends AbstractMojo {

    /**
     * Include pattern.
     */
    @Parameter(property = "includes", defaultValue = "javax*.jar")
    private String includes;

    /**
     * Exclude pattern.
     */
    @Parameter(property = "excludes")
    private String excludes;

    /**
     * The directory containing the spec artifacts to process.
     */
    @Parameter(property = "dir", required = true)
    private File dir;

    /**
     * The specification configurations.
     */
    @Parameter(property = "specs", required = true)
    private List<Spec> specs;

    /**
     * Find or create the specification configuration for the given artifact.
     * @param file the artifact file to match
     * @return the spec configuration
     * @throws IOException if an error occurs while reading the JAR file entries
     */
    private Spec getSpec(final File file) throws IOException {
        try (JarFile jar = new JarFile(file)) {
            Artifact a = Artifact.fromJar(jar);
            for (Spec s : specs) {
                if (s.getArtifact().equals(a)) {
                    s.setMetadata(Metadata.fromJar(jar));
                    return s;
                }
            }
            Spec spec = new Spec();
            spec.setArtifact(a);
            spec.setMetadata(Metadata.fromJar(jar));
            return spec;
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!dir.exists()) {
            String msg = String.format(
                    "directory (%s) does not exist",
                    dir.getAbsolutePath());
            getLog().error(msg);
            throw new MojoFailureException(msg);
        }

        List<File> jars = Collections.EMPTY_LIST;
        try {
            jars = FileUtils.getFiles(dir, includes, excludes);
        } catch (IOException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }

        for (File jar : jars) {
            try {
                Spec spec = getSpec(jar);
                spec.verify();

                if (!spec.getErrors().isEmpty()) {
                    System.out.println("");
                    System.out.println(spec.getArtifact().toString());
                    String specDesc = spec.toString();
                    if (!specDesc.isEmpty()) {
                        System.out.println(spec.toString());
                    }
                    for (int i = 0; i < spec.getErrors().size(); i++) {
                        System.out.println(new StringBuilder()
                                .append('-')
                                .append(' ')
                                .append(spec.getErrors().get(i))
                                .toString());
                    }
                    System.out.println("");
                }
            } catch (IOException ex) {
                getLog().warn(ex.getMessage(), ex);
            }
        }
    }
}
