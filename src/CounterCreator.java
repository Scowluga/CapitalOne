import java.util.HashMap;
import java.util.Map;

class CounterCreator {
    static Map<String, AbstractCounter> initCounters() {
        Map<String, AbstractCounter> map = new HashMap<>();

        AbstractCounter standardCounter = new AbstractCounter() {
            void foo() {

            }



            @Override
            void nextLine(String line) {

            }
        };

        map.put(".java", standardCounter);
        map.put(".ts", standardCounter);
        map.put(".c", standardCounter);
        map.put(".cc", standardCounter);

        return map;
    }
}
