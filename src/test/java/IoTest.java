import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

public class IoTest {
    @Test
    public void processTest() throws IOException {
        Process process = Runtime.getRuntime().exec("F:\\java\\example\\hexoConvert\\src\\test\\resources\\test.bat");
        byte[] buf = IOUtils.toByteArray(process.getInputStream());
        System.out.println(new String(buf, "UTF-8"));
    }
}
