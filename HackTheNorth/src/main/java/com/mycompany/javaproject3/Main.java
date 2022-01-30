package com.mycompany.javaproject3;

import EventsPackage.HelloEvent;
import EventsPackage.TicTacToe;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {

    public static void main(String args[]) throws Exception {
        String token = KEY
        JDA jda = JDABuilder.createDefault(token).build();
        TicTacToe tictactoe = new TicTacToe();
        jda.addEventListener(new HelloEvent(tictactoe));
    }
}
