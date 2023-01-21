import java.awt.*;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.*;
import java.util.Random;
import java.util.Vector;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.FixedHeightLayoutCache;

import java.sql.*;
	
public class Mygame extends JPanel implements Runnable {

    static Table t1 ;
    static JFrame f ;
    int NUMOFHAPPY = 5;
    int NUMOFSAD = 4;
    int i, j, x;
    int speed = 10;
    static int FWIDTH = 750, FHEIGHT = 725;
    boolean inside = false;
    Thread th;
    Graphics2D g;
    //creating objects
    Bowl bowl = new Bowl();
    Happy happy[] = new Happy[NUMOFHAPPY];
    Sad sad[] = new Sad[NUMOFSAD];
    SadMover sadMover[] = new SadMover[NUMOFSAD];
    HappyMover happyMover[] = new HappyMover[NUMOFHAPPY];


    //static Color c1 = Color.WHITE,c2 = Color.BLACK;
    static Color colorWhite = Color.WHITE;
    static String name ;

    public Mygame(){

        this.setBackground(Color.WHITE);
        //position of bowl
        bowl.setMx(30);
        bowl.setMy(200);

        for(i = 0;i < sad.length; ++i){
            sad[i] = new Sad();
            sad[i].setMy(100);
            sad[i].reset();
        }

        for(j = 0; j < sad.length; ++j){
            sadMover[j]= new SadMover(sad[j],bowl);
            sadMover[j].setInitialDelay((j + 1) * 1500);
            sadMover[j].move();

        }
        for(i = 0;i < happy.length; ++i){
            happy[i] = new Happy();
            happy[i].setMy(100);
            happy[i].reset();
        }

        for(j = 0; j < happy.length; ++j){
            happyMover[j]= new HappyMover(happy[j], bowl);
            happyMover[j].setInitialDelay((j + 1) * 1000);
            happyMover[j].move();

        }

        //This to control the bowl by using mouse
        MouseListener ml = new MouseAdapter(){     //to chack if the user clicks on the bowl
            public void mousePressed(MouseEvent me){
                x = (int)me.getPoint().getX();
                if(bowl.contains(me.getPoint())){
                    inside = true;
                }
            }
            public void mouseReleased(MouseEvent me){
                inside = false;
            }
        };
        addMouseListener(ml);

        MouseMotionListener mll = new MouseAdapter(){ //to move the bowl by using mouse
            public void mouseDragged(MouseEvent me){
                if(inside) {
                    bowl.setMx((int)me.getPoint().getX());
                }
            }
        };
        addMouseMotionListener(mll);

        th = new Thread(this);
        th.start();
    }

    public void paint(Graphics g2) {
//        System.out.println(getWidth()+" "+getHeight());
        Image enemy;
        enemy = new ImageIcon("C:/Users/DELL/Downloads/src/bg_vanam.jpg").getImage();
        Graphics2D g = (Graphics2D) g2;

        super.paint(g);
        //Image scaled = enemy.getScaledInstance(getWidth(), getHeight(),0);
        g.drawImage(enemy, 0, 0, getWidth(), getHeight(), this);


        //Making the Bowl's position lower.
        bowl.setMy(getHeight()-182);

        //drawing Bowl
        bowl.drawOn(g);

        //happy emoji
        for(int i = 0; i < happy.length; ++i){
            happy[i].paint(g);
        }

        //sad empji
        for(int i = 0; i < sad.length; ++i){
            sad[i].paint(g);
        }


    }


    public void run(){
        try{
            while(true)
            {
                th.sleep(5);
                if(bowl.life == 0) { //if the life becomes 0
                    for(int i = 0; i < happy.length; ++i){
                        happy[i].reset();
                        happyMover[i].stop(); //stop
                    }

                    for(int i = 0; i < sad.length; ++i){
                        sad[i].reset();
                        sadMover[i].stop();
                    }



                    JOptionPane.showMessageDialog(null, "Your Score Was " + bowl.score);
                    int a = JOptionPane.showConfirmDialog(null, "Restart The Game?", "Game Over", JOptionPane.OK_CANCEL_OPTION);

                    

                    t1.insertScore ( bowl.score ) ;
                    t1.display () ;

                    if(a == JOptionPane.OK_OPTION){ //if the user clicks ok for "restart the game?"
                        for(int i = 0; i < happy.length; ++i){   //happys move
                            happyMover[i].setInitialDelay((i + 1) * 3000); //each happy's delay
                            happyMover[i].move();
                        }
                        for(int j = 0; j < sad.length; ++j){   //sads move
                            sadMover[j].setInitialDelay((j + 1) * 1500);
                            sadMover[j].move();
                        }

                        bowl.life = 25;    //reset player's 
                        bowl.score = 0; // reset player's score
                    }
                    if(a == JOptionPane.CANCEL_OPTION){
                        System.exit(0);
                    }
                }
                repaint ();
            }
        }catch(Exception e)
        {

        }
     
    }

    public static void main(String args[]){


        JFrame jfm = new JFrame();
        jfm.setPreferredSize(new Dimension(100,150));
        jfm.setResizable(false);
        jfm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfm.setLayout(new BorderLayout());
        jfm.setTitle ( "Emoji Game" ) ;
        jfm.getContentPane().add(new Mygame());
        jfm.setBackground(Color.WHITE);

        f = new JFrame( "Start" );

       name = JOptionPane.showInputDialog ( f , "Enter Name ");

        f = new JFrame ( "LeaderBoard" ) ;
        f.setLocation ( 750 , 0 ) ;

        System.out.println ( "Table() Calling ..." ) ;
        t1 = new Table() ;
        t1.display () ;
        System.out.println ( "Table() Called ..." ) ;
        // if(a == JOptionPane.CANCEL_OPTION){
        //     System.exit(0);
        // }

        JLabel l= new JLabel("Welcome! Enjoy!");
        l.setFont( new Font(Font.SERIF, Font.PLAIN, 20));
        l.setBorder(BorderFactory.createTitledBorder(null, "Emoji Catching Game!", TitledBorder.CENTER,
        TitledBorder.ABOVE_TOP, new Font("Arial", 1, 22), Color.RED));
        jfm.add(l,BorderLayout.NORTH); //north means on the top
        
        final Mygame myGame = new Mygame();

        myGame.bowl.setOutputComponent(l);

        //keyboards
        KeyListener kl = new KeyAdapter(){
            public void keyPressed(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_RIGHT){
                    //if you press --> bowl goes to the right
                    int shift = ( myGame.bowl.X + 25 ) < 580 ? myGame.bowl.X + 25 : 580 ;
                    myGame.bowl.setMx( shift ) ;
                }
                if(ke.getKeyCode() == KeyEvent.VK_LEFT){
                    //if you press <-- bowl goes to the left
                    int shift = ( myGame.bowl.X - 25 ) > 0 ? myGame.bowl.X - 25 : 0 ;
                    myGame.bowl.setMx( shift ) ;
                }
            }
        };
        jfm.addKeyListener(kl);

        jfm.add(myGame);
        jfm.setSize(FWIDTH, FHEIGHT);
        jfm.setVisible(true);
        f.setVisible(true);
    }
}

class Happy extends JPanel{
    int X,Y;
    Rectangle r;

    String loc;
    static Random rn = new Random();

    Happy() {
        setOpaque(false);
        //creating an object
        String[]arr= new String[]{"C:/Users/DELL/Downloads/src//image1.png","C:/Users/DELL/Downloads/src//laugh.png","C:/Users/DELL/Downloads/src//heart.png","C:/Users/DELL/Downloads/src//celeb.png","C:/Users/DELL/Downloads/src//eyes.png"};
       Random r = new Random();
       int val=r.nextInt(5);
//       System.out.println(val);
       loc=arr[val];

    }

    //when bowl and rectangle intersects
    boolean fallsInBowl(Bowl b) {
        return r.intersects(b.r);
    }

    void setMx(int x) {
        if ( x < 620 ) X = x ;
        else X = 620 ;
    }

    void setMy(int y) {
        Y = y;
    }

    public void reset() {
        //height of happy's start position
        setMy(20);
        //width of happy's start points
        setMx(50+ rn.nextInt(Mygame.FWIDTH - 50)); //making happys start from different points
    }

    void paint(Graphics2D g) {
        Image enemy;
        enemy = new ImageIcon(loc).getImage();
        Graphics2D g2D = (Graphics2D) g;
        BufferedImage img= imageToBufferedImage(enemy);
        int color=img.getRGB(0,0);
        Image image= makeColorTransparent(img,new Color(color));
        g.drawImage(image, X, Y, null, this);


        r = new Rectangle(X, Y,50,50);

    }
    private static BufferedImage imageToBufferedImage(final Image image)
    {
        final BufferedImage bufferedImage =
                new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }
    public static Image makeColorTransparent(BufferedImage im, final Color color)
    {
        final ImageFilter filter = new RGBImageFilter()
        {
            // the color we are looking for (white)... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFFFFFFFF;

            public final int filterRGB(final int x, final int y, final int rgb)
            {
                if ((rgb | 0xFF000000) == markerRGB)
                {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                }
                else
                {
                    // nothing to do
                    return rgb;
                }
            }
        };

        final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    //creating random colors which are blue, green, or red for happys

}


class Sad extends Happy {
    //color of Sad

    int X,Y;
    Rectangle r;
    String loc;
    private Random myRand;

    static Random rn = new Random();

    public Sad() {
//        setOpaque(false);
        String[]arr= new String[]{"C:/Users/DELL/Downloads/src//angry.png","C:/Users/DELL/Downloads/src//sad.png","C:/Users/DELL/Downloads/src//cry.png","C:/Users/DELL/Downloads/src//sad2.png"};
        Random r = new Random();
        int val=r.nextInt(4);
        System.out.println(val);
        loc=arr[val];
    }

    //when sads and the bowl intersects
    boolean fallsInBowl(Bowl b){
        return r.intersects(b.r);
    }

    void setMx(int x){
        if ( x < 620 ) X = x ;
        else X = 620 ;
    }

    void setMy(int y){
        Y = y;
    }

    public void reset(){
        //sads' start points of height
        setMy(30);
        //sads' start points of width
        setMx(30 + rn.nextInt(Mygame.FWIDTH - 50)); //making sads start from different points
    }

    void paint(Graphics2D g) {
        Image enemy;
        enemy = new ImageIcon(loc).getImage();

        g.drawImage(enemy, X, Y, null, this);
//        setOpaque(false);

        r = new Rectangle(X, Y,50,50);

    }
    private static BufferedImage imageToBufferedImage(final Image image)
    {
        final BufferedImage bufferedImage =
                new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }
    public static Image makeColorTransparent(BufferedImage im, final Color color)
    {
        final ImageFilter filter = new RGBImageFilter()
        {
            // the color we are looking for (white)... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFFFFFFFF;

            public final int filterRGB(final int x, final int y, final int rgb)
            {
                if ((rgb | 0xFF000000) == markerRGB)
                {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                }
                else
                {
                    // nothing to do
                    return rgb;
                }
            }
        };

        final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}

class HappyMover{
    ActionListener al;
    Timer timer;
    Happy happy;
    Bowl bowl;
    boolean allowed = true;

    public HappyMover(Happy ba, Bowl bo){
        happy = ba;
        bowl = bo;
        al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                happy.setMy(happy.Y + 3); //to move the happys
                if(isAllowed()) {
                    if(happy.fallsInBowl(bowl)) { //if happys fit in the bowl
                        System.out.println ( "\n BALL Caught !" ) ;
                        int co_x = happy.X , co_y = happy.Y ;
                        happy.reset(); //happys reset
                        ToastMessage message = new ToastMessage( "+25" , co_x , co_y ) ;
                        message.display() ;
                        bowl.updateScoreForHappy(); //the user gets points
                    }
                }
                System.out.println ( "BALL info : " + happy.r.y + " + " + happy.r.height + " > " + bowl.r.y + " + " + bowl.r.height / 2 ) ;
                if ( happy.r.y + happy.r.height > bowl.r.y + bowl.r.height / 2 && !happy.fallsInBowl(bowl)){ //if happys go further down than the bowl position
                    System.out.println ( "BALL Fell Beyond Bowl !" ) ;
                    allowed = false; //player cannot catch happys
                    if(happy.r.y + happy.r.height >= 525){ //if the happys falls off farther than or equals to 525 which is the bottom of the screen
                        System.out.println ( "BALL Fell Beyond Screen !" ) ;
                        happy.reset(); //the happys' positions reset, then fall off again
                        bowl.life -= 1;  //player's life is deducted 1
                        bowl.updateLife(); //show current player's life
                    }
                } else {
                    allowed = true;
                }
            }
        };
        //the speed of Happys
        timer = new Timer(20, al);
    }
    void setInitialDelay(int i){
        timer.setInitialDelay(i);
    }

    boolean isAllowed(){
        return allowed;
    }

    void move(){
        timer.start();

    }

    void stop(){
        timer.stop();
    }
}

class SadMover {
    ActionListener al;
    Timer timer;
    Sad sad;
    Bowl bowl;
    boolean allowed = true;

    public SadMover(Sad s,Bowl b){
        sad = s;
        bowl = b;
        al = new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                sad.setMy(sad.Y + 3); //to move the sads
                if(isAllowed()){
                    if(sad.fallsInBowl(bowl)){ //if sads fit in the bowl
                        System.out.println ( "\n SQUARE Caught !" ) ;
                        int co_x = sad.X , co_y = sad.Y ;
                        sad.reset(); //sads reset
                        ToastMessage message = new ToastMessage( "-10" , co_x , co_y ) ;
                        message.display() ;
                        bowl.updateScoreForSad(); //the player gets points
                    }
                }
                System.out.println ( sad.r.y + " + " + sad.r.height + " > " + bowl.r.y + " + " + bowl.r.height / 2 ) ;
                if(sad.r.y + sad.r.height > bowl.r.y + bowl.r.height / 2 && !sad.fallsInBowl(bowl)){ //if sads go further down than the bowl position
                    System.out.println ( "SQUARE Fell Beyond Bowl !" ) ;
                    allowed = false; //the player cannot catch sads
                    if(sad.r.y + sad.r.height >= 525 ){ //if sads fall off farther than or equals to 525 which is the bottom of the screen
                        System.out.println ( "SQUARE Fell Beyond Screen !" ) ;
                        sad.reset();  //the sads' positions reset, then fall off again
                      //  bowl.life -= 1; //player's life is deducted 1
                       // bowl.updateLife(); //show current player's life
                    }
                } else {
                    allowed = true;
                }
            }
        };
        //speed of sads
        timer = new Timer(15, al);
    }
    void setInitialDelay(int i){
        timer.setInitialDelay(i);
    }

    boolean isAllowed(){
        return allowed;
    }

    void move(){
        timer.start();
    }

    void stop(){
        timer.stop();
    }
}




class Bowl extends JPanel{

    int X, Y;
    Rectangle r;
    JLabel l;

    int score = 0, life = 25;

    Bowl() {

    }

    void setMx(int dx) {
        if ( dx < 620 ) X = dx ;
        else X = 620 ;
    }

    void setOutputComponent(JLabel lb) {
        l = lb;
    }

    void updateScoreForSad() {
            l.setText("Score = " + (score -= 10) + "          Life = " + (life));
    }

    void updateScoreForHappy() {
            l.setText("Score = " + (score += 25) + "          Life = " + (life));
    }

    void updateLife() {
        l.setText("Score = " + (score += 0) + "          Life = " + (life));
    }

    void setMy(int dy) {
        Y = dy;
    }

    boolean contain(Point p) {
        return r.contains(p);
    }

    void drawOn(Graphics2D g) {
        Image enemy;
        enemy = new ImageIcon("C:/Users/DELL/Downloads/src//catch.png").getImage();
        Graphics2D g2D = (Graphics2D) g;
        BufferedImage img= imageToBufferedImage(enemy);
        int color=img.getRGB(0,0);
        Image image= makeColorTransparent(img,new Color(color));
        g.drawImage(image, X, Y, null, this);


       r = new Rectangle(X, Y,110,100);

    }
    private static BufferedImage imageToBufferedImage(final Image image)
    {
        final BufferedImage bufferedImage =
                new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }
    public static Image makeColorTransparent(BufferedImage im, final Color color)
    {
        final ImageFilter filter = new RGBImageFilter()
        {
            // the color we are looking for (white)... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFFFFFFFF;

            public final int filterRGB(final int x, final int y, final int rgb)
            {
                if ((rgb | 0xFF000000) == markerRGB)
                {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                }
                else
                {
                    // nothing to do
                    return rgb;
                }
            }
        };

        final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}

class Table extends Mygame
 {
     public Table() {}

     public void display ()
     {
        try {      
            System.out.println ( "\n BEFORE REGISTER !") ;
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println ( "\n REGISTERED !") ;
            Connection con=DriverManager.getConnection(  
            "jdbc:mysql://localhost:3306/catch_game","root","mysql");
                Statement stmt=con.createStatement();
                ResultSet res =stmt.executeQuery("select * from leaderboard order by score desc ; ");  
                ResultSetMetaData rsmd = res.getMetaData();
                int colcount = rsmd.getColumnCount();
                Vector columns = new Vector(colcount);
                    for(int i=1; i<=colcount; i++)
                {
                    columns.add( rsmd.getColumnName(i).toUpperCase () );
                }
                Vector data = new Vector();
                Vector row;
    
                // Store row data
                while(res.next())
                {
                    row = new Vector(colcount);
                    for(int i=1; i<=colcount; i++)
                    {
                        if ( i == 2 )
                            row.add ( res.getString(i) ) ;
                        else
                            row.add ( res.getInt (i) ) ;
                    }
                    data.add (row) ;
                }
    
                JTable table = new JTable(data, columns) {
                    public boolean editCellAt(int row, int column, java.util.EventObject e) {
                       return false;
                    }
                 };
    
                table.getTableHeader().setBackground( Color.yellow ) ;
                table.getTableHeader().setForeground ( Color.RED ) ;
                table.getTableHeader().setFont(new Font("", 1 , 25 ) ) ;
    
                table.setAutoResizeMode ( JTable.AUTO_RESIZE_LAST_COLUMN ) ;
                table.setGridColor ( Color.black ) ;
                table.setRowHeight ( 40 ) ;
                table.setRowMargin ( 3 ) ;
                table.setBackground ( Color.yellow ) ;
                table.setFont(new Font("", 4 , 25 ) );
                table.setForeground ( Color.BLUE ) ;
    
                DefaultTableCellRenderer defaultRenderer = (DefaultTableCellRenderer) table.getDefaultRenderer(Object.class);
                defaultRenderer.setHorizontalAlignment(JLabel.CENTER);
    
                JScrollPane sp = new JScrollPane (table) ;
                f.add (sp) ;
                f.setSize ( 500 , 700 ) ;
                f.setVisible ( true )
     ;
                }catch(Exception e){ System.out.println(e);}  
                    
     }

    public void insertScore ( int score )
    {  
        try{  
            System.out.println ( "\n BEFORE REGISTER !") ;
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println ( "\n REGISTERED !") ;
            Connection con=DriverManager.getConnection(  
            "jdbc:mysql://localhost:3306/catch_game","root","mysql");  
            System.out.println ( "\n CONNECTED to DB !") ;
            Statement stmt1=con.createStatement();
            ResultSet res1 = stmt1.executeQuery("select count(*)  from leaderboard");  
            res1.next () ;
            int sno  = res1.getInt(1);

            System.out.println ( "\n SCORE = " + score ) ;

            stmt1.executeUpdate("insert into leaderboard values ( " + (sno+1) + " , '" + name + "' , "+ score + ") ;");
        }catch ( Exception e ) {}
     }

}

class ToastMessage extends JFrame {
    int X , Y ;
    public ToastMessage(final String message , int x , int y ) {
       X = x ;
       Y = y + 65 ;
       setUndecorated(true);
       setLayout(new GridBagLayout());
       setBackground(new Color(0,0,0,0));
       setLocation( X , Y ) ;
       setSize(40, 40);
       JLabel msg = new JLabel(message) ;
       msg.setFont( new Font( "", Font.BOLD, 18));
       msg.setForeground(new Color(7, 57, 188));
       add( msg ) ;
        
       addComponentListener(new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
             setShape(new  RoundRectangle2D.Double(0,0,getWidth(),
             getHeight(), 20, 20));                      
          }
       });        
    }
 
    public void display() {
       try {
          setOpacity(1);
          setVisible(true);
 
          //hide the toast message in slow motion
          for (double d = 1.0; d > 0.2; d -= 0.2) {
            Thread.sleep(100);
            setLocation ( X , Y -= 5 ) ;
            setOpacity((float)d);
          }
 
          // set the visibility to false
          setVisible(false);
       }catch (Exception e) {
          System.out.println(e.getMessage());
       }
    }
 }