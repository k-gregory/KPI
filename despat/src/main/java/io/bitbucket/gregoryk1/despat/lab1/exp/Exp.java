package io.bitbucket.gregoryk1.despat.lab1.exp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by grego on 21.03.2017.
 */
public class Exp {
    public static void main(String... args){
        Pattern pattern = Pattern.compile("x");
        Matcher matcher = pattern.matcher("axa");
        while (matcher.find()){
            System.out.println(matcher.start()+" "+matcher.end());
        }
    }
}
