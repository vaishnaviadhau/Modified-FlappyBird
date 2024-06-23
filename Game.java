
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;



class FlappyBird extends JPanel implements ActionListener,KeyListener{
    int borderWidth=360;
    int borderHeight=640;
//images
     Image backgroungImg;
     Image birdImg;
     Image bottomPipeImg;
     Image topPipeImg;
//bird
int birdX=borderWidth/8;
int birdY=borderHeight/2;
int birdwidth=34;
int birdheight=24;
    String currentBird;

class Bird{
    int x=birdX;
    int y=birdY;
    int width=birdwidth;
    int height=birdheight;
    Image img;
    Bird(Image img){
        this.img=img;
    }
}


//change bird 





//pipes
int pipeX=borderWidth;
int pipeY=0;
int pipeWidth=64;
int pipeHeight=512;

class Pipe{
    int x=pipeX;
    int y=pipeY;
    int width=pipeWidth;
    int height=pipeHeight;
    Image img;
    boolean passed=false;

    Pipe(Image img){
        this.img=img;
    }
}







//game logic
Bird bird;
int velocityX=-4; //move to the left speed
int velocityY=0; //bird move up and down
int gravity=1;

ArrayList<Pipe> pipes;
Random random=new Random();
//pipeloop
Timer placePipesTimer;
boolean gameOver=false;
double score=0;
//game timer
Timer gameloop;


JButton BIRD;
JWindow panel;
JButton b1;
JButton b2;
JButton b3;
JButton b4;
// images.jpeg1-removebg-preview.png

FlappyBird(){
   
    setPreferredSize(new Dimension(borderWidth,borderHeight));
    // setBackground(Color.blue);
    //load the images
    backgroungImg =new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();//here the bgimg is of type image but we want image so we get using .getimage method
    birdImg =new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
    topPipeImg=new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
    bottomPipeImg=new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
   
    //bird
    bird=new Bird(birdImg);
    //pipe
    pipes=new ArrayList<Pipe>();
    placePipesTimer=new Timer(1500,new ActionListener() 
    {public void actionPerformed(ActionEvent e){placePipe();}});
    placePipesTimer.start();
    gameloop=new Timer(1000/60,this); 
    gameloop.start();
    setFocusable(true);
    addKeyListener(this);




    BIRD=new JButton("Bird");
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BorderLayout());
    buttonPanel.add(BIRD,BorderLayout.EAST);
    add(buttonPanel, BorderLayout.EAST);
    BIRD.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
       showScreen();
        }
    });

    

}
//changeBirdImage



public void showScreen(){
    panel=new JWindow();
    panel.setSize(300,300);
    panel.setLayout(new GridLayout(2,3));
    panel.setLocationRelativeTo(null); // Center the window
    b1=new JButton("bird1");
    b2=new JButton("bird2");
    b3=new JButton("bird3");
    b4=new JButton("bird4");    
    b1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
           changeBird("bird1");
           panel.dispose();
        }
    });

    panel.add(b1);
    panel.add(b2);
    panel.add(b3);
    panel.add(b4);
    panel.setVisible(true);
}

private void changeBird(String birdName){
    currentBird=birdName;
    
}




//place pipe

public void placePipe(){
    //math.random()=0-1*pipeheight/2--> 0-256
    //pipeheiught/4-->128
    //pipey=0
    //0-128-512/2 if math.random()=1 then 1/4pipeheight and if 0 thenm 3/4 pipeheight
    int randomPipeY=(int)(pipeY - pipeHeight/4 -Math.random()*(pipeHeight/2));
    int openingSpace=borderHeight/4;
    Pipe topPipe=new Pipe(topPipeImg);
    topPipe.y=randomPipeY;
    pipes.add(topPipe);
   
    Pipe bottomPipe=new Pipe(bottomPipeImg);
    bottomPipe.y=topPipe.y + pipeHeight + openingSpace;
    pipes.add(bottomPipe);
}

public void paintComponent(Graphics g){
    super.paintComponent(g);
    draw(g);
}

public void draw(Graphics g){
    //background
    g.drawImage(backgroungImg, 0, 0, borderWidth,borderHeight,null);
    //bird
    g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);
   
    for(int i =0;i<pipes.size();i++){
        Pipe pipe=pipes.get(i);
       
        g.drawImage(pipe.img, pipe.x,pipe.y, pipe.width, pipe.height, null);
    }

    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial",Font.PLAIN,19));

    if(gameOver){
        g.drawString("Game Over: "+String.valueOf((int) score), 20 ,35);
    }
    else{
        g.drawString("Score :"+String.valueOf((int) score), 10, 35);
    }
}
public void move(){
    velocityY+=gravity;
    bird.y+=velocityY;
    bird.y=Math.max(bird.y,0);

    for(int i=0;i<pipes.size();i++){
        Pipe pipe=pipes.get(i);
        pipe.x+=velocityX;

       if(!pipe.passed&&bird.x>pipe.x+pipe.width){
        pipe.passed=true;
        score+=0.5;
       }


        if(collision(bird, pipe)){
            gameOver=true;
        }
    }

    if(bird.y>borderHeight){
        gameOver=true;
    }
}

public boolean collision(Bird a,Pipe b){
    return a.x < b.x+b.width&&
           a.x+a.width > b.x&&
           a.y < b.y+b.height&&
           a.y+a.height > b.y; 
}











public void actionPerformed(ActionEvent e)//this is for gameloop
 {
    move();
   repaint();
   if(gameOver)
  {
    placePipesTimer.stop();
   gameloop.stop();
  
  }
}



@Override
public void keyPressed(KeyEvent e) {
 if(e.getKeyCode()==KeyEvent.VK_SPACE){
    velocityY=-9;
    if(gameOver){
      bird.y=birdY;
      velocityY=0;
      pipes.clear();
      gameOver=false;
      score=0;
      gameloop.start();
      placePipesTimer.start();
    }
 }
}





public void keyTyped(KeyEvent e) {}
public void keyReleased(KeyEvent e) {}






}












































public class Game {
    public static void main(String[] args){
    JFrame f=new JFrame("Flappy Bird");
    f.setSize(360, 640);
    
    f.setLocationRelativeTo(null);
    f.setResizable(false);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
   FlappyBird g=new FlappyBird();
   f.add(g);
   f.pack();
   f.requestFocus();
   f.setVisible(true);
    }
}
