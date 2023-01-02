package Cribbage;

/**
 * The Runner for the Cribbage game.
 *
 * @author Maverick Berkland
 * @version 10/26/2022
 */
public class Runner {
    public static void main(String[] args) {
        Game g = new Game(2);
        System.out.println(g.toString());
    }
}
