package newGame;

import sz.csi.ConsoleSystemInterface;
import sz.csi.wswing.WSwingConsoleInterface;

import java.util.Random;

public class MainGame {

    public static Random random;
    public static ConsoleSystemInterface csi;

    public static void main(String[] args) {
    	for(;;)
    	{
    		new MainGame();
    	}
    }

    private MainGame() {
        random = new Random();
        csi = new WSwingConsoleInterface();
        new Map();
        csi.refresh();
        csi.waitKey(10);
    }
}
