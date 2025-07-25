//import statements
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;
public static void main(String args[]){

/*The game class is what each window will be refering to for its basic structure
has static variables that are shared through all derived classes and can be modified through purchasing upgrades in the Purchase class
*/

class Game extends JFrame{
    static private int wallet, income, damage, mult, total_damage, odds_mod;
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
        odds_mod=0;
        super("Game");

    }
    Game(String name){
        wallet=0;
        income=0;
        mult=1;
        damage=1;
        total_damage=1;
        odds_mod=0;
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
        notifyListeners();
    }
    //adds a multiplier to the existing one
    public void addMult(int mult){
        Game.mult+=mult;
        total_damage=(damage)*(Game.mult);
        System.out.println(Game.mult);
        notifyListeners();
    }
    //allows for multipliers to be multiplicative
    public void multMult(int mult){
        Game.mult=Game.mult*mult;
        total_damage=damage*mult;
    }
    public int getDamage(){
        return total_damage;
    }
    public void addOddsMod(int odd){
        odds_mod+=odd;
        notifyListeners();
    }
    public int getOddsMod(){
        return odds_mod;
    }
    
}
/*
Creates a player window
From this window, the user can view important information such as their wallet, income, odds and total damage output
*/
class Player extends Game implements Game.Update{
    //labels for wallet, income, odds, and damage;
    JLabel wal,inc,odm,dam;
    Player(){
        super("Player");
        //sets size of the window
        this.setSize(300,500);
        wal=new JLabel("Wallet: " + this.getWallet());
        inc= new JLabel("Income: "+this.getIncome());
        odm= new JLabel("Odds Modifier: "+this.getOddsMod());
        dam= new JLabel("Total Damage: "+this.getDamage());
        //sets the size and position of text
        wal.setBounds(0,0,100,30);
        inc.setBounds(0,30,100,30);
        odm.setBounds(0,60,100,30);
        dam.setBounds(0,90,100,30);
        this.add(wal);
        this.add(inc);
        this.add(odm);
        this.add(dam);
        Game.addListener(this);
    }
    @Override
    public void change(){
        wal.setText("Wallet: " + this.getWallet());
        inc.setText("Income: "+this.getIncome());
        odm.setText("Odds Modifier: "+this.getOddsMod());
        dam.setText("Total Damage: "+this.getDamage());
    }
    
}


/*Creates the minigame window
the minigame window is a game of rock paper scissors and allows the player to make money at the very begining
will contain classes for enemies that grant differing amounts of money*/ 
class MiniGame extends Game implements ActionListener, Game.Update{
    private int odds, money,hp;
    
    JLabel health,wal,dam;
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
            System.out.println("Normal Enemy kills: "+enemy_kill);
            System.out.println("Miniboss kills: "+miniboss_kill);
            System.out.println("Boss kills: "+boss_kill);
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
        //returns total number of kills across all enemy types
        public int getKill(){
            return enemy_kill+miniboss_kill+boss_kill;
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
        super("Minigame");
        odds=33+this.getOddsMod();
        en= new Enemy();
        wal = new JLabel("Wallet: "+this.getWallet());
        health = new JLabel("Health: " + en.getHP());
        dam= new JLabel("Damage: "+ this.getDamage());
        this.setSize(450,500);
        wal.setBounds(0,0,20,5);
        rock=new JButton("Rock odds: "+ this.getOdds());
        paper=new JButton("Paper odds: "+ this.getOdds());
        scissors=new JButton("Scissors odds: "+ this.getOdds());
        rock.setBounds(0, 350, 150, 100);
        paper.setBounds(150,350,150,100);
        scissors.setBounds(300,350,150,100);
        //allows buttons to react to pressing
        rock.addActionListener(this);
        paper.addActionListener(this);
        scissors.addActionListener(this);
        health.setBounds(0,0,100,30);
        wal.setBounds(0,50,100,30);
        dam.setBounds(0,100,100,30);
        this.add(rock);
        this.add(paper);
        this.add(scissors);
        this.add(health);
        this.add(wal);
        this.add(dam);
        Game.addListener(this);
        
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

        en.dealDamage(this.getDamage());
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
            //sets text
            rock.setText("Rock odds: "+this.getOdds());
            paper.setText("Paper odds: "+this.getOdds());
            scissors.setText("Sissors odds: "+this.getOdds());
            //en.setEarn();
            //en.setHP();
            health.setText("Health: " + en.getHP());

        }
        
    }
    //modidifies and retrieves the odds to be manipulated depending on the type of enemy
    public int getOdds(){
        if(en instanceof Boss){
            this.setOdds(9+this.getOddsMod());
        }
        else if(en instanceof MiniBoss){
            this.setOdds(18+this.getOddsMod());
        }
        else if(en instanceof Enemy){
            this.setOdds(33+this.getOddsMod());
        }
        return odds;
    }
    public void setOdds(int odds){
        this.odds=odds;
    }
    //odds will vary depending on the enemy type (currently magic numbers but will be based on base number and modifiers purchased)
    //maybe have an odds set for each item (rock, paper and scissors)
    @Override
    public void actionPerformed(ActionEvent e) {
        int roll=(int)(Math.random()*(100-1)+1);
            if(odds>=roll){
                dealDamage();
            }        
    }
    //overides change to allow damage and wallet to change depending on what is purchased outside of this window
    //as well as update the odds on the rock, paper, and scissors buttons
    @Override
    public void change(){
        System.out.println(this.getOdds());
        dam.setText("Damage: "+ this.getDamage());
        wal.setText("Wallet: "+this.getWallet());
        rock.setText("Rock odds: "+this.getOdds());
        paper.setText("Paper odds: "+this.getOdds());
        scissors.setText("Sissors odds: "+this.getOdds());
    }
    
    
} 
/*
Creates buy class (extends button)
Each class will have a type of modifier (damage, income, mult, etc) and the price for it.
Price increasing will be handled in each window depending on the actions performed in the window
*/
class Buy extends JButton{
    private final int price;
    private int num;

    //name is the name of the button, p is the price of the button and num is the ammount to increase once the button is pushed
    Buy(String name, int p,int num){
        super(name+": "+p);
        price=p;
        this.num=num;
        
        
    }
    public int getPrice(){
        return price;
    }

    
    public int getNum(){
        return num;
    }
}
/*
Creates damage window
It will create a set of 5 buttons, each one with a specific cost that increases the damage of the player
When one is purchased, a new one is added with price adjusted according to what has been purchased
so far, each upgrade increases damage by 1
*/
class Purchase extends Game{
    private String name;
    private int price,num;
    private char c;
    private Buy inc;
    private Buy[] tot={null,null,null,null,null};
    //Default constructor for the purchase window
    Purchase(){
        name="Purchase";
        price=1;
        c='d';
        num=1;
        super("Purchase");
        this.setSize(500, 100);
        this.setLayout(new FlowLayout());
        //creates 5 buttons and sets their price and creates a action listener per button
        for(int i=0; i<5; i++){
            addButton(i,price);
        }
        
    }
    //Constructor for declaring a specific purchase window with specific start price
    //name and c are identifiers of what the window will be and how the buttons will modify the players statistics
    //initial_price is the starting price of all the upgrades
    //num is the number that the statistic will increase by once purchased
    Purchase(String name, char c, int initial_price, int num){
        this.name=name;
        this.c=c;
        this.num=num;
        price=initial_price;
        super(name);
        this.setSize(500, 100);
        this.setLayout(new FlowLayout());
        //creates 5 buttons and sets their price and creates a action listener per button
        for(int i=0; i<5; i++){
            addButton(i,price);
        }
    }

    //organizes all buttons in the list
    private void organize(){
        //reorganizes the list and its indices
        for(int i=0; i<5;i++){
            final int index=i;
            this.remove(tot[i]);
            addButton(index,tot[i].getPrice());
        }

    }
    private void bought(int i){
        for(int j=i; j<4;j++){
            tot[j]=tot[j+1];
        }
        addButton(4,price);
    }
    public int getPrice(){
        return price;
    }
    //logic for adding a button
    //p is the index of the button in the list and pric is the price of the button
    private void addButton(int p,int pric){
        //pr is the price of the button, incex is the index of the button in the list
        final int pr= pric;
        final int index = p;
        //creates a button that will increase the defined stat based on the name variable
        inc=new Buy("Increase "+name+" by "+num,pr,num);
        tot[p]=inc;
        inc.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //allows purchase if it is able to be afforded
                System.out.println(index);
                 if(tot[index].getPrice()<=getWallet()){
                    addWallet(-1*tot[index].getPrice());
                    //modifies the game values depending on the value of c
                    if(c=='d'){
                        addDamage(tot[index].getNum());
                    }
                    else if(c=='m'){
                        addMult(tot[index].getNum());
                    }
                    else if(c=='o'){
                        addOddsMod(tot[index].getNum());
                    }
                    //removes the button at the index needed
                    Purchase.this.remove(tot[index]);
                    //reorganizes after removal
                    bought(index);
                    organize();
                    //removes and redraws the window if able to buy the upgrade
                    Purchase.this.revalidate();
                    Purchase.this.repaint();
                    }


                }
            });
            this.add(inc);
            price+=price;
    }

    

}
/*Creates a window, Income, for upgrading the income of the user
Is not included in the Purchase class as only one button will be available and will change depending on the number of times
it has been purchased
Income is earned every second that passes after purchasing the first income upgrade is purchased
*/
class Income extends Game{
    Timer tick;
    Buy inc;
    private int price,increase;
    private int done=0;
    //default constructor
    Income(){
        super("Income");
        tick=new Timer(1000,new ActionListener(){
            public void actionPerformed(ActionEvent e){
                addWallet(getIncome());
            }
        });
        inc=new Buy("Increase income by 1",1,1);
        inc.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(getWallet()>=price){
                    addIncome(inc.getNum());
                    clicked();
                }
            }
        });
        this.setSize(500,100);
        this.setLayout(new FlowLayout());
        inc.setSize(100,50);
        this.add(inc);
    }
    //takes in a variable for the initial price and for how much the income will increase by per second
    Income(int price, int increase){
        super("Income");
        tick=new Timer(1000,new ActionListener(){
            public void actionPerformed(ActionEvent e){
                addWallet(getIncome());
            }
        });
        this.increase=increase;
        this.price=price;
        inc=new Buy("Increase income by "+this.increase,this.price,this.increase);
        inc.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(getWallet()>=price){
                    addIncome(inc.getNum());
                    clicked();
                }
            }
        });
        this.setSize(500,100);
        this.setLayout(new FlowLayout());
        inc.setSize(100,50);
        this.add(inc);
    }
    private void clicked(){
        if(done==0){
            done=1;
            tick.start();
        }
        price+=price;
        increase+=1;
        this.remove(inc);
        inc=new Buy("Increase income by " +increase,price,increase);
        inc.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(getWallet()>=price){
                    addIncome(inc.getNum());
                    clicked();
                }
            }
        });
        this.add(inc);
        this.revalidate();
        this.repaint();
    }

}
//initialize an enemy, player, and purchase class
    MiniGame click= new MiniGame();
    click.setVisible(true);
    Player player = new Player();
    player.setVisible(true);
    //initial price of damage is 1
    Purchase damage = new Purchase("Damage",'d',1,1);
    damage.setVisible(true);
    //initial price of the multipliers is 5
    Purchase multiplier = new Purchase("Multiplier",'m',5,1);
    multiplier.setVisible(true);
    Purchase oddPurchase = new Purchase("Odds",'o',10,2);
    oddPurchase.setVisible(true);
    Income incomes = new Income(20,1);
    incomes.setVisible(true);
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
