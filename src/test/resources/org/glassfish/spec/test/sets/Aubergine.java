/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.spec.test.sets;

import org.glassfish.spec.Artifact;
import org.glassfish.spec.test.TestSpec;

/**
 *
 * @author Romain Grecourt
 */
public class Aubergine extends TestSpec {

    public Aubergine() {
        super(new Artifact(
                "${aubergine.groupId}",
                "${aubergine.artifactId}",
                "${aubergine.mavenVersion}"),
                "${aubergine.specVersion}",
                "${aubergine.newSpecVersion}",
                "${aubergine.specImplVersion}",
                "${aubergine.implVersion}",
                "${aubergine.newImplVersion}",
                "${aubergine.specBuild}",
                "${aubergine.implBuild}",
                "${aubergine.apiPackage}",
                "${aubergine.implNamespace}",
                "${aubergine.jarType}",
                "${aubergine.specMode}",
                Boolean.parseBoolean("${aubergine.nonFinal}"));
    }

    @Override
    public String getExpectedBundleVersion() {
        return "${aubergine.bundleVersion}";
    }

    @Override
    public String getExpectedBundleSpecVersion() {
        return "${aubergine.bundleSpecVersion}";
    }

    @Override
    public String getExpectedBundleSymbolicName() {
        return "${aubergine.bundleSymbolicName}";
    }

    @Override
    public String getExpectedJarExtensionName() {
        return "${aubergine.jarExtensionName}";
    }

    @Override
    public String getExpectedJarImplementationVersion() {
        return "${aubergine.jarImplementationVersion}";
    }

    @Override
    public String getExpectedJarSpecificationVersion() {
        return "${aubergine.jarSpecificationVersion}";
    }

    @Override
    public String getJarPath() {
       return "target/its/modules/${aubergine.artifactId}/target/${aubergine.artifactId}.jar";
    }
}
