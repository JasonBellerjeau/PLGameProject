//import button ussage
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

    Purchase(){
        super();
        price=10;
        earn=1;
    }
    Purchase(String name, int price){
        this.price=price;
        super(name);
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
class Player{
    int damage;
    int wallet;
    int income;
    Player(){
        damage=1;
        wallet=0;
        income=0;
    }

} //implement an enemy, player, and button class
    int hp=10;
    JLabel health = new JLabel("Health: "+hp);
    JButton rock = new JButton("Rock");
    JButton paper = new JButton("Paper");
    JButton scissors = new JButton("Scissor");
    JFrame mini = new JFrame("mini");
    JFrame frame = new JFrame("Tycoon");
    frame.setSize(1000, 500);
    mini.setSize(300,500);
    rock.setBounds(0, 350, 100, 100);
    paper.setBounds(100,350,100,100);
    scissors.setBounds(200,350,100,100);
    health.setBounds(0,0,100,10);
    mini.add(rock);
    mini.add(paper);
    mini.add(scissors);
    mini.add(health);
    mini.setVisible(true);
    frame.setVisible(true);
    rock.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            int roll=(int)(Math.random()*(2)+1);
            if(roll==1){
                health.setText("Health: "+hp--);
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
