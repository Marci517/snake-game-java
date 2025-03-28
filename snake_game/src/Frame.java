
import javax.swing.*;
import java.io.IOException;

public class Frame extends JFrame {

    public Frame() throws IOException {
        Panel p = new Panel();
        this.add(p);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setJMenuBar(p.getMenuBar());
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
