import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainTest {

    public static void main(String[] args) throws CloneNotSupportedException, IOException {
//		String[] names = {"Kai", "Jay", "Zane", "Cole", "Lloyd", "Nia", "Wu", "Skylor"};
//		List<String> ninjas = Arrays.asList(names);
//		List<String> shortNames = ninjas.stream().filter(s -> s.length() <= 4).collect(Collectors.toList());
//		shortNames.forEach(System.out::println);

        JFrame frame = new JFrame("image from jar");
        JLabel l = new JLabel();
        ImageIcon img = getImage("mario.png");
        l.setIcon(img);
        frame.add(l);
        frame.pack();
        frame.setVisible(true);

        StringBuilder s = new StringBuilder();

//        A a1 = new A(69);
//        A a2 = a1.clone();
//        a2.id = 420;
//        System.out.println(a1.id);
//        System.out.println(a2.id);
    }

    public static ImageIcon getImage(String filename) throws IOException {
        URL url = Objects.requireNonNull(MainTest.class.getClassLoader().getResource(filename));
        System.out.println(url.getFile());
        BufferedImage img = ImageIO.read(url);
        return new ImageIcon(url);
    }

    static class A implements Cloneable {
        int id;

        public A(int id) {
            this.id = id;
        }

        @Override
        public A clone() throws CloneNotSupportedException {
            return (A) super.clone();
        }
    }

}
