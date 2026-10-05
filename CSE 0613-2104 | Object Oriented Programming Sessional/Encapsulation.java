class UGV{
    String department;
    String building;
}


class Student extends UGV{
    // variables/attributes
    int id;
    String name;

    // functions/methods
    void display(){
        System.out.println("Student ID: "+ id);
        System.out.println("Student Name: "+ name);
        System.out.println("Student Department: "+ department);
        System.out.println("Building: "+ building);
    }
}

public class Encapsulation {
    public static void main(String[] args) {
        Student s1 = new Student();
        s1.id = 1;
        s1.name = "Mr S";
        s1.department = "CSE";
        s1.building = "B2";
        
        s1.display();

        System.out.println("--------------------------------");
        Student s2 = new Student();
        s2.id = 2;
        s2.name = "Mr S2222";
        s2.department = "BBA";
        s2.building = "B1";
        s2.display();;
    }
}

// class
// functions

// Access Modifiers
    // private
    // public
    // protected
    // default value

// encapsulation
    // data validation



// balance --> private
// display --> public
// balance --> private

// user --> display