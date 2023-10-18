import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ThirdLessonTests {
    @ParameterizedTest
    @ValueSource(strings = {"", "qwertyqwerty123","qwerty1231231231","qwerty12312312312", "qwertyqwerty12", "qwerty1", "gregergerggrgegergergergergergergergergergegrgergerger"})
    public void FirstHomeWork(String name){

            assertTrue(name.length() > 15, "Маленькое сообщение, не подходит");


    }
}
