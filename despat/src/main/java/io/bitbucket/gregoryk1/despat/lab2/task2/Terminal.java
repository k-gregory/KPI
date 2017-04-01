package io.bitbucket.gregoryk1.despat.lab2.task2;


public class Terminal {
    public void pass(SubwayClient client, PaymentChecker checker){
        System.out.println(client.getName()+" tries to pass");
        if(checker.tryPass()){
            System.out.println(client.getName()+" passed");
        } else {
            System.out.println(client.getName()+" failed to pass");
        }
    }
}
