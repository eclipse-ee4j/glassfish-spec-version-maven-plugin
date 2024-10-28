/*
  Copyright (c) 2024 Eclipse Foundation and/or its affiliates. All rights reserved.

  This program and the accompanying materials are made available under the
  terms of the Eclipse Public License v. 2.0, which is available at
  http://www.eclipse.org/legal/epl-2.0.

  This Source Code may also be made available under the following Secondary
  Licenses when the conditions for such availability set forth in the
  Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
  version 2 with the GNU Classpath Exception, which is available at
  https://www.gnu.org/software/classpath/license.html.

  SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
*/

String [] buildLog = new File(basedir, 'build.log')

goalExecutionLines = buildLog.grep ~/^\[INFO].*spec-version:.*:check-distribution.*distribution-negative.*$/

assert goalExecutionLines.size() == 1: 'Info about goal execution is expected in build output'

unmetExtensionNameLines = buildLog.grep ~/^.*WARNING: Extension-Name \(jakarta\.transaction\) should be javax\.transaction.*$/

assert unmetExtensionNameLines.size() == 1: 'Warning about unmet expectation for Extension-Name is expected in build output'

unmetSpecVersion = buildLog.grep ~/^.*WARNING: Specification-Version \(2\.0\) should be 2\.1.*/

assert unmetSpecVersion.size() == 1: 'Warning about unmet expectation for Specification-Version is expected in build output'

unmetSpecImplVersion = buildLog.grep ~/^.*WARNING: spec-impl-version \(1\.1\.0\) must start with JCP spec-version number \(2\.1\).*$/

assert unmetSpecImplVersion.size() == 1: 'Warning about unmet expectation for spec-impl-version is expected in build output'

failureMessage = buildLog.grep ~/^\[ERROR] Failed to execute goal .*:check-distribution.*: Found spec errors.*$/

assert failureMessage.size() == 1: 'Error message for failing the build is expected in build output'
