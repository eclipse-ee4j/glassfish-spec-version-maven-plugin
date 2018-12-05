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

package org.glassfish.spec.maven;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.glassfish.spec.Artifact;
import org.glassfish.spec.Spec;

/**
 * Generate spec properties.
 * @author Romain Grecourt
 */
@Mojo(name = "set-spec-properties",
      requiresProject = true,
      defaultPhase = LifecyclePhase.VALIDATE)
public final class SetPropertiesMojo extends AbstractMojo {

    /**
     * The maven project.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    /**
     * The spec.
     */
    @Parameter(property = "spec", required = true)
    private Spec spec;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        spec.setArtifact(new Artifact(
                project.getGroupId(),
                project.getArtifactId(),
                project.getVersion()));

        Properties specProps = spec.getMetadata().getProperties();

        getLog().info("");
        getLog().info("-- spec properties --");
        Iterator<Entry<Object, Object>> it = specProps.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Object, Object> e =  it.next();
            getLog().info(e.getKey() + " = " + e.getValue());
        }
        getLog().info("");

        project.getProperties().putAll(specProps);
    }
}
