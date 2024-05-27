import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 500;
        int boardHeight = 640;

        JFrame frame = new JFrame("Fishy Fish");
        // frame.setVisible(true);
		frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FishyFish fishyfish = new FishyFish();
        frame.add(fishyfish);
        frame.pack();//pack used to avoid condiering the frame width (the game title on the pop up) within the dimensions
        fishyfish.requestFocus();
        frame.setVisible(true);
    }
}