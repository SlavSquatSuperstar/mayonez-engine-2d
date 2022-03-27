package slavsquatsuperstar.test;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.math.geom.Polygon;
import slavsquatsuperstar.math.geom.Triangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Driver code for new features in the engine.
 */
public class MayonezTest {

    public static void main(String[] args) throws Exception {
        Polygon poly = new Polygon(new Vec2(0, 0), new Vec2(2, 0), new Vec2(2, 3), new Vec2(0, 3));
//        Polygon poly = new Polygon(new Vec2(), 5, 4f);
        System.out.println(poly.area());
        System.out.println(poly.angMass(1));
        System.out.println();

        for (Triangle t : poly.getTriangles()) {
            System.out.println(t.area());
            System.out.println(t.angMass(1));
            System.out.println();
        }

//        EventListener myListener1 = event -> Logger.log("Listener 1: %s", event);
//        EventListener myListener2 = event -> Logger.log("Listener 2 reports: %s", event);
//        EventGenerator myGenerator = new EventGenerator();
//        myGenerator.addListener(myListener1);
//        myGenerator.addListener(myListener2);
//        myGenerator.createEvent(new Event("Clankers inbound!"));
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
