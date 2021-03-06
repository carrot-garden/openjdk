/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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

/*
 * @test
 * @bug 8170859
 * @summary Ensure no incubator modules are resolved by default in the image
 * @library /lib/testlibrary
 * @modules jdk.compiler
 * @build CompilerUtils
 * @run testng DefaultImage
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jdk.testlibrary.ProcessTools.executeCommand;
import static org.testng.Assert.*;

public class DefaultImage {
    private static final String JAVA_HOME = System.getProperty("java.home");
    private static final Path TEST_SRC = Paths.get(System.getProperty("test.src"));
    private static final Path CP_DIR = Paths.get("cp");

    @BeforeTest
    private void setup() throws Throwable {
        Path src = TEST_SRC.resolve("src").resolve("cp").resolve("listmods");
        assertTrue(CompilerUtils.compile(src, CP_DIR));
    }

    public void test() throws Throwable {
        if (isExplodedBuild()) {
            System.out.println("Test cannot run on exploded build");
            return;
        }

        java("-cp", CP_DIR.toString(),
             "listmods.ListModules")
            .assertSuccess()
            .resultChecker(r -> r.assertOutputContains("java.base"))
            .resultChecker(r -> r.assertOutputDoesNotContain("jdk.incubator"));
    }

    @DataProvider(name = "tokens")
    public Object[][] singleModuleValues() throws IOException {
        return new Object[][]{ { "ALL-DEFAULT" }, { "ALL-SYSTEM"} };
    }

    @Test(dataProvider = "tokens")
    public void testAddMods(String addModsToken) throws Throwable {
        if (isExplodedBuild()) {
            System.out.println("Test cannot run on exploded build");
            return;
        }

        java("--add-modules", addModsToken,
             "-cp", CP_DIR.toString(),
             "listmods.ListModules")
            .assertSuccess()
            .resultChecker(r -> r.assertOutputContains("java.base"))
            .resultChecker(r -> r.assertOutputDoesNotContain("jdk.incubator"));
    }

    static ToolResult java(String... opts) throws Throwable {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        String[] options = Stream.concat(Stream.of(getJava()), Stream.of(opts))
                .toArray(String[]::new);

        ProcessBuilder pb = new ProcessBuilder(options);
        int exitValue = executeCommand(pb).outputTo(ps)
                .errorTo(ps)
                .getExitValue();

        return new ToolResult(exitValue, new String(baos.toByteArray(), UTF_8));
    }

    static class ToolResult {
        final int exitCode;
        final String output;

        ToolResult(int exitValue, String output) {
            this.exitCode = exitValue;
            this.output = output;
        }

        ToolResult assertSuccess() {
            assertEquals(exitCode, 0,
                    "Expected exit code 0, got " + exitCode
                            + ", with output[" + output + "]");
            return this;
        }

        ToolResult resultChecker(Consumer<ToolResult> r) {
            r.accept(this);
            return this;
        }

        ToolResult assertOutputContains(String subString) {
            assertTrue(output.contains(subString),
                       "Expected to find [" + subString + "], in output ["
                            + output + "]" + "\n");
            return this;
        }

        ToolResult assertOutputDoesNotContain(String subString) {
            assertFalse(output.contains(subString),
                        "Expected to NOT find [" + subString + "], in output ["
                            + output + "]" + "\n");
            return this;
        }
    }

    static String getJava() {
        Path image = Paths.get(JAVA_HOME);
        boolean isWindows = System.getProperty("os.name").startsWith("Windows");
        Path java = image.resolve("bin").resolve(isWindows ? "java.exe" : "java");
        if (Files.notExists(java))
            throw new RuntimeException(java + " not found");
        return java.toAbsolutePath().toString();
    }

    static boolean isExplodedBuild() {
        Path modulesPath = Paths.get(JAVA_HOME).resolve("lib").resolve("modules");
        return Files.notExists(modulesPath);
    }
}
