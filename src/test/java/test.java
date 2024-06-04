import org.junit.jupiter.api.Test;

public class test {
    @Test
    public void test() {
        ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "gws");
        threadLocal.set("123");
        threadLocal.remove();
        System.out.println(threadLocal.get());
    }
}
