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

package org.glassfish.spec.test.sets;

import org.glassfish.spec.Artifact;
import org.glassfish.spec.test.TestSpec;

/**
 *
 * @author Romain Grecourt
 */
public class Courgette extends TestSpec {

    public Courgette() {
        super(new Artifact(
                "${courgette.groupId}",
                "${courgette.artifactId}",
                "${courgette.mavenVersion}"),
                "${courgette.specVersion}",
                "${courgette.newSpecVersion}",
                "${courgette.specImplVersion}",
                "${courgette.implVersion}",
                "${courgette.newImplVersion}",
                "${courgette.specBuild}",
                "${courgette.implBuild}",
                "${courgette.apiPackage}",
                "${courgette.implNamespace}",
                "${courgette.jarType}",
                "${courgette.specMode}",
                Boolean.parseBoolean("${courgette.nonFinal}"));
    }

    @Override
    public String getExpectedBundleVersion() {
        return "${courgette.bundleVersion}";
    }

    @Override
    public String getExpectedBundleSpecVersion() {
        return "${courgette.bundleSpecVersion}";
    }

    @Override
    public String getExpectedBundleSymbolicName() {
        return "${courgette.bundleSymbolicName}";
    }

    @Override
    public String getExpectedJarExtensionName() {
        return "${courgette.jarExtensionName}";
    }

    @Override
    public String getExpectedJarImplementationVersion() {
        return "${courgette.jarImplementationVersion}";
    }

    @Override
    public String getExpectedJarSpecificationVersion() {
        return "${courgette.jarSpecificationVersion}";
    }

    @Override
    public String getJarPath() {
       return "target/it/modules/${courgette.artifactId}/target/${courgette.artifactId}.jar";
    }
}
