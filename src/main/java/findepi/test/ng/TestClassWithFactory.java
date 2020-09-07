package findepi.test.ng;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

@Test(singleThreaded = true)
public class TestClassWithFactory
        extends BaseTestClass
{
    @Factory(dataProvider = "factory")
    public TestClassWithFactory(int instance) {}

    @DataProvider
    public static Object[][] factory()
    {
        return new Object[][] {
                {1},
                {2},
        };
    }

    @Override
    public void forceTestNgToRespectSingleThreaded() {}
}
