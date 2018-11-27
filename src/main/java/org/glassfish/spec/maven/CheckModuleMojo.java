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

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.glassfish.spec.Artifact;
import org.glassfish.spec.Metadata;
import org.glassfish.spec.Spec;


/**
 *
 * @goal check-module
 * @phase package
 *
 * @author Romain Grecourt
 */
public class CheckModuleMojo extends AbstractMojo {
    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    
    /**
     * Module to verify
     * 
     * @parameter expression="${module}"
     */
    protected File module;
    
    /**
     * Ignore failures
     * 
     * @parameter expression="${ignoreErrors}"
     */
    protected boolean ignoreErrors;
    
    /**
     * Spec
     * 
     * @parameter expression="${spec}"
     */    
    protected Spec spec;
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if(module ==null || !module.exists()){
            module = project.getArtifact().getFile();
            if(module == null || !module.exists()){
                getLog().error("There is no jar to verify, try using mvn package first.");
                throw new MojoFailureException("no jar to verify");
            }
        }
        try {
            if (spec == null) {
                spec = new Spec();
            }
            spec.setArtifact(new Artifact(
                    project.getGroupId(),
                    project.getArtifactId(),
                    project.getVersion()));
            spec.setMetadata(Metadata.fromJar(new JarFile(module)));
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
                if(!ignoreErrors){
                    throw new MojoFailureException("spec verification failed.");
                }
            }
        } catch (IOException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }
}
