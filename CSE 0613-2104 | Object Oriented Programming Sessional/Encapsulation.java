class Atm {
    // attributes
    private int card_number = 1;
    private int balance = 1000;

    // functions
    public void withdraw(int amount){
        if (amount > balance){
            System.out.println("exceed the limit");
        }
        else{
            balance = balance - amount;
        }
    }

    public void display() {
        System.out.println("card number: " + card_number);
        System.out.println("blance: " + balance);
    }
}

public class Encapsulation {
    public static void main(String[] args) {
        Atm u1 = new Atm();
        u1.display();
        System.out.println("==========");
        u1.withdraw(5000);
        u1.display();

        // u1.display();
        // u2.display();
    }
}

/*
functions/methods
access modifiers
    1.public
    2.private
    3.protected
    4.default value
Encapsulation
    1. Data Validation
*/
