/*
 * Edward Lee
 * December 21, 2023
 */

package src.Model;

public enum Type {
    Normal,
    Fighting,
    Flying,
    Poison,
    Ground,
    Rock,
    Bug,
    Ghost,
    Steel,
    Fire,
    Water,
    Grass,
    Electric,
    Psychic,
    Ice,
    Dragon,
    Dark,
    Fairy;

    private int number;
    static {
        Normal.number = 1;
        Fighting.number = 2;
        Flying.number = 3;
        Poison.number = 4;
        Ground.number = 5;
        Rock.number = 6;
        Bug.number = 7;
        Ghost.number = 8;
        Steel.number = 9;
        Fire.number = 10;
        Water.number = 11;
        Grass.number = 12;
        Electric.number = 13;
        Psychic.number = 14;
        Ice.number = 15;
        Dragon.number = 16;
        Dark.number = 17;
        Fairy.number = 18;
    }

    public int getNumber() {
        return number;
    }
}