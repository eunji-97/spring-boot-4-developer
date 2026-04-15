import org.junit.jupiter.api.*;

public class JUnitCycleTest {

    @BeforeAll // 전체 테스트를 시작하기 전 1회 실행 (static)
    static void beforeAll(){
        System.out.println("beforeAll");
    }

    @BeforeEach // 테스트 케이스를 시작하기 전마다 실행
    public void beforeEach() {
        System.out.println("beforeEach");
    }

    @Test
    public void Test1() {
        System.out.println("test1");
    }

    @Test
    public void Test2() {
        System.out.println("test2");
    }

    @Test
    public void Test3() {
        System.out.println("test3");
    }

    @AfterAll // 전체 테스트를 마치고 종료 전 1회 실행 (static)
    static void afterAll() {
        System.out.println("afterAll");
    }

    @AfterEach // 테스트 케이스 종료하기 전마다 실행
    public void afterEach() {
        System.out.println("afterEach");
    }
}
