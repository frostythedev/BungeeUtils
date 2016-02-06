package me.frostythedev.bungeeutils.utils;

/**
 * Programmed by Tevin on 1/25/2016.
 */
public class MessageUtils {

    public static String buildfromArgs(int index, String[] args){
        StringBuilder builder = new StringBuilder();

        if(index > args.length){
            index = args.length;
        }
        for(int i = index; i < args.length; i++){
            builder.append(args[i]).append(" ");
        }
        return builder.toString();
    }
}
