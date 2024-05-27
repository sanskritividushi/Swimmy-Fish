import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AvatarSelection extends JDialog {
    private Image selectedAvatar;
    int boardWidth = 700;
    int boardHeight = 750;

    public AvatarSelection(Frame parent, Image fish1, Image fish2, int boardWidth, int boardHeight) {
        super(parent, "Select Avatar", true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        
        // Create panels for the avatars
        JPanel avatarsPanel = new JPanel();
        avatarsPanel.setLayout(new GridLayout(1, 2));
        
        // Add the first avatar
        JButton fish1Button = new JButton(new ImageIcon(fish1.getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        fish1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedAvatar = fish1;
                setVisible(false);
            }
        });
        avatarsPanel.add(fish1Button);

        // Add the second avatar
        JButton fish2Button = new JButton(new ImageIcon(fish2.getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        fish2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedAvatar = fish2;
                setVisible(false);
            }
        });
        avatarsPanel.add(fish2Button);

        add(avatarsPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(parent);
    }

    public Image getSelectedAvatar() {
        return selectedAvatar;
    }
}
