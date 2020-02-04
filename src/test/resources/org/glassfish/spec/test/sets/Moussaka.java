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
public class Moussaka extends TestSpec {

    public Moussaka() {
        super(new Artifact(
                "${moussaka.groupId}",
                "${moussaka.artifactId}",
                "${moussaka.mavenVersion}"),
                "${moussaka.specVersion}",
                "${moussaka.newSpecVersion}",
                "${moussaka.specImplVersion}",
                "${moussaka.implVersion}",
                "${moussaka.newImplVersion}",
                "${moussaka.specBuild}",
                "${moussaka.implBuild}",
                "${moussaka.apiPackage}",
                "${moussaka.implNamespace}",
                "${moussaka.jarType}",
                "${moussaka.specMode}",
                Boolean.parseBoolean("${moussaka.nonFinal}"));
    }

    @Override
    public String getExpectedBundleVersion() {
        return "${moussaka.bundleVersion}";
    }

    @Override
    public String getExpectedBundleSpecVersion() {
        return "${moussaka.bundleSpecVersion}";
    }

    @Override
    public String getExpectedBundleSymbolicName() {
        return "${moussaka.bundleSymbolicName}";
    }

    @Override
    public String getExpectedJarExtensionName() {
        return "${moussaka.jarExtensionName}";
    }

    @Override
    public String getExpectedJarImplementationVersion() {
        return "${moussaka.jarImplementationVersion}";
    }

    @Override
    public String getExpectedJarSpecificationVersion() {
        return "${moussaka.jarSpecificationVersion}";
    }

    @Override
    public String getJarPath() {
       return "target/its/modules/${moussaka.artifactId}/target/${moussaka.artifactId}.jar";
    } 
}
