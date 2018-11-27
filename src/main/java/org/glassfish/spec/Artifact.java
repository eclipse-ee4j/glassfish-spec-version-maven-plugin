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
 *
 * @author Romain Grecourt
 */
public final class Artifact {

    private String groupId;
    private String artifactId;
    private ArtifactVersion version;
    private String buildNumber;
    
    private static final String[] buildNumberSeparators = new String[] {"m", "b"};
    public static final String SNAPSHOT_QUALIFIER = "SNAPSHOT";
    
    public static String stripSnapshotQualifier(String qualifier) {
        if (qualifier != null) {
            if (qualifier.endsWith("-" + SNAPSHOT_QUALIFIER)) {
                qualifier = qualifier.replace("-" + SNAPSHOT_QUALIFIER, "");
            }
            return qualifier;
        }
        return null;
    }
    
    private static String getBuildNumber(String qualifier) {
        qualifier = stripSnapshotQualifier(qualifier);
        if (qualifier != null) {
            for (String c : buildNumberSeparators) {
                if (qualifier.contains(c)) {
                    return qualifier.substring(qualifier.lastIndexOf(c) + 1);
                }
            }
        }
        return null;
    }
    
    public Artifact(){
    }

    public Artifact(String _groupId, String _artifactId, String _version) {
        this.groupId = _groupId;
        this.artifactId = _artifactId;
        this.version = new DefaultArtifactVersion(_version);
        this.buildNumber = getBuildNumber(version.getQualifier());
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public ArtifactVersion getVersion() {
        return version;
    }
    
    public String getAbsoluteVersion(){
        return stripSnapshotQualifier(version.toString());
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setVersion(String version) {
        this.version = new DefaultArtifactVersion(version);
        this.buildNumber = getBuildNumber(this.version.getQualifier());
    }
    
    public String getBuildNumber() {
        return buildNumber;
    }
    
    private static ZipEntry getPomPropertiesFile(JarFile jar){
        Enumeration<JarEntry> entries = jar.entries();
        while(entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
            if(entry.getName().endsWith("pom.properties")){
                return entry;
            }
        }
        return null;
    }
    
    public static Artifact fromJar(JarFile jar) throws IOException {
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Artifact other = (Artifact) obj;
        if ((this.groupId == null) ? (other.groupId != null) : !this.groupId.equals(other.groupId)) {
            return false;
        }
        if ((this.artifactId == null) ? (other.artifactId != null) : !this.artifactId.equals(other.artifactId)) {
            return false;
        }
        if (this.version != other.version && (this.version == null || !this.version.equals(other.version))) {
            return false;
        }
        return true;
    }
}
