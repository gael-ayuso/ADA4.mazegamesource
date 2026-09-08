package src.main;

import java.time.Duration;

public class TimeCalculator {
    private Duration duration = Duration.ZERO;

    public void calcTimeforMaze(int totalDimonds, int xSize, int ySize)//some kinda method that determines the time a player has for each level based on the level size and dimonds.
    {
        if (xSize / ySize < 1)//this method should be changed in order to provide a more relistic time system.
        {
            duration = duration.plusMinutes((ySize / xSize) + 1);
        } else
            duration = duration.plusMinutes((ySize / xSize) + 1);
        if (totalDimonds > 6 && totalDimonds * .10 + duration.toSecondsPart() <= 60){
            duration = duration.plusMinutes((ySize / xSize) + 1);
        } else {
            duration = duration.plusMinutes(1);
        }
        if (duration.toMinutesPart() == 0){
            duration = Duration.ofMinutes(2);
        }
    }//end method

    public int getMinutes() {
        return duration.toMinutesPart();
    }

    public int getSeconds() {
        return duration.toSecondsPart();
    }
}//end class
