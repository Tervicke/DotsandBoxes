import javax.swing.*;
import java.awt.*;

public class MLabel extends JLabel {
    public MLabel(String text) {
        super(text);
        setForeground(Color.WHITE); // Text color
        setFont(new Font("Arial", Font.BOLD, 18)); // Modern font
        setOpaque(true); // Required for background color
        setBackground(new Color(50, 50, 50)); // Dark background
        setHorizontalAlignment(SwingConstants.CENTER); // Center text
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding
        setFont(new Font("Arial", Font.BOLD, 18));
        setBackground(Color.GRAY);
    }
}
