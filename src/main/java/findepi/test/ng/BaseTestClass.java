/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package findepi.test.ng;

import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.testng.Assert.assertEquals;

@Test(singleThreaded = true)
public abstract class BaseTestClass
{
    private final AtomicInteger currentTests = new AtomicInteger();

    protected void test()
    {
        int currentTests = this.currentTests.incrementAndGet();
        try {
            assertEquals(currentTests, 1);
            MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            this.currentTests.decrementAndGet();
        }
    }

    @Test
    public void test1()
    {
        test();
    }

    @Test
    public void test2()
    {
        test();
    }

    @Test
    public void test3()
    {
        test();
    }

    @Test
    public void test4()
    {
        test();
    }

    // See https://github.com/cbeust/testng/issues/2361#issuecomment-688393166
    public abstract void forceTestNgToRespectSingleThreaded();
}
