package com.domain.player;

import com.domain.mqAdapter.MqAdapter;
import com.domain.mqAdapter.MqFactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This class is the logic of the program. This is a singleton
 */
public class PlayerService {
    private static PlayerService playerService;
    public static PlayerService getInstance()
    {
        if(playerService== null)
            playerService= new PlayerService();
        return playerService;
    }

    private PlayerService() {
    }

    /**
     * This method is responsible for passing the queues to the players
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void runMessagePassing() throws ExecutionException, InterruptedException {
        MqAdapter<String> toBeSendQueue = MqFactory.getInstance().getMq();
        MqAdapter<String> toBeReceivedQueue = MqFactory.getInstance().getMq();
        Player initiator=new Player(toBeSendQueue,toBeReceivedQueue);
        Player receiver=new Player(toBeReceivedQueue,toBeSendQueue);
        /**
         * I used CompletableFuture because async call,
         */
        CompletableFuture initiateFuture =CompletableFuture.runAsync(() ->initiator.initiate("This is a message") ) ;
        CompletableFuture acknowledgeFuture =CompletableFuture.runAsync(initiator::receiveAcknowledge);
        CompletableFuture receiveFuture =CompletableFuture.runAsync(receiver::receive);
        CompletableFuture.allOf(initiateFuture,acknowledgeFuture,receiveFuture).get();
    }
}
