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
public class Ratatouille extends TestSpec {
    public Ratatouille() {
        super(new Artifact(
                "${ratatouille.groupId}",
                "${ratatouille.artifactId}",
                "${ratatouille.mavenVersion}"),
                "${ratatouille.specVersion}",
                "${ratatouille.newSpecVersion}",
                "${ratatouille.specImplVersion}",
                "${ratatouille.implVersion}",
                "${ratatouille.newImplVersion}",
                "${ratatouille.specBuild}",
                "${ratatouille.implBuild}",
                "${ratatouille.apiPackage}",
                "${ratatouille.implNamespace}",
                "${ratatouille.jarType}",
                "${ratatouille.specMode}",
                Boolean.parseBoolean("${ratatouille.nonFinal}"));
    }

    @Override
    public String getExpectedBundleVersion() {
        return "${ratatouille.bundleVersion}";
    }

    @Override
    public String getExpectedBundleSpecVersion() {
        return "${ratatouille.bundleSpecVersion}";
    }

    @Override
    public String getExpectedBundleSymbolicName() {
        return "${ratatouille.bundleSymbolicName}";
    }

    @Override
    public String getExpectedJarExtensionName() {
        return "${ratatouille.jarExtensionName}";
    }

    @Override
    public String getExpectedJarImplementationVersion() {
        return "${ratatouille.jarImplementationVersion}";
    }
    
    @Override
    public String getExpectedJarSpecificationVersion() {
        return "${ratatouille.jarSpecificationVersion}";
    }

    @Override
    public String getJarPath() {
       return "target/its/modules/${ratatouille.artifactId}/target/${ratatouille.artifactId}.jar";
    }
}
