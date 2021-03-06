/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jdk.internal.misc;

import java.io.PrintStream;
import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleDescriptor.Exports;
import java.lang.module.ModuleDescriptor.Opens;
import java.lang.module.ModuleDescriptor.Requires;
import java.lang.module.ModuleDescriptor.Provides;
import java.lang.module.ModuleDescriptor.Version;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.net.URI;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import jdk.internal.module.ModuleHashes;

/**
 * Provides access to non-public methods in java.lang.module.
 */

public interface JavaLangModuleAccess {

    /**
     * Creates a builder for building a module with the given module name.
     *
     * @param strict
     *        Indicates whether module names are checked or not
     */
    ModuleDescriptor.Builder newModuleBuilder(String mn,
                                              boolean strict,
                                              boolean open,
                                              boolean synthetic);

    /**
     * Returns the set of packages that are exported (unconditionally or
     * unconditionally).
     */
    Set<String> exportedPackages(ModuleDescriptor.Builder builder);

    /**
     * Returns the set of packages that are opened (unconditionally or
     * unconditionally).
     */
    Set<String> openPackages(ModuleDescriptor.Builder builder);

    /**
     * Returns a {@code ModuleDescriptor.Requires} of the given modifiers
     * and module name.
     */
    Requires newRequires(Set<Requires.Modifier> ms, String mn, Version v);

    /**
     * Returns an unqualified {@code ModuleDescriptor.Exports}
     * of the given modifiers and package name source.
     */
    Exports newExports(Set<Exports.Modifier> ms,
                       String source);

    /**
     * Returns a qualified {@code ModuleDescriptor.Exports}
     * of the given modifiers, package name source and targets.
     */
    Exports newExports(Set<Exports.Modifier> ms,
                       String source,
                       Set<String> targets);

    /**
     * Returns an unqualified {@code ModuleDescriptor.Opens}
     * of the given modifiers and package name source.
     */
    Opens newOpens(Set<Opens.Modifier> ms, String source);

    /**
     * Returns a qualified {@code ModuleDescriptor.Opens}
     * of the given modifiers, package name source and targets.
     */
    Opens newOpens(Set<Opens.Modifier> ms, String source, Set<String> targets);

    /**
     * Returns a {@code ModuleDescriptor.Provides}
     * of the given service name and providers.
     */
    Provides newProvides(String service, List<String> providers);

    /**
     * Returns a {@code ModuleDescriptor.Version} of the given version.
     */
    Version newVersion(String v);

    /**
     * Clones the given module descriptor with an augmented set of packages
     */
    ModuleDescriptor newModuleDescriptor(ModuleDescriptor md, Set<String> pkgs);

    /**
     * Returns a new {@code ModuleDescriptor} instance.
     */
    ModuleDescriptor newModuleDescriptor(String name,
                                         Version version,
                                         boolean open,
                                         boolean automatic,
                                         boolean synthetic,
                                         Set<Requires> requires,
                                         Set<Exports> exports,
                                         Set<Opens> opens,
                                         Set<String> uses,
                                         Set<Provides> provides,
                                         Set<String> packages,
                                         String mainClass,
                                         String osName,
                                         String osArch,
                                         String osVersion,
                                         int hashCode);

    /**
     * Resolves a collection of root modules, with service binding
     * and the empty configuration as the parent. The post resolution
     * checks are optionally run.
     */
    Configuration resolveRequiresAndUses(ModuleFinder finder,
                                         Collection<String> roots,
                                         boolean check,
                                         PrintStream traceOutput);

}
