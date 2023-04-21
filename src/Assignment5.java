import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Assignment5 {
    public static void main(String[] args) {
        System.out.println("\n" +
                "░█████╗░░██████╗░██████╗██╗░██████╗░███╗░░██╗███╗░░░███╗███████╗███╗░░██╗████████╗  ███████╗\n" +
                "██╔══██╗██╔════╝██╔════╝██║██╔════╝░████╗░██║████╗░████║██╔════╝████╗░██║╚══██╔══╝  ██╔════╝\n" +
                "███████║╚█████╗░╚█████╗░██║██║░░██╗░██╔██╗██║██╔████╔██║█████╗░░██╔██╗██║░░░██║░░░  ██████╗░\n" +
                "██╔══██║░╚═══██╗░╚═══██╗██║██║░░╚██╗██║╚████║██║╚██╔╝██║██╔══╝░░██║╚████║░░░██║░░░  ╚════██╗\n" +
                "██║░░██║██████╔╝██████╔╝██║╚██████╔╝██║░╚███║██║░╚═╝░██║███████╗██║░╚███║░░░██║░░░  ██████╔╝\n" +
                "╚═╝░░╚═╝╚═════╝░╚═════╝░╚═╝░╚═════╝░╚═╝░░╚══╝╚═╝░░░░░╚═╝╚══════╝╚═╝░░╚══╝░░░╚═╝░░░  ╚═════╝░");
        System.out.println("Created by:                             Andrew Colbert                             \n");
        try {
            //******
            //PART A
            //******

            //Set up the scanner and read the initial parameters
            Scanner fr = new Scanner(new FileReader("src/CustomerData.txt"));
            String paramStr = fr.nextLine();
            String[] paramArr = paramStr.split(" ");

            //Retrieve the parameters
            int numExpLanes = Integer.parseInt(paramArr[0]);
            int numNormLanes = Integer.parseInt(paramArr[1]);
            int maxExpItems = Integer.parseInt(paramArr[2]);
            int numCusts = fr.nextInt();

            //Initialize the lanes
            LinkedQueue<Customer>[] lanesQueuesArr = new LinkedQueue[numExpLanes + numNormLanes];
            for(int i =0; i<numExpLanes+numNormLanes;i++){
                lanesQueuesArr[i] = new LinkedQueue<>();
            }

            //Create an array to maintain the time for each queue
            int[] timePerLane = new int[lanesQueuesArr.length];

            //Iterate through the file of customers
            while(fr.hasNext()){
                Customer curCust = new Customer(fr.nextInt());

                //Create a queue loop starting value, if customer can use express will be 0,
                //if not will start searching after the express lanes
                int startSearch = 0;
                if(curCust.getItems() > maxExpItems)
                    startSearch = numExpLanes;

                //Search the times for the queues for the shortest available
                int leastTime = 1000000;
                int leastIndex = -1;
                for(int i = startSearch; i<timePerLane.length;i++){
                    if (timePerLane[i] < leastTime){
                        leastTime = timePerLane[i];
                        leastIndex = i;
                    }
                }

                //Add the customer to the lowest lanes available and increase time
                lanesQueuesArr[leastIndex].enqueue(curCust);
                timePerLane[leastIndex] += curCust.calculateEstTime();
            }

            //Output part A
            System.out.println("PART A - Checkout lanes and time estimates");
            for(int i = 0; i< lanesQueuesArr.length; i++) {
                String laneType = (i<numExpLanes)?"Express":"Normal";
                System.out.printf("Checkout(%s) #%d (est time %ds) = %s\n", laneType, i+1, timePerLane[i],lanesQueuesArr[i].toString());
            }

            //******
            //PART B
            //******
            int timer = 0;
            int[] timeRemainingPerCust = new int[lanesQueuesArr.length];

            //Initialize the time remaining array to the first customer in each line
            for(int i = 0; i < timeRemainingPerCust.length; i++)
                if(!lanesQueuesArr[i].isEmpty())
                    timeRemainingPerCust[i] = lanesQueuesArr[i].peek().calculateEstTime();
                else
                    timeRemainingPerCust[i] = 0;

            //Output the headers
            System.out.println("\nPART B - Simulate customers removed from queue");
            String output = "t(s) ";
            for(int i = 0; i < lanesQueuesArr.length; i++){
                output = output.concat( "Lane " + (i+1) + " ");
            }
            System.out.println(output);

            //Create simulation
            while(true){
                //Create a flag assuming we're finished (empty queues). if any customers are left
                //this flag will be set to false
                boolean finished = true;

                //Loop through the time remaining for each customer to see if any should finish
                for(int i = 0; i < timeRemainingPerCust.length; i++){
                    //Check to see if a customer has finished their time
                    if(timeRemainingPerCust[i] == 0){
                        lanesQueuesArr[i].dequeue();

                        //Customer has finished, check if there's a next customer
                        if(!lanesQueuesArr[i].isEmpty()) {
                            //Set the time remaining for this lane equal to the next customer's time
                            timeRemainingPerCust[i] = lanesQueuesArr[i].peek().calculateEstTime();
                        }
                        else
                            //No customer remaining for this queue, set time remaining to invalid value
                            timeRemainingPerCust[i] = -1;
                    } else if(timeRemainingPerCust[i] > 0){
                        //Still a customer in line, set finished to false
                        finished = false;
                    }

                    //Decrement the time remaining for this customer in this lane
                    timeRemainingPerCust[i]--;
                }



                //If all customers have been attended to finish the simulation
                if(finished) {
                    outputLanes(lanesQueuesArr, timer);
                    break;
                } else {
                    //Output the lanes in 60 'second' intervals
                    //Put it into the else statement to prevent potential doubles being printed at the end
                    if(timer % 30 == 0){
                        outputLanes(lanesQueuesArr, timer);
                    }
                }

                timer++;
            }

        } catch (Error | FileNotFoundException e) {
            System.out.println("Error" + e.getMessage());
        }
    }
    public static void outputLanes(LinkedQueue<Customer>[] lanes, int timer){
        String output = String.format("%4d  ", timer);
        for(LinkedQueue<Customer> lane : lanes){
            output = output.concat(String.format("%3d    ", lane.size()));
        }
        System.out.println(output);
    }
}
