//import statements
import java.awt.event.*;
import javax.swing.*;
public static void main(String args[]){

/*class for creating a button that will contain a price to purchase it as well as the number of that item that is owned and the
earnings for each one.*/
class Purchase extends JButton{

    private int price;
    private int earn;
    boolean visable= false;
    int num=0;
    JButton button;

    Purchase(){
        button= new JButton("Default");
        price=10;
        earn=1;
    }
    Purchase(String name, int price){
        this.price=price;
        earn=1;
    }
    public int getEarn(){
        return earn;
    }
    public void addNum(){
        num+=1;

    }
    public int getNum(){
        return num;
    }
    public int getPrice(){
        return price;
    }

}
class Game extends JFrame{
    static private int wallet, income;
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
        super("Game");

    }
    Game(String name){
        wallet=0;
        income=0;
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
    
}
/*
Creates player window
From this window, the player can view their money, damage, as well as any upgrades they might want to purchase

*/
class Player extends Game implements Game.Update{
    JLabel wal,inc;
    Player(){
        this.setSize(300,500);
        wal=new JLabel("Wallet: " + this.getWallet());
        inc= new JLabel("Income: "+this.getIncome());
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
    private int odds,mult,damage,total_damage, money;
    int hp;
    JLabel health,wal;
    JButton rock,paper,scissors;
    Enemy en;
    /*Enemy class
    creates a base class for enemies
    subclasses include miniboss and boss, which each modify the earnings and health
    */
    class Enemy{
        static protected int enemy_kill=0,boss_kill=0,miniboss_kill=0,earn=0,hp=0;
        Enemy(){
            hp=enemy_kill+(5*miniboss_kill)+(10*boss_kill)+1;
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
        public void setHP(){
            hp=enemy_kill+(5*miniboss_kill)+(10*boss_kill)+1;
        }
        public void dealDamage(int dam){
            hp=hp-dam;
        }
        public int getEarn(){
            return earn;
        }
        public void setEarn(){
            earn=enemy_kill+1;
        }
        public int getKill(){
            return enemy_kill;
        }
    
    }
    //miniboss and boss classes override the setHP, setEarn, and addEnemyKill methods as each one has a different outcome than just an enemy
    class MiniBoss extends Enemy{
        MiniBoss(){
            super();
        }
        @Override
        public void addEnemyKill(){
            miniboss_kill+=1;
        }
        @Override
        public void setEarn(){
            earn=earn*5;
        }
        @Override
        public void setHP(){
            hp=hp*10;
        }

    }
    class Boss extends Enemy{
        Boss(){
            super();
        }
        @Override
        public void addEnemyKill(){
            boss_kill+=1;
        }
        @Override
        public void setEarn(){
            earn=earn*100;
        }
        @Override
        public void setHP(){
            hp=hp*100;
        }
    }




    MiniGame(){
        money=10;
        odds=33;
        super("Minigame");
        en= new Enemy();
        wal = new JLabel("Wallet: "+this.getWallet());
        health = new JLabel("Health: " + en.getHP());
        mult=1;
        damage=mult;
        total_damage=damage;
        wal.setBounds(0,0,20,5);
        this.setSize(300,500);
        rock=new JButton("Rock");
        paper=new JButton("Paper");
        scissors=new JButton("Scissors");
        rock.setBounds(0, 350, 100, 100);
        paper.setBounds(100,350,100,100);
        scissors.setBounds(200,350,100,100);
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
    public void addDamage(int add){
        damage+=add;
        total_damage=(damage)*mult;
    }
    public void addMult(int mult){
        this.mult+=mult;
        total_damage=(damage)*(this.mult);
    }
    public void multMult(int mult){
        this.mult=this.mult*mult;
        total_damage=damage*mult;
    }
    private void dealDamage(){

        en.dealDamage(total_damage);
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

            en.setEarn();
            en.setHP();
            health.setText("Health: " + en.getHP());

        }
        
    }
    public int getOdds(){
        return odds;
    }
    public void setOdds(int odds){
        this.odds=odds;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        int roll=(int)(Math.random()*(100-1)+1);
            if(odds<=roll){
                dealDamage();
            }        
    }
    
    
    
} //implement an enemy, player, and button class
    MiniGame click= new MiniGame();
    click.setVisible(true);
    Player player = new Player();
    player.setVisible(true);
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
