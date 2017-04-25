package game;

/**
 * @ SFSU Spring 2017 CSC413 assignment 4 4/18/2017
 */

public class GameTimer {

    private int frame = 0;
    private int frameSize;
    private int moments[];
    private GameEvents gE;
    int i = 0; 

    public GameTimer(GameEvents gE, int moments[], int frameSize) {
        this.gE = gE;
        this.moments = moments;
        this.frameSize = frameSize;
    }

    public void play() {
        if (frame < moments[moments.length-1]) {
            frame += frameSize;
            //System.out.println("Frame: " + frame);
            if (frame == moments[i]) {
                gE.setValue(i++);
            }
        } else {
            frame = i = 0;
        }
    }
}
