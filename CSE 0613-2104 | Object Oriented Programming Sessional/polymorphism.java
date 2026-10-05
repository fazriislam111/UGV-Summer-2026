abstract class ugv{
    abstract void security();

    void accounts(){
        System.out.println("accounts section");
    }

    void bba(){
        System.out.println("bba section");
    }

    void display(){
        System.out.println("ugv");
    }
}

class cse extends ugv{

    void security(){
        System.out.println("cse security");
    }


    void display(){
        System.out.println("cse department");
    }
}


public class polymorphism {
    public static void main(String[] args) {
        cse s1 = new cse();
        s1.accounts();
    }
}

// inheritance
// polymorphism
// 1. Overriding
// 2. Overloading
// abstruction

// tasks
// types of inheritance
