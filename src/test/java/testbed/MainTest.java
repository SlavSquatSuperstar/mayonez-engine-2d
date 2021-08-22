package testbed;

import org.apache.commons.io.IOUtils;
import slavsquatsuperstar.mayonez.Logger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainTest {

    public static void main(String[] args) throws Exception {
//        EventListener myListener1 = event -> Logger.log("Listener 1: %s", event);
//        EventListener myListener2 = event -> Logger.log("Listener 2 reports: %s", event);
//        EventGenerator myGenerator = new EventGenerator();
//        myGenerator.addListener(myListener1);
//        myGenerator.addListener(myListener2);
//        myGenerator.createEvent(new Event("Clankers inbound!"));

//        URL root = ClassLoader.getSystemResource("assets");
//        Logger.log("Root = %s", root.getPath());

//        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("assets/");
//        while (resources.hasMoreElements()) {
//            URL url = resources.nextElement();
//            System.out.println(url);
//            System.out.println(new Scanner((InputStream) url.getContent()).useDelimiter("\\A").next());
//        }

        List<String> files = IOUtils.readLines(ClassLoader.getSystemResourceAsStream("assets/"), StandardCharsets.UTF_8);
        files.forEach(f -> Logger.log(files.toString()));
    }

    interface EventListener {
        void onReceiveEvent(Event event);
    }

    static class Event {
        private String msg;

        public Event(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return msg;
        }
    }

    static class EventGenerator {
        private List<EventListener> listeners = new ArrayList<>();

        public void addListener(EventListener e) {
            listeners.add(e);
        }

        public void removeListener(EventListener e) {
            listeners.remove(e);
        }

        public void createEvent(Event event) {
            listeners.forEach(l -> l.onReceiveEvent(event));
        }
    }

}
