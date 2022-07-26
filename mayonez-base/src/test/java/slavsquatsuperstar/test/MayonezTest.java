package slavsquatsuperstar.test;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.shapes.Polygon;
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Driver code for new features in the engine.
 */
public class MayonezTest {

    public static void main(String[] args) throws Exception {
//        EventListener myListener1 = event -> Logger.log("Listener 1 reports: %s", event);
//        EventListener myListener2 = event -> Logger.log("Listener 2 reports: %s", event);
//        EventGenerator myGenerator = new EventGenerator();
//        myGenerator.addListener(myListener1);
//        myGenerator.addListener(myListener2);
//        myGenerator.createEvent(new Event("Clankers inbound!"));

        Polygon poly = new Polygon(new Vec2(0, 0), new Vec2(1, 0), new Vec2(1, 1), new Vec2(0, 1));
        System.out.println(poly);
//        System.out.println(poly.scale(new Vec2(1, 1), null)); // scale 1x from center
        System.out.println(poly.scale(new Vec2(2, 2), null)); // scale 2x from center, area 4x
//        System.out.println(poly.scale(new Vec2(2, 2), new Vec2(0.5f, 0.5f))); // same effect as above
        System.out.println(poly.scale(new Vec2(2, 2), new Vec2(0, 0))); // scale 2x from origin, area 4x
        System.out.println(poly.scale(new Vec2(2, 2), new Vec2(-1, -1))); // scale 2x from (-1, -1)), area 4x

        Rectangle rect = new Rectangle(new Vec2(0.5f, 0.5f), new Vec2(1, 1));
        System.out.println(rect);
        System.out.println(rect.scale(new Vec2(2, 2), null)); // scale 2x from center, area 4x
        System.out.println(rect.scale(new Vec2(2, 2), new Vec2(0, 0))); // scale 2x from origin, area 4x
        System.out.println(rect.scale(new Vec2(2, 2), new Vec2(-1, -1))); // scale 2x from (-1, -1)), area 4x
    }

    interface EventListener {
        void onReceiveEvent(Event event);
    }

    static class Event {
        private final String msg;

        public Event(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return String.format("%s (%s)",
                    getClass().isAnonymousClass() ? "Event" : getClass().getSimpleName(), msg);
        }
    }

    static class EventGenerator {
        private final List<EventListener> listeners = new ArrayList<>();

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
