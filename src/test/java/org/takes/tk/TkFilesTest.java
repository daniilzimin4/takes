/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.tk;

import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.llorllale.cactoos.matchers.StartsWith;
import org.takes.HttpException;
import org.takes.rq.RqFake;
import org.takes.rs.HeadPrint;

/**
 * Test case for {@link TkFiles}.
 * @since 0.8
 */
public final class TkFilesTest {

    /**
     * Temp directory.
     */
    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();

    /**
     * TkFiles can dispatch by file name.
     * @throws Exception If some problem inside
     */
    @Test
    public void dispatchesByFileName() throws Exception {
        FileUtils.write(
            this.temp.newFile("a.txt"), "hello, world!", StandardCharsets.UTF_8
        );
        MatcherAssert.assertThat(
            new HeadPrint(
                new TkFiles(this.temp.getRoot()).act(
                    new RqFake(
                        "GET", "/a.txt?hash=a1b2c3", ""
                    )
                )
            ),
            new StartsWith("HTTP/1.1 200 OK")
        );
    }

    /**
     * TkFiles can throw when file not found.
     * @throws Exception If some problem inside
     */
    @Test(expected = HttpException.class)
    public void throwsWhenResourceNotFound() throws Exception {
        new TkFiles("/absent-dir-for-sure").act(
            new RqFake("PUT", "/something-else.txt", "")
        );
    }
}
