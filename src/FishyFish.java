import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FishyFish extends JPanel implements ActionListener, KeyListener { //fish class inherits JPanel , jpanel used for canvas/bg
    //this way we can keep jpanel and add vars and functiosn to the game
    int boardWidth = 700;
    int boardHeight = 750;

    //images
    Image backgroundImg;
    Image fishImg;
    Image topSharkImg;
    Image bottomSharkImg;
    Image gameOverImg;

    //bird class
    int fishX = boardWidth/8;
    int fishY = boardWidth/2;
    int fishWidth = 50;
    int fishHeight = 50;

    class Fish {
        int x = fishX;
        int y = fishY;
        int width = fishWidth;
        int height = fishHeight;
        Image img;

        Fish(Image img) {
            this.img = img;
        }
    }

    //pipe class
    int sharkX = boardWidth;
    int sharkY = 0;
    int sharkWidth = 200;  //scaled by 1/6
    int sharkHeight = 502;
    
    class Shark {
        int x = sharkX;
        int y = sharkY;
        int width = sharkWidth;
        int height = sharkHeight;
        Image img;
        boolean passed = false;

        Shark(Image img) {
            this.img = img;
        }
    }

    //game logic
    Fish fish;
    int velocityX = -4; //move pipes to the left speed (simulates bird moving right)
    int velocityY = 0; //move bird up/down speed.
    int gravity = 1;

    ArrayList<Shark> sharks;
    Random random = new Random();

    Timer gameLoop;
    Timer placeSharkTimer;
    boolean gameOver = false;
    double score = 0;

    FishyFish() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./newwateerbg.jpg")).getImage();
        fishImg = new ImageIcon(getClass().getResource("./fishy.png")).getImage();
        topSharkImg = new ImageIcon(getClass().getResource("./shark.png")).getImage();
        gameOverImg = new ImageIcon(getClass().getResource("./finalgameover.png")).getImage();
        bottomSharkImg = new ImageIcon(getClass().getResource("./sharkbelow.png")).getImage();

        //bird
        fish = new Fish(fishImg);
        sharks = new ArrayList<Shark>();

        //place pipes timer
        placeSharkTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              // Code to be executed
              placeSharks();
            }
        });
        placeSharkTimer.start();
        
		//game timer
		gameLoop = new Timer(1000/60, this); //how long it takes to start timer, milliseconds gone between frames 
        gameLoop.start();
	}
    
    void placeSharks() {
        //(0-1) * pipeHeight/2.
        // 0 -> -128 (pipeHeight/4)
        // 1 -> -128 - 256 (pipeHeight/4 - pipeHeight/2) = -3/4 pipeHeight
        int randomSharkY = (int) (sharkY - sharkHeight/4 - Math.random()*(sharkHeight/2));
        int openingSpace = boardHeight/4;
    
        Shark topShark = new Shark(topSharkImg);
        topShark.y = randomSharkY;
        sharks.add(topShark);
    
        Shark bottomPipe = new Shark(bottomSharkImg);
        bottomPipe.y = topShark.y  + sharkHeight + openingSpace;
        sharks.add(bottomPipe);
    }
    
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

    public void draw(Graphics g) {
        // System.out.println("draw");
        //background
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);

        //bird
        g.drawImage(fishImg, fish.x, fish.y, fish.width, fish.height, null);

        //pipes
        for (int i = 0; i < sharks.size(); i++) {
            Shark shark = sharks.get(i);
            g.drawImage(shark.img, shark.x, shark.y, shark.width, shark.height, null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("American Typewriter", Font.PLAIN, 32));
        if(gameOver) {
            int imgWidth = gameOverImg.getWidth(null);
            int imgHeight = gameOverImg.getHeight(null);
            int centerX = (this.boardWidth - imgWidth) / 2;
            int centerY = (this.boardHeight - imgHeight) / 2;
            g.drawImage(gameOverImg, centerX, centerY, null);   
        }
        else {
            g.drawString("Score: " + String.valueOf((int) score), 10, 35);
        }
        
	}

    public void move() {
        //bird
        velocityY += gravity;
        fish.y += velocityY;
        fish.y = Math.max(fish.y, 0); //apply gravity to current bird.y, limit the bird.y to top of the canvas

        //pipes
        for (int i = 0; i < sharks.size(); i++) {
            Shark shark = sharks.get(i);
            shark.x += velocityX;

            if (!shark.passed && shark.x + shark.width < fish.x) {
                score += 0.5; //0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                shark.passed = true;
            }

            if (collision(fish, shark)) {
                gameOver = true;
            }
        }

        if (fish.y > boardHeight) {
            gameOver = true;
        }
    }

    boolean collision(Fish a, Shark b) {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameOver) {
            placeSharkTimer.stop();
            gameLoop.stop();
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // System.out.println("JUMP!");
            velocityY = -9;

            if (gameOver) {
                //restart game by resetting conditions
                fish.y = fishY;
                velocityY = 0;
                sharks.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placeSharkTimer.start();
            }
        }
    }

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
