/*
 *  Copyright (c) 2020, 2023 Eclipse Foundation and/or its affiliates. All rights reserved.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v. 2.0, which is available at
 *  http://www.eclipse.org/legal/epl-2.0.

 *  This Source Code may also be made available under the following Secondary
 *  Licenses when the conditions for such availability set forth in the
 *  Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 *  version 2 with the GNU Classpath Exception, which is available at
 *  https://www.gnu.org/software/classpath/license.html.
 *
 *  SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
*/
package org.glassfish.spec.test.unit;

import static org.glassfish.spec.Artifact.stripApprovedQualifier;

import static java.util.Arrays.asList;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ArtifactTest {
    @Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[/* full version */][/* expected/stripped version*/] {
            { "2.3.4-SNAPSHOT", "2.3.4" },
            { "2.4.11-RC1", "2.4.11" },
            { "3.1.7", "3.1.7" },
            { "4.5-b2", "4.5-b2" }
        });
    }

    @Parameter(0)
    public String fullVersion;

    @Parameter(1)
    public String stripped;

    @Test
    public void testStrippingQualifier() {
        assertEquals(stripped, stripApprovedQualifier(fullVersion));
    }
}
