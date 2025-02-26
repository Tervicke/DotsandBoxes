import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class MButton extends JButton {
    private final Color defaultColor = new Color(80, 80, 80); // Dark Gray
    private final Color hoverColor = new Color(100, 100, 100); // Slightly lighter gray

    public MButton(String text, Runnable onClick) {
        super(text);
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(defaultColor);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultColor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });
    }
}
