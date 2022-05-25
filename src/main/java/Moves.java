public class Moves {
    private String name, type, category;
    private String damage, pp, accuracy;
    public Moves(String name) {
        String []info = MoveTable.getMoveInfoByName(name);
        this.name = info[0];
        this.type = info[1];
        this.category = info[2];
        this.damage = info[3];
        this.pp = info[4];
        this.accuracy = info[5];
    }

    public String getName () {
        return name;
    }
}
