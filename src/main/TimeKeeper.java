package src.main;

import java.time.Duration;

public class TimeKeeper {
    Duration duration = Duration.ZERO;

    public void TimeKeeper(int min, int sec)//a class to keep track of the total seconds and minuntes the player has used to get to a level
    {
        duration = duration.plusMinutes(min).plusSeconds(sec);
    }//end src.main.TimeKeeper

    public int getMinutes() {
        return duration.toMinutesPart();
    }

    public int getSeconds() {
        return duration.toSecondsPart();
    }
}//end class
