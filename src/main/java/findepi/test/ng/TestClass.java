package findepi.test.ng;

import org.testng.annotations.Test;

@Test(singleThreaded = true)
public class TestClass
        extends BaseTestClass
{
    @Override
    public void forceTestNgToRespectSingleThreaded() {}
}
