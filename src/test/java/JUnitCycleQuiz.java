import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JUnitCycleQuiz {


    @BeforeEach
    public void hello() {
        System.out.println("Hello!");
    }

    @Test
    public void junitQuiz3() {
        System.out.println("This is first test");
    }

    @Test
    public void junitQuiz4() {
        System.out.println("This is second test");
    }

    @AfterAll
    public static void bye(){
        System.out.println("bye!");
    }
}
