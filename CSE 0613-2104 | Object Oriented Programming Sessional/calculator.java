import java.util.Scanner;

public class calculator {

    public static double add(){
        Scanner input2 = new Scanner(System.in);
        System.out.println("How many numbers do you want to add?");
        int n = input2.nextInt();

        double sum = 0;
        for (int i = 0; i < n; i++){
            double value = input2.nextDouble();
            sum = sum + value;
        }

        return sum;


    }
    public static double sub(double n1, double n2){
        return n1-n2;
    }


    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        
        
        while (true){
            System.out.println("============Menu==============");
            System.out.println("1. Addition");
            System.out.println("2. Subtraction");
            System.out.println("3. Multiplication");
            System.out.println("4. Divition");
            System.out.println("0. Exit");
            System.out.println("===============================");

            System.out.println("Input your choice: ");
            int choice = input.nextInt();
            
            if (choice == 0){
                System.out.println("Calculation Completed.");
                break;
            }

            switch (choice){
                case 1:
                    {
                    double result = add();
                    System.out.println("Result of addition is: "+result);
                    break;}
                case 2:
                   { System.out.println("Input first number: ");
                    double n1 = input.nextDouble();
                    System.out.println("Input second number: ");
                    double n2 = input.nextDouble();
                    double result = sub(n1, n2);
                    System.out.println("Result of addition is: " + result);
                    break;}
                case 3:
                    System.out.println("Multiplication--");
                    break;
                case 4:
                    System.out.println("Divition--");
                    break;
                default:
                    System.out.println("Invalid Input");
            }
        }
    }
}
