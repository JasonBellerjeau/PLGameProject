//import statements
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;
public static void main(String args[]){

/*The game class is what each window will be refering to for its basic structure
it has 2 static variables that are shared across all game instances as these are the 2 variables that need to be shared
across the subclasses
*/

class Game extends JFrame{
    static private int wallet, income, damage, mult, total_damage;
    public interface Update{
        void change();
    }
    //stores listeners so that variables update across all windows
    static private java.util.List<Update> listeners = new java.util.ArrayList<>();

    public static void addListener(Update listener) {
        listeners.add(listener);
    }

    private static void notifyListeners() {
        for (Update listener : listeners) {
            listener.change();
        }
    }
    Game(){
        
        wallet=0;
        income=0;
        mult=1;
        damage=1;
        total_damage=1;
        super("Game");

    }
    Game(String name){
        wallet=0;
        income=0;
        mult=1;
        damage=1;
        total_damage=1;
        super(name);
    
    }
    public void addWallet(int earn){
        wallet+=earn;
        notifyListeners();
    }
    public void setWallet(int val){
        wallet=val;
        notifyListeners();
    }
    public int getWallet(){
        return wallet;
    }
    public int getIncome(){
        return income;
    }
    public void setIncome(int in){
        income=in;
        notifyListeners();
    }
    public void addIncome(int in){
        income=income+in;
        notifyListeners();
    }
    //adds damage to the existing damage
    public void addDamage(int add){
        damage+=add;
        total_damage=(damage)*mult;
    }
    //adds a multiplier to the existing one
    public void addMult(int mult){
        Game.mult+=mult;
        total_damage=(damage)*(Game.mult);
    }
    //allows for multipliers to be multiplicative
    public void multMult(int mult){
        Game.mult=Game.mult*mult;
        total_damage=damage*mult;
    }
    
}

class Player extends Game implements Game.Update{
    JLabel wal,inc;
    Player(){
        super("Player");
        //sets size of the window
        this.setSize(300,500);
        wal=new JLabel("Wallet: " + this.getWallet());
        inc= new JLabel("Income: "+this.getIncome());
        //sets the size and position of text
        wal.setBounds(0,30,100,30);
        inc.setBounds(0,0,100,30);
        this.add(wal);
        this.add(inc);
        Game.addListener(this);
    }
    @Override
    public void change(){
        wal.setText("Wallet: " + this.getWallet());
        inc.setText("Income: "+this.getIncome());
    }
    
}


/*Creates the minigame window
the minigame window is a game of rock paper scissors and allows the player to make money at the very begining
will contain classes for enemies that grant differing amounts of money*/ 
class MiniGame extends Game implements ActionListener{
    private int odds, money,hp;
    
    JLabel health,wal;
    JButton rock,paper,scissors;
    //instance of the enemy class that is able to change between the different sub and parent classes based on the number of kills the player has
    Enemy en;
    /*Enemy class
    creates a base class for enemies
    subclasses include miniboss and boss, which each modify the earnings and health
    will also include an odds modifier that will determine the difficulty of playing
    */
    class Enemy{
        static protected int enemy_kill=0,boss_kill=0,miniboss_kill=0,earn=0,hp=0;
        Enemy(){
            //the health and amount earned from a normal enemy
            hp=enemy_kill+(5*miniboss_kill)+(10*boss_kill)+1;
            System.out.println("set1");
            earn=enemy_kill+1;
        }
        public void addEnemyKill(){
            enemy_kill+=1;
        }
       /* public void addMiniBossKill(){
            miniboss_kill+=1;
        }
        public void addBossKill(){
            boss_kill+=1;
        }*/
        public int getHP(){
            return hp;
        }
        
        public void dealDamage(int dam){
            hp=hp-dam;
        }
        public int getEarn(){
            return earn;
        }
        public void setEarn(int x){
            earn=x;
        }
        public int getKill(){
            return enemy_kill;
        }
    
    }
    //miniboss and boss classes overrides the addEnemyKill method as each type of enemy killed is tracked seperately
    class MiniBoss extends Enemy{
        MiniBoss(){
            super();
            hp=hp*10;
            earn=earn*5;
        }
        @Override
        public void addEnemyKill(){
            miniboss_kill+=1;
        }
        

    }
    class Boss extends Enemy{
        Boss(){
            super();
            hp=hp*100;
            earn=earn*100;
        }
        @Override
        public void addEnemyKill(){
            boss_kill+=1;
        }
        
    }




    MiniGame(){
        //sets up the space for the minigame as well as positioning of the buttons
        odds=33;
        super("Minigame");
        en= new Enemy();
        wal = new JLabel("Wallet: "+this.getWallet());
        health = new JLabel("Health: " + en.getHP());
        
        wal.setBounds(0,0,20,5);
        this.setSize(300,500);
        rock=new JButton("Rock");
        paper=new JButton("Paper");
        scissors=new JButton("Scissors");
        rock.setBounds(0, 350, 100, 100);
        paper.setBounds(100,350,100,100);
        scissors.setBounds(200,350,100,100);
        //allows buttons to react to pressing
        rock.addActionListener(this);
        paper.addActionListener(this);
        scissors.addActionListener(this);
        health.setBounds(0,0,100,30);
        wal.setBounds(0,100,100,30);
        this.add(rock);
        this.add(paper);
        this.add(scissors);
        this.add(health);
        this.add(wal);
        
    }
    /*adds damage to the existing damage
    public void addDamage(int add){
        damage+=add;
        total_damage=(damage)*mult;
    }
    //adds a multiplier to the existing one
    public void addMult(int mult){
        this.mult+=mult;
        total_damage=(damage)*(this.mult);
    }
    //allows for multipliers to be multiplicative
    public void multMult(int mult){
        this.mult=this.mult*mult;
        total_damage=damage*mult;
    }*/
    private void dealDamage(){

        en.dealDamage(Game.total_damage);
        health.setText("Health: "+en.getHP());
        if(en.getHP()<=0){
            
            this.addWallet(en.getEarn());
            
            wal.setText("Wallet: " + this.getWallet());
            en.addEnemyKill();
            //a miniboss spawns every 5th enemy and the boss spawns every 10th
            if(en.getKill()%8==0){
                en = new Boss();
            }
            else if(en.getKill()%4==0){
                en = new MiniBoss();
            }
            else{
                en=new Enemy();
            }

            //en.setEarn();
            //en.setHP();
            health.setText("Health: " + en.getHP());

        }
        
    }
    //modidifies and retrieves the odds to be manipulated
    public int getOdds(){
        return odds;
    }
    public void setOdds(int odds){
        this.odds=odds;
    }
    //odds will vary depending on the enemy type (currently magic numbers but will be based on base number and modifiers purchased)
    //maybe have an odds set for each item (rock, paper and scissors)
    @Override
    public void actionPerformed(ActionEvent e) {
        if(en instanceof Enemy){
            this.setOdds(33);
        }
        else if(en instanceof MiniBoss){
            this.setOdds(18);
        }
        else if(en instanceof Boss){
            this.setOdds(9);
        }
        int roll=(int)(Math.random()*(100-1)+1);
            if(odds>=roll){
                dealDamage();
            }        
    }
    
    
    
} 
/*
Creates player window
From this window, the player can view their money, damage, as well as any upgrades they might want to purchase
Will inherit from the minigame class as this class contains the damage modifier
It will create a set of 10 buttons, each one with a specific cost that increases the damage of the player
When one is purchased, a new one is added with price adjusted according to what has been purchased
so far, each upgrade increases damage by 1
*/
class Purchase extends Game implements ActionListener{
    private int price;
    private JButton[] buy={null,null,null,null,null,null,null,null,null,null};
    Purchase(){

        price=1;
        super("Purchase");
        this.setSize(500, 100);
        this.add(new JButton("gup"));
        setLayout(new FlowLayout());
        for (int i = 0; i < 10; i++) {
            buy[i]= new JButton("Price: " + price);
            buy[i].addActionListener(this);
            price+=price;
            this.add(buy[i]);
        } 
    }
    private void organize(){
        for (int i = 0; i < 9; i++) {
            buy[i]=buy[i+1];
        }
    }
    public void bought(){
        //add way for each button to track its own price
        if(price<=getWallet()){
            this.remove(buy[0]);
            this.organize();
            buy[9]=new JButton("Price: " + price);
            buy[9].addActionListener(this);
            this.add(buy[9]);
            this.setPrice();
            this.addDamage(1);
            //System.out.println("gra");
            this.revalidate();
            this.repaint();
        }
        else{
            System.out.println("Unable to purchase item");
        }
    }
    public void setPrice(){
        price+=price;
    }
    public int getPrice(){
        return price;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        bought();
        
    }

}
//initialize an enemy, player, and purchase class
    MiniGame click= new MiniGame();
    click.setVisible(true);
    Player player = new Player();
    player.setVisible(true);
    Purchase purchase = new Purchase();
    purchase.setVisible(true);
    /*JButton rock = new JButton("Rock");
    JButton paper = new JButton("Paper");
    JButton scissors = new JButton("Scissor");*/
    
    /*JFrame mini = new JFrame("mini");
    JFrame frame = new JFrame("Tycoon");
    frame.setSize(1000, 500);
    mini.setSize(300,500);
    /*rock.setBounds(0, 350, 100, 100);
    paper.setBounds(100,350,100,100);
    scissors.setBounds(200,350,100,100);*/
    /*health.setBounds(0,0,100,10);
    /*mini.add(rock);
    mini.add(paper);
    mini.add(scissors);*/
    /*mini.add(health);
    mini.setVisible(true);
    frame.setVisible(true);
    /*rock.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            int roll=(int)(Math.random()*(2)+1);
            if(roll==1){
                health.setText("Health: ");
            }
        }
    });
    /*Purchase programmer = new Purchase("Programmers", 10);
    programmer.setBounds(0,0,200,100);
    JLabel wallet = new JLabel("Money: "+programmer.bank);
    frame.add(programmer);
    
    programmer.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            if(programmer.bank>=programmer.getPrice()){
                programmer.bank-=programmer.getPrice();
                programmer.addNum();
                wallet.setText("Money: "+programmer.bank);
            }
            else{
                System.out.println("Cannot purchase this");
            }
        }
        });
    Timer tick = new Timer(1000, new ActionListener(){
        public void actionPerformed(ActionEvent e){
            programmer.bank+=programmer.income;
            
            wallet.setText("Money: "+programmer.bank);
        }
    });
    tick.start();
    frame.add(wallet);
    frame.setVisible(true);
    /*JFrame frame = new JFrame("Ex");
    JButton button = new JButton("Click");
    frame.add(button);
    frame.setVisible(true);
    frame.setBounds(0, 0, 1000, 500);
    button.setBounds(0, 0, 100, 50);
    */
}
