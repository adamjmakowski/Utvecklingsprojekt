package View;

import javax.swing.*;
import java.awt.*;

public class GUIUtilities {
    public static ImageIcon scaleImage(String filepath, int w, int h) {
        ImageIcon temp = new ImageIcon(filepath);
        Image img = temp.getImage();
        Image scaled = img.getScaledInstance(w,h, Image.SCALE_SMOOTH);
        ImageIcon scaledPic = new ImageIcon(scaled);
        return scaledPic;
    }
    public static Image scaleImage(Image image, int w, int h) {
        return image.getScaledInstance(w,h,Image.SCALE_SMOOTH);
    }
    public static boolean isImage(String filepath) {
        return filepath.endsWith(".jpg") || filepath.endsWith(".png") || filepath.endsWith(".jpeg") || filepath.endsWith(".gif");
    }
    public static void displayImage(String sender, String text, ImageIcon image) {
        JOptionPane.showMessageDialog(null, text, "Bild från: " + sender, JOptionPane.INFORMATION_MESSAGE, image);
    }
    public static JLabel createUserLabel(ImageIcon icon, String name) {
        JLabel lbl = new JLabel(icon);
        lbl.setText(name);
        return lbl;
    }
}
