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
public class Zucchini extends TestSpec {

    public Zucchini() {
        super(new Artifact(
                "${zucchini.groupId}",
                "${zucchini.artifactId}",
                "${zucchini.mavenVersion}"),
                "${zucchini.specVersion}",
                "${zucchini.newSpecVersion}",
                "${zucchini.specImplVersion}",
                "${zucchini.implVersion}",
                "${zucchini.newImplVersion}",
                "${zucchini.specBuild}",
                "${zucchini.implBuild}",
                "${zucchini.apiPackage}",
                "${zucchini.implNamespace}",
                "${zucchini.jarType}",
                "${zucchini.specMode}",
                Boolean.parseBoolean("${zucchini.nonFinal}"));
    }

    @Override
    public String getExpectedBundleVersion() {
        return "${zucchini.bundleVersion}";
    }

    @Override
    public String getExpectedBundleSpecVersion() {
        return "${zucchini.bundleSpecVersion}";
    }

    @Override
    public String getExpectedBundleSymbolicName() {
        return "${zucchini.bundleSymbolicName}";
    }

    @Override
    public String getExpectedJarExtensionName() {
        return "${zucchini.jarExtensionName}";
    }

    @Override
    public String getExpectedJarImplementationVersion() {
        return "${zucchini.jarImplementationVersion}";
    }

    @Override
    public String getExpectedJarSpecificationVersion() {
        return "${zucchini.jarSpecificationVersion}";
    }

    @Override
    public String getJarPath() {
       return "target/it/modules/${zucchini.artifactId}/target/${zucchini.artifactId}.jar";
    }
}
