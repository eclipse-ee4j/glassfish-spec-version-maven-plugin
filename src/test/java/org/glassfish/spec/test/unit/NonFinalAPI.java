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

package org.glassfish.spec.test.unit;

import org.glassfish.spec.test.TestSpec;
import org.glassfish.spec.test.sets.Aubergine;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Romain Grecourt
 */
public class NonFinalAPI {

    private static TestSpec spec;

    @BeforeClass
    public static void init(){
        spec = new Aubergine();
    }

    @Test
    public void verifySpec() {
        spec.verify();
        if(!spec.getSpec().getErrors().isEmpty()){
            StringBuilder msg = new StringBuilder(spec.getSpec().toString());
            msg.append(" should be compliant");
            if (!spec.getSpec().getErrors().isEmpty()) {
                msg.append(" -- ");
            }
            for(int i=0 ; i < spec.getSpec().getErrors().size() ; i++){
                msg.append(spec.getSpec().getErrors().get(i));
                if(i < spec.getSpec().getErrors().size() -1){
                   msg.append(" -- ");
                }
            }
            Assert.fail(msg.toString());
        }
    }

    @Test
    public void verifyMetadata(){
        spec.assertMetadata();
    }
}
