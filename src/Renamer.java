import javax.swing.*;
import java.awt.*;

public class Renamer {
    String path;

    public static void main( final String[] args ) {
        createForm();
    }

    public static void createForm() {
        MainForm mainForm = new MainForm();
        mainForm.createForm();
    }
}
