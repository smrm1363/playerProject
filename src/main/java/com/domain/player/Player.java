package com.domain.player;
import com.domain.mqAdapter.MqAdapter;

/*
this is a Player class
 */
public class Player {
    private final int maxMessage = 10;
    private final MqAdapter<String> toBeSendQueue;
    private final MqAdapter<String> toBeReceivedQueue;

    public Player(MqAdapter<String> toBeSendQueue, MqAdapter<String> toBeReceivedQueue) {
        this.toBeSendQueue = toBeSendQueue;
        this.toBeReceivedQueue = toBeReceivedQueue;
    }
/*
This method is for sending start message, the initiator uses this method
 */
    public void initiate(String message) {
        int counter = 0;
       while (counter < maxMessage) {
           try {
               toBeSendQueue.put(message);
               System.out.println("Message sent "+counter);
               counter++;
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
   /*
   This method is for receiving response from the second player, the initiator uses this method
    */
    public void receiveAcknowledge() {
           int counter = 0;
           while (counter<maxMessage)
           {
               try {
                   System.out.println("Returned message : "+toBeReceivedQueue.take());
                   counter++;
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
   }

    /**
     * This method is for receiving message from initiator, the second player uses this method
     */
   public void receive()
   {
           int counter = 0;
           while (counter<maxMessage)
           {
               try {
                   String message = toBeReceivedQueue.take()+ counter;
                   System.out.println("Message received "+counter++);
                   toBeSendQueue.put(message);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
   }
}
