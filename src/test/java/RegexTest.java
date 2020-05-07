import com.regex.RegexReplace;
import org.junit.Test;


/**
 * <h3>hexoConvert</h3>
 *
 * @author : ck
 * @date : 2020-05-07 09:59
 **/
public class RegexTest {
    @Test
    public void regexTest1() {
        String picRex = "!\\[\\]\\(/picUp/.*?\\)";
        String str = "xx![](/picUp/测试___无标题.png) 123 ![](/picUp/测试___无标题.png)";
        System.out.println(RegexReplace.replace(str, picRex, (strs) -> "nice"));
    }
}
