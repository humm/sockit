import java.io.IOException;

import sockit.InputMessage;
import sockit.OutputMessage;
import sockit.Server;


public class FibServer {
    
    // the port number to use
    public static final int PORT  = 1984;
    // the local ip
    public static final String ip = "127.0.0.1";
    
    /** PROTOCOL **/
    public static final int HELLO_TYPE = 0; // a string in the message to the server, no answer
    public static final int BYE_TYPE   = 1; // a string in the message to the server, empty answer
    public static final int FIBO_TYPE  = 2; // a int in the message to the server, an int in the answer
    
    // this class allows to run the server in a thread
    public class ServerThread extends Thread{
        public void run(){
            ServerTest();
        }
    }
    
    // the main function wich launch the server and the client and waits for the end of both
    public static void main(String[] args) {
        FibServer e = new FibServer();
        ServerThread s = e.new ServerThread();
        s.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    
    // server code
    public static void ServerTest(){
        Server s = new Server();
        s.start(PORT);
        System.out.println("SERVER -> started");
        boolean run = true;
        boolean count = false;
        while(run == true){
            int n = s.getNumberOfMessages();
            if(n > 0){
                InputMessage in = s.receive();
                int type = in.getType();
                switch(type){
                case HELLO_TYPE:
                    {
                        try {
                            System.out.println("SERVER -> receives : " + in.readString());
                            count = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                case BYE_TYPE:
                    {
                        try {
                            System.out.println("SERVER -> receives : " + in.readString());
                            OutputMessage a = new OutputMessage();
                            a.setType(BYE_TYPE);
                            s.send(a);
                            count = false;
                            run = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                case FIBO_TYPE:
                    {
                        if(count == true){
                            int f;
                            try {
                                f = in.readInt();
                                System.out.println("SERVER -> computes fibo(" + f + ")");
                                int t = fibo(f);
                                OutputMessage a = new OutputMessage();
                                a.setType(FIBO_TYPE);
                                a.appendInt(t);
                                s.send(a);
                            } catch (IOException e) {
                                OutputMessage a = new OutputMessage();
                                a.setType(FIBO_TYPE);
                                a.appendInt(-1);
                                s.send(a);
                                e.printStackTrace();
                            }
                        }
                        else{
                            System.out.println("SERVER -> client is rude ! No answer !");
                            run = false;
                        }
                        break;
                    }
                }
            }
        }
        s.stop();
        System.out.println("SERVER -> stopped");
        return;
    }

    
    // recursive version of fibo
    public static int fibo(int n) {
        if (n <= 1) return n;
        else return fibo(n-1) + fibo(n-2);
    }
}
