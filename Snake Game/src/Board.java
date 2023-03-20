import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int B_height=400;
    int B_width=400;
    int maxdots=1600;
    int dotsize=10;
    int dots;
    int score;

    int []x=new int[maxdots];
    int []y=new int[maxdots];
    int apple_x;
    int apple_y;

    //Images
    Image body,head,apple;
    Timer timer;
    int delay=300;

    boolean leftdirection=true;
    boolean rightdirection=false;
    boolean updirection=false;
    boolean downdirection=false;

    boolean ingame=true;

    Board(){
        score=0;
        TAdapter tAdapter=new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_width,B_height));
        setBackground(Color.BLACK);
        initGame();
        loadImages();
    }

    //initialize game
    public void initGame(){
        dots=3;

        //initialize snake position
        x[0]=250; y[0]=250;
        for(int i=1;i<dots;i++){
            x[i]=x[0]+dotsize*i;
            y[i]=y[0];
        }

        locateApple();
        timer=new Timer(delay,this);
        timer.start();
    }
    //load images from resources folder to image object
    public void loadImages(){
        ImageIcon bodyIcon=new ImageIcon("src/resources/dot.png");
        body=bodyIcon.getImage();

        ImageIcon headIcon=new ImageIcon("src/resources/head.png");
        head=headIcon.getImage();

        ImageIcon appleIcon=new ImageIcon("src/resources/apple.png");
        apple=appleIcon.getImage();
    }

    //draw images at snake and apple position
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }

    //draw images
    public void doDrawing(Graphics g){
        if(ingame){
            g.drawImage(apple,apple_x,apple_y,this);

            for(int i=0;i<dots;i++){
                if(i==0){
                    g.drawImage(head,x[0],y[0],this);
                }
                else{
                    g.drawImage(body,x[i],y[i],this);
                }
            }
        }
        else {
            gameover(g);
            timer.stop();
        }
    }

    //Randomize apple's possition
    public void locateApple(){
        apple_x=((int)(Math.random()*39))*dotsize;
        apple_y=((int)(Math.random()*39))*dotsize;
    }

    //check collision with border and body
    public void checkCollision(){
        //body collision
        for(int i=1;i<dots;i++){
            if(i>4 && x[0]==x[i] && y[0]==y[i]){
                ingame=false;
            }
        }
        //wall collision
        if(x[0]<0 || x[0]>=B_width || y[0]<0 || y[0]>=B_height)
            ingame=false;
    }

    //display game over message
    public void gameover(Graphics g){
        String msg="Game Over";
        score=dots-3;
        String scoremsg="Score:"+Integer.toString(score);
        Font small=new Font("Helvrtica",Font.BOLD,14);
        FontMetrics fontMetrics=getFontMetrics(small);
        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg,(B_width-fontMetrics.stringWidth(msg))/2,B_height/4);
        g.drawString(scoremsg,(B_width-fontMetrics.stringWidth(msg))/2,3*(B_height/4));
    }
    @Override
    public void actionPerformed(ActionEvent ae){
        if(ingame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }
    public void move(){
        for(int i=dots-1;i>0;i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if(leftdirection)
            x[0]-=dotsize;
        if(rightdirection)
            x[0]+=dotsize;
        if(updirection)
            y[0]-=dotsize;
        if(downdirection)
            y[0]+=dotsize;
    }
    //make snake eat food
    public void checkApple(){
        if(apple_x==x[0] && apple_y==y[0]){
            dots++;
            locateApple();
        }
    }

    //implements control
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key=keyEvent.getKeyCode();
            if(key==KeyEvent.VK_LEFT && rightdirection==false){
                leftdirection=true;
                updirection=false;
                downdirection=false;
            }
            if(key==KeyEvent.VK_RIGHT && leftdirection==false){
                rightdirection=true;
                updirection=false;
                downdirection=false;
            }
            if(key==KeyEvent.VK_UP && downdirection==false){
                updirection=true;
                leftdirection=false;
                rightdirection=false;
            }
            if(key==KeyEvent.VK_DOWN && updirection==false){
                leftdirection=false;
                downdirection=true;
                rightdirection=false;
            }
        }
    }

}
