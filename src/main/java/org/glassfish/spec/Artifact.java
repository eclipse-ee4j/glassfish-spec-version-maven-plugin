/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.spec;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/**
 * Represent the API JAR file as a Maven artifact.
 *
 * @author Romain Grecourt
 */
public final class Artifact {

    /**
     * Artifact groupId.
     */
    private String groupId;

    /**
     * Artifact artifactId.
     */
    private String artifactId;

    /**
     * Artifact version.
     */
    private ArtifactVersion version;


    /**
     * The Maven SNAPSHOT qualifier.
     */
    private static final String SNAPSHOT_QUALIFIER = "-SNAPSHOT";

    /**
     * The Milestone qualifier.
     */
    private static final String M_QUALIFIER = "-M";

    /**
     * The Release Candidate qualifier.
     */
    private static final String RC_QUALIFIER = "-RC";

    /**
     * Strip a small set of approved qualifiers from a given version.
     *
     * <p>
     * The Jakarta EE process has defined a few qualifiers that are okay to
     * use in versions. These will be removed from the version as they only
     * serve as an intermediate release placeholder, not something to permanently
     * depend upon.
     *
     * <p>
     * At the moment these are {@code -SNAPSHOT}, {@code -Mx} and {@code -RCx}, where {@code x} is typically
     * a number (e.g. -M1, -RC3, etc).
     *
     * @param version the qualifier to process
     * @return a version without any of the mentioned qualifiers
     */
    public static String stripApprovedQualifier(final String version) {
        if (version == null) {
            return null;
        }

        if (version.endsWith(SNAPSHOT_QUALIFIER)) {
            return version.replace(SNAPSHOT_QUALIFIER, "");
        }

        if (version.contains(M_QUALIFIER)) {
            return version.substring(0, version.indexOf(M_QUALIFIER));
        }

        if (version.contains(RC_QUALIFIER)) {
            return version.substring(0, version.indexOf(RC_QUALIFIER));
        }

        return version;
    }

    /**
     * Create a new {@link Artifact} instance.
     */
    public Artifact() {
    }

    /**
     * Create a new {@link Artifact} instance.
     * @param gId the artifact groupId
     * @param aId the artifact artifactId
     * @param v the artifact version
     */
    public Artifact(final String gId, final String aId, final String v) {
        this.groupId = gId;
        this.artifactId = aId;
        this.version = new DefaultArtifactVersion(v);
    }

    /**
     * Get the artifactId for this artifact.
     * @return the artifactId
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Get the groupId for this artifact.
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Get the version for this artifact.
     * @return the version
     */
    public ArtifactVersion getVersion() {
        return version;
    }

    /**
     * Get the normalized release version for this artifact.
     * @return the version
     */
    public String getAbsoluteVersion() {
        return stripApprovedQualifier(version.toString());
    }

    /**
     * Set the artifactId of this artifact.
     * @param aId the artifactId value to use
     */
    public void setArtifactId(final String aId) {
        this.artifactId = aId;
    }

    /**
     * Set the groupId of this artifact.
     * @param gId the artifactId value to use
     */
    public void setGroupId(final String gId) {
        this.groupId = gId;
    }

    /**
     * Set the version of this artifact.
     * @param v the artifactId value to use
     */
    public void setVersion(final String v) {
        this.version = new DefaultArtifactVersion(v);
    }

    /**
     * Get the {@link ZipEntry} for {@code pom.properties} in the given
     * JAR file.
     * @param jar the jar file to process
     * @return the {@link ZipEntry} if found, {@code null} otherwise
     */
    private static ZipEntry getPomPropertiesFile(final JarFile jar) {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith("pom.properties")) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Create an {@link Artifact} instance from a given JAR file.
     * @param jar the jar file to process
     * @return the create {@link Artifact} instance
     * @throws IOException if an error occurs while reading JAR file entries
     */
    public static Artifact fromJar(final JarFile jar) throws IOException {
        ZipEntry entry = getPomPropertiesFile(jar);
        if (entry == null) {
            throw new RuntimeException(
                    "unable to find pom.properties "
                    + "files inside " + jar.getName());
        }
        InputStream is = jar.getInputStream(entry);
        Properties pomProps = new Properties();
        pomProps.load(is);

        return new Artifact(
                pomProps.getProperty("groupId"),
                pomProps.getProperty("artifactId"),
                pomProps.getProperty("version"));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        sb.append(groupId);
        sb.append(':');
        sb.append(artifactId);
        sb.append(':');
        sb.append(version);
        sb.append(" ]");
        return sb.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Artifact other = (Artifact) obj;
        if (this.groupId == null && other.groupId != null) {
                return false;
        } else if (this.groupId != null
                && !this.groupId.equals(other.groupId)) {
            return false;
        }
        if (this.artifactId == null && other.artifactId != null) {
            return false;
        } else if (this.artifactId != null
                && !this.artifactId.equals(other.artifactId)) {
            return false;
        }
        if (this.version == null && other.version != null) {
            return false;
        } else if (this.version != null
                && !this.version.equals(other.version)) {
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("checkstyle:MagicNumber")
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.groupId != null
                ? this.groupId.hashCode() : 0);
        hash = 71 * hash + (this.artifactId != null
                ? this.artifactId.hashCode() : 0);
        hash = 71 * hash + (this.version != null
                ? this.version.hashCode() : 0);
        return hash;
    }
}
