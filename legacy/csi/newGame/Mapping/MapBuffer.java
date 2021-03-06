package newGame.Mapping;

import newGame.Entities.Inventory.InventoryStack;
import newGame.Entities.Item;
import newGame.IntPoint;
import newGame.MainGame;

/**
 * Provides a way to store multiple types of data
 * in one tile of the map.
 *
 * (Previously used the 'char' type; it didn't allow
 * storage of InventoryStacks)
 */
public class MapBuffer extends ArrayList2D<Tile> {

    private static final int chanceVal = 1000; // Value for MainGame.random.nextInt();
    /**
     * Default MapBuffer constructor; uses
     * default settings for the 2d ArrayList inside the
     * ArrayList2D default constructor.
     */
    public MapBuffer() {
        super();
    }

    /**
     * Constructor that will initialize the super class with
     * the width and height parameters.
     * @param width Width of the map buffer.
     * @param height Height of the map buffer.
     */
    public MapBuffer(int width, int height) {
        super(width, height);
    }

    /**
     * Scatters a specified item throughout the map with the given minimum
     * and maximum per stack; and rarity for each item.
     * @param itemtype Type of item to scatter.
     * @param scaton Tile to scatter on.
     * @param min Minimum amount per InventoryStack.
     * @param max Maximum amount per InventoryStack.
     * @param rarity Chance of spawning (rarity/100)
     */
    public void scatter(Item itemtype, Tile scaton,  int min, int max, int rarity) {
        final int cmax = Math.max(Math.max(max, min), 1);
        final int cmin = Math.max(Math.min(max, min), 1);
        InventoryStack<Item> is;
        int amount;
        int spawned = 0;
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                final Tile t = getElement(x, y);
                if(t == null || !t.equalsTo(scaton) || t.hasItems()) {
                    continue;
                }
                final int chance = MainGame.random.nextInt(chanceVal) + 1;
                if(chance <= rarity) {
                    is = new InventoryStack<>();
                    if (cmax - cmin <= 0) {
                        amount = 1;
                    }
                    else {
                        amount = MainGame.random.nextInt(cmax - cmin + 1) + cmin;
                    }
                    is.setLimit(amount);
                    for(int i = 0; i <= amount; i++) {
                        is.addNext(itemtype);
                    }
                    t.setInventoryStack(is);
                    setElement(t, x, y);
                    spawned++;
                    if(spawned == cmax)
                        return;
                }
            }
        }
    }

    /**
     * Randomly picks a location of a sepcified type.
     * @param oftype Tile type to find a location on.
     * @return Position of a located tile equivalent to the one specified.
     */
    public IntPoint randomLoc(Tile oftype) {
        int rx;
        int ry;
        Tile t;
        do {
            rx = MainGame.random.nextInt(getWidth());
            ry = MainGame.random.nextInt(getHeight());
            t = getElement(rx, ry);
        }
        while(t == null || !t.equalsTo(oftype));
        return new IntPoint(rx, ry);
    }
}
