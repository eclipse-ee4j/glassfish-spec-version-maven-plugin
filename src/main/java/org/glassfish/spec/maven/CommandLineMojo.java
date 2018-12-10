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

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.jar.JarFile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.glassfish.spec.Artifact;
import org.glassfish.spec.Spec;
import org.glassfish.spec.Spec.JarType;

/**
 * Maven Goal to run spec verifications from the command line.
 *
 * @author Romain Grecourt
 */
@Mojo(name = "cli",
      requiresProject = true,
      defaultPhase = LifecyclePhase.VALIDATE)
public final class CommandLineMojo extends AbstractMojo {

    /**
     * Is it a final specification?.
     */
    @Parameter(property = "isFinal", defaultValue = "false")
    private boolean isFinal;

    /**
     * Is it an API jar?.
     */
    @Parameter(property = "isApi", defaultValue = "true")
    private String jarType;

    /**
     * Path to the API jar file.
     */
    @Parameter(property = "apijar")
    private String apiJar;

    /**
     * Path to the Impl jar file.
     */
    @Parameter(property = "impljar")
    private String implJar;

    /**
     * Implementation namespace.
     */
    @Parameter(property = "implnamespace")
    private String implNamespace;

    /**
     * Mode. Allowed values are "javaee", "jakarta"
     */
    @Parameter(property = "specMode", defaultValue = "jakarta")
    private String specMode;

    /**
     * API package.
     */
    @Parameter(property = "apipackage")
    private String apiPackage;

    /**
     * Spec version.
     */
    @Parameter(property = "specversion")
    private String specVersion;

    /**
     * Spec implementation version.
     */
    @Parameter(property = "specimplversion")
    private String specImplVersion;

    /**
     * Implementation version.
     */
    @Parameter(property = "implversion")
    private String implVersion;

    /**
     * New implementation version.
     */
    @Parameter(property = "newimplversion")
    private String newImplVersion;

    /**
     * New spec version.
     */
    @Parameter(property = "newspecversion")
    private String newSpecVersion;

    /**
     * Spec build.
     */
    @Parameter(property = "specbuild")
    private String specBuild;

    /**
     * Implementation build.
     */
    @Parameter(property = "implbuild")
    private String implBuild;

    /**
     * Property file.
     */
    @Parameter(property = "properties")
    private File properties;

    /**
     * Show the usage.
     */
    @Parameter(property = "help")
    private Boolean help;

    /**
     * The system console.
     */
    private static Console cons;

    /**
     * Prompt with the string and return the user's input.
     * @param msg the prompt message
     * @return the user input
     */
    private static String prompt(final String msg) {
        if (cons == null) {
            return null;
        }
        String s = cons.readLine("%s: ", msg);
        if (s == null || s.length() == 0) {
            return null;
        }
        return s;
    }

    /**
     * Print error and exit.
     * @param msg the error message
     */
    private static void fail(final String msg) {
        System.err.println("ERROR: " + msg);
        System.exit(1);
    }

    /**
     * Print a given parameter to the standard output.
     * @param arg the parameter
     * @param desc the description
     */
    private static void printParam(final String arg, final String desc) {
        StringBuilder sb = new StringBuilder("\t-D");
        System.out.println(sb.append(arg).append(' ').append(desc).toString());
    }

    @Override
    @SuppressWarnings({
        "checkstyle:LineLength",
        "checkstyle:MethodLength"
    })
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (help) {
            printParam("properties", "file\tread settings from property file");
            printParam("nonfinal", "\t\tnon-final specification");
            printParam("standalone", "\t\tAPI has a standalone implementation");
            printParam("apijar", "api.jar\tAPI jar file");
            printParam("impljar", "impl.jar\timplementation jar file");
            printParam("apipackage", "package\tAPI package");
            printParam("implpackage", "package\timplementation package");
            printParam("specversion", "version\tversion number of the JCP specification");
            printParam("specimplversion", "vers\tversion number of the API classes");
            printParam("implversion", "version\tversion number of the implementation");
            printParam("newspecversion", "vers\tversion number of the spec under development");
            printParam("specbuild", "num\tbuild number of spec API jar file");
            printParam("newimplversion", "vers\tversion number of the implementation when final");
            printParam("implbuild", "num\tbuild number of implementation jar file");
            printParam("specMode", "specMode\t'javaee' or 'jakarta'");
            return;
        }

        Artifact artifact = null;

        if (properties != null) {
            FileInputStream fis = null;
            try {
                Properties p = new Properties();
                fis = new FileInputStream(properties);
                p.load(fis);
                fis.close();
                specMode = p.getProperty("SPEC_MODE", specMode);
                apiPackage = p.getProperty("API_PACKAGE", apiPackage);
                implNamespace = p.getProperty("IMPL_NAMESPACE", implNamespace);
                jarType = p.getProperty("JAR_TYPE", jarType);
                if (jarType.equals(JarType.impl.toString())) {
                    implVersion = p.getProperty("SPEC_IMPL_VERSION", implVersion);
                    specBuild = p.getProperty("SPEC_BUILD", specBuild);
                    newSpecVersion = p.getProperty("NEW_SPEC_VERSION", newSpecVersion);
                    apiJar = p.getProperty("API_JAR", apiJar);
                    artifact = Artifact.fromJar(new JarFile(apiJar));
                } else {
                    implVersion = p.getProperty("IMPL_VERSION", implVersion);
                    implBuild = p.getProperty("IMPL_BUILD", implBuild);
                    newImplVersion = p.getProperty("NEW_IMPL_VERSION", newImplVersion);
                    implJar = p.getProperty("IMPL_JAR", implJar);
                    artifact = Artifact.fromJar(new JarFile(implJar));
                }
                specVersion = p.getProperty("SPEC_VERSION", specVersion);
                // really, any of the above 4
                isFinal = newSpecVersion == null;
            } catch (FileNotFoundException ex) {
                throw new MojoExecutionException(ex.getMessage(), ex);
            } catch (IOException ex) {
                throw new MojoExecutionException(ex.getMessage(), ex);
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                    getLog().warn(ex.getMessage());
                }
            }
        }

        if (jarType.equals(JarType.impl.toString())) {
            if (implJar != null) {
                fail("--impljar must not be specified if no standalone implementation");
            }
            if (implNamespace != null) {
                fail("--implpackage must not be specified if no standalone implementation");
            }
            if (implVersion != null) {
                fail("--implversion must not be specified if no standalone implementation");
            }
            if (newImplVersion != null) {
                fail("--newimplversion must not be specified if no standalone implementation");
            }
        }

        if (isFinal) {
            if (newSpecVersion != null) {
                fail("--newspecversion must not be specified for final specification");
            }
            if (specBuild != null) {
                fail("--specbuild must not be specified for final specification");
            }
            if (newImplVersion != null) {
                fail("--newimplversion must not be specified for final specification");
            }
            if (implBuild != null) {
                fail("--implbuild must not be specified for final specification");
            }
        }

        // if no options, prompt for everything
        if (properties == null
                && apiJar == null
                && implJar == null
                && implNamespace == null
                && apiPackage == null
                && specVersion == null
                && specImplVersion == null
                && implVersion == null
                && newImplVersion == null
                && newSpecVersion == null
                && specBuild == null
                && implBuild == null) {

            cons = System.console();
            String s;
            s = prompt("Is this a non-final specification?");
            isFinal = !(s.charAt(0) == 'y');
            s = prompt("Is there a standalone implementation of this specification?");
            if (!(s.charAt(0) == 'y')) {
                jarType = JarType.impl.name();
            } else {
                jarType = JarType.api.name();
            }

            specMode = prompt("Enter the spec mode ('javaee' or 'jakarta')");
            apiPackage = prompt("Enter the main API package (e.g., javax.wombat)");
            specVersion = prompt("Enter the version number of the JCP specification");

            if (jarType.equals(JarType.impl.toString())) {
                specImplVersion = prompt("Enter the version number of the API jar file");
                newSpecVersion = prompt("Enter the version number of the implementation that will be used when the implementation is final");
                if (!isFinal) {
                    specBuild = prompt("Enter the build number of the implementation jar file");
                }
                artifact = new Artifact(apiPackage, apiPackage + Spec.API_SUFFIX, newSpecVersion);
            } else {
                implNamespace = prompt("Enter the main implementation package (e.g., com.sun.wombat)");
                if (!isFinal) {
                    implBuild = prompt("Enter the build number of the implementation jar file");
                }
                newImplVersion = prompt("Enter the version number of the Impl jar file");
                artifact = new Artifact(implNamespace, apiPackage, newImplVersion);
            }
        }

        // TODO remove mojo parameters and replace with spec.
        Spec spec = new Spec();
        spec.setSpecMode(specMode);
        spec.setArtifact(artifact);
        spec.setSpecVersion(specVersion);
        spec.setNewSpecVersion(newSpecVersion);
        spec.setSpecImplVersion(specImplVersion);
        spec.setImplVersion(implVersion);
        spec.setSpecBuild(specBuild);
        spec.setImplBuild(implBuild);
        spec.setApiPackage(apiPackage);
        spec.setImplNamespace(implNamespace);
        spec.setJarType(jarType);
        spec.setNonFinal(!isFinal);
        spec.verify();

        for (String error : spec.getErrors()) {
            System.out.println(error);
        }
    }
}
