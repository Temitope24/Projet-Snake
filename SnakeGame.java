import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
//import java.util.ArrayList;


public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    
    private class Tile{
        int x;
        int y;
        Tile(int x,int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tailleduncarreau=25;

    //pour le serpent 
    Tile têteduserpent;
    ArrayList<Tile> corpsduserpent;

    //pour la nourriture du serpent 
    Tile nourriture;
    Random random;

    // logique du jeu 
    Timer boucledujeu;
    int vélocitéX;
    int vélocitéY;
    Boolean gameOver= false;


    SnakeGame (int boardWidth,int boardHeight){
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);


        têteduserpent =new Tile(5,5);//les x et les y sont compter en terme de case, chaque case fais 25 pixels, donc la tête du serpent est à 125 pixels vers la droite(pour les x) et 125 pixel vers le bas
        corpsduserpent=new ArrayList<Tile>();

        nourriture= new Tile(10,10);
        random=new Random();
        placeNourriture();//fonciton permettant de placer aléatoirement la nourriture sur le graphe 

        vélocitéX=0;
        vélocitéY=0;

        boucledujeu= new Timer(100,this);
        boucledujeu.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //Grid 
        for (int i=0;i< boardWidth/tailleduncarreau;i++){
            g.drawLine(i*tailleduncarreau,0,i*tailleduncarreau,boardWidth);//ligne verticale du cadrillage espacé de 5
            g.drawLine(0,i*tailleduncarreau,boardWidth,i*tailleduncarreau);//ligne horizontale du cadrillage espacé de 5 
        }
        //nourriture: dessinde la nourriture sur le graphique 
        g.setColor(Color.red);
        g.fillRect(nourriture.x*tailleduncarreau,nourriture.y*tailleduncarreau,tailleduncarreau,tailleduncarreau);
        //Tête du Serpent: dessin du serpent sur le grapique 
        g.setColor(Color.green);
        g.fillRect(têteduserpent.x*tailleduncarreau, têteduserpent.y*tailleduncarreau, tailleduncarreau, tailleduncarreau);//la multiplication permet de modifier l'emplacement de notre point vert
         
        //corps du serpent
        for (int i=0; i<corpsduserpent.size(); i++){
            Tile partiduserpent= corpsduserpent.get(i);
            g.fillRect(partiduserpent.x*tailleduncarreau, partiduserpent.y*tailleduncarreau, tailleduncarreau, tailleduncarreau);
        }
    }
    public void placeNourriture(){
        nourriture.x=random.nextInt(boardWidth/tailleduncarreau);//valeur de  x qui est la position sur l'axe des abscisses compris entre [0,24]
        nourriture.y=random.nextInt(boardHeight/tailleduncarreau);//valeur de  y qui est la position sur l'axe des ordonnées compris entre [0,24], x est y sont pris aléatoirement entre 0 et 24
    }
    public boolean collision(Tile carreau1,Tile carreau2 ){
        return carreau1.x==carreau2.x&& carreau1.y==carreau2.y ;//pour la collision il vérifie la position de la tête du serpent et du point rouge
    }
    public void sedéplacer(){
        //pour manger de le point rouge
        if(collision(têteduserpent, nourriture)){
            corpsduserpent.add(new Tile(nourriture.x, nourriture.y));
            placeNourriture();  
        }

        //corps du serpent 
        for(int i=corpsduserpent.size()-1;i>=0;i--){
            Tile partiduserpent=corpsduserpent.get(i);
            if(i==0){
                partiduserpent.x=têteduserpent.x;
                partiduserpent.y=têteduserpent.y;
            }
            else{
                Tile prevpartiduserpent=corpsduserpent.get(i-1);
                partiduserpent.x=prevpartiduserpent.x;
                partiduserpent.y=prevpartiduserpent.y;
            }
        }
        
        //Snake head 
        têteduserpent.x +=vélocitéX;
        têteduserpent.y +=vélocitéY;
          //game over condition
        for(int i=0;i<corpsduserpent.size();i++){
            Tile partiduserpent = corpsduserpent.get(i);
            if (collision(têteduserpent,partiduserpent)){
                gameOver=true;
            }
        }
        if (têteduserpent.x<0){
           têteduserpent.x=24;
        }
        if(têteduserpent.x>24){
            têteduserpent.x=0;
        }
        if (têteduserpent.y<0){
            têteduserpent.y=24;
        }
        if (têteduserpent.y>24){
            têteduserpent.y=0;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sedéplacer();
        repaint();//va être appelé toutes les millisecondes par le programme va faire changer de couleur les différentes cases:vert->noir,noir->vert,vert->rouge,noir->rouge
        if (gameOver){
            boucledujeu.stop();
        }
         }
   //à titre d'informatique sur le graphique l'extrimité en haut à gauche à pour coordonnées (0px,0px), et les coordonnées pour le cointoutes en bas à droite(600px,600px)
  @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_UP && vélocitéY!=1){//indique qu'on a appuyé sur la flêche du haut pour se déplacer, vas déplacer le serpentvert le haut, la touche fonctionne ssi le serpent ne va pas vers le bas 
        vélocitéX= 0;
        vélocitéY=-1;
        }
        else if (e.getKeyCode()==KeyEvent.VK_DOWN && vélocitéY!=-1){//indique qu'on a appuyé sur la flêche du bas pour se déplacer vas déplacer le serpent le bas, la touche fonctionne ssi le serpent ne va pas vers le haut 
        vélocitéX= 0;
        vélocitéY=1;
        }
        else if (e.getKeyCode()==KeyEvent.VK_LEFT && vélocitéX!=1){//indique qu'on a appuyé sur la flêche de gauche pour se déplacer vas déplacer le serpent le gauche, la touche fonctionne ssi le serpent ne va pas vers la droite
        vélocitéX= -1;
        vélocitéY=0;
        }
        else if (e.getKeyCode()==KeyEvent.VK_RIGHT && vélocitéX!=-1){//indique qu'on a appuyé sur la flêche de droite pour se déplacer vas déplacer le serpent la droite, la touche fonctionne ssi le serpent ne va pas vers la gauche 
        vélocitéX= 1;
        vélocitéY=0;
        }
    }

    //inutile
   @Override
    public void keyTyped(KeyEvent e) {}

    
    @Override
    public void keyReleased(KeyEvent e) {}

}