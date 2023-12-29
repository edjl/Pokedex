/*
 * Edward Lee
 * December 21, 2023
 */

package src;

import src.Model.Move;
import src.Model.MoveCategory;
import src.Model.Pokemon;

public class Simulation {
    /*
     * Simulates attack 10,000 times
     * Returns survival rate
     */
    public static double simulateAttack(Pokemon attacker, Move move, Pokemon defender, int hp, boolean value) {
        double targets = 1;
        double pb = 1;
        double weather = 1;
        double glaveRush = 1;
        double stab = (attacker.isType(move.getType())) ? 1.5 : 1;
        double type = defender.getTypeDamageMultiplier(move.getType());
        double burn = 1;
        double other = 1;
        
        double count = 0;
        int runs = 10000;
        for (int i = 0; i < runs; i++) {
            attacker.randValues();
            defender.randValues();

            double A = (move.getCategory() == MoveCategory.Physical) ? attacker.getStats()[1] : attacker.getStats()[3];
            double D = (move.getCategory() == MoveCategory.Physical) ? defender.getStats()[2] : defender.getStats()[4];
            
            double critical = (0.0625 > Math.random()) ? 2 : 1;
            double random = (16 * Math.random() + 85) / 100.0;

            int damage = (int)(((2.0 * (double)attacker.getLevel() / 5.0 + 2) * move.getPower() * A/D / 50 + 2) * critical * random * targets * pb 
                * weather * glaveRush * stab * type * burn * other);
            if (value && damage < hp || !value && damage < ((double)hp / 100 * defender.getStats()[1]))
                count++;
        }
        return count / runs;
    }
}
