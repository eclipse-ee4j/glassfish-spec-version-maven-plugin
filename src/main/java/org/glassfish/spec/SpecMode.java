/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;

/**
 * API specification modes.
 * Value of API specification mode is passed as <code>specMode</code>
 * configuration property.
 */
public enum SpecMode {
    /** Java EE spec mode for <code>javaee</code> value
     *  of <code>specMode</code> property (default). */
    JAVAEE("JavaEE", "javax."),
    /** Jakarta EE4J spec mode for <code>jakarta</code> value
     *  of <code>specMode</code> property. */
    JAKARTA("Jakarta", "jakarta.");

    /** Spec mode enumeration elements count. */
    public static final int COUNT = SpecMode.values().length;

    /** Spec mode name to SpecMode instance conversion map. */
    private static final Map<String, SpecMode> STR_TO_SPECMODE_MAP
            = new HashMap<>(COUNT);

    // Initialize spec mode name to SpecMode instance conversion
    // map.
    static {
        for (SpecMode sm : SpecMode.values()) {
            STR_TO_SPECMODE_MAP.put(sm.name.toLowerCase(), sm);
        }
    }

    /**
     * Get spec plugin mode with corresponding name.
     * @param name name of spec plugin mode
     * @return spec plugin mode with corresponding name or default
     *         <code>JAVAEE</code> value when no appropriate value
     *         was found.
     */
    public static SpecMode getSpecMode(final String name) {
        if (name == null) {
            return JAVAEE;
        }
        final SpecMode sm = STR_TO_SPECMODE_MAP.get(name.toLowerCase());
        return sm != null ? sm : JAVAEE;
    }

    /**
     * Name of spec plugin mode.
     * Value of <code>name.toLowerCase()</code> must match corresponding
     * <code>specMode</code> lowercase value from <code>pom.xml</code>.
     */
    private final String name;

    /**
     * Group ID and package prefix for this mode.
     * Including <code>'.'</code> at the end.
     */
    private final String prefix;

    /**
     * Creates an instance of spec plugin mode.
     * @param modeName name of spec plugin mode
     * @param groupIdPrefix group ID prefix for specific mode
     */
    SpecMode(final String modeName, final String groupIdPrefix) {
        this.name = modeName;
        this.prefix = groupIdPrefix;
    }

    /**
     * Returns group ID and package prefix for this mode.
     * Including  <code>'.'</code> at the end.
     * @return group ID and package prefix for this mode
     */
    public String grePrefix() {
        return prefix;
    }

}
