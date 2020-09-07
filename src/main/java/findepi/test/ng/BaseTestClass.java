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
