public class Pokemon {
    private int id, level, iv, ev;
    private String name, nature;
    private String type1, type2;
    private int total, hp, attack, defense, spAttack, spDefense, speed;

    public Pokemon(String name, int level, int iv, int ev, String nature) {
        String []info = GeneralTable.getGeneralInfoByName(name);
        id = Integer.parseInt(info[0]);
        this.name = info[1];
        type1 = info[2];
        type2 = info[3];
        hp = (int)(0.01 * level * (2 * Integer.parseInt(info[5]) + iv + (int)(0.25 * ev))) + level + 10;
        attack = (int)(((0.01 * level * (2 * Integer.parseInt(info[6]) + iv + (int)(0.25 * ev))) + 5) * NatureTable.getMultiplier(nature, "Attack"));
        defense = (int)(((0.01 * level * (2 * Integer.parseInt(info[6]) + iv + (int)(0.25 * ev))) + 5) * NatureTable.getMultiplier(nature, "Defense"));
        spAttack = (int)(((0.01 * level * (2 * Integer.parseInt(info[6]) + iv + (int)(0.25 * ev))) + 5) * NatureTable.getMultiplier(nature, "Sp. Attack"));
        spDefense = (int)(((0.01 * level * (2 * Integer.parseInt(info[6]) + iv + (int)(0.25 * ev))) + 5) * NatureTable.getMultiplier(nature, "Sp. Defense"));
        speed = (int)(((0.01 * level * (2 * Integer.parseInt(info[6]) + iv + (int)(0.25 * ev))) + 5) * NatureTable.getMultiplier(nature, "Speed"));
        total = hp + attack + defense + spAttack + spDefense + speed;
        this.level = level;
        this.iv = iv;
        this.ev = ev;
        this.nature = nature;
    }

    public String getType() {
        return type1 + type2;
    }

    public void printPokemon() {
        System.out.print("Pokemon:\t" + id + "\t  " + name.toUpperCase() + "    \t" + type1);
        System.out.println((type2.equals("") ? "" : "-" + type2));
        System.out.println("\tLevel:\t" + level + "\t\t" + iv + " IV   " + ev + " EV");
        System.out.println("\tNature:\t" + (char)(nature.charAt(0)-32) + nature.substring(1));
        System.out.println("\tStats:\t" + total + "\t  " + hp + "  " + attack + "  " + defense
            + "  " + spAttack + "  " + spDefense + "  " + speed);
    }
}
