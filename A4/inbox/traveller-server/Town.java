import java.util.ArrayList;

class Town {
    private ArrayList<String> characters; // character representation
    private ArrayList<Town> connections; // this towns connections
    private String name; // name of town

    // default contructor
    public Town(String name) {
        connections = new ArrayList<Town>();
        characters = new ArrayList<String>();
        this.name = name;
    }

    // get town name
    public String getTownName() {
        return name;
    }

    // add a character to this town
    public void addCharacter(String name) {
        characters.add(name);
    }

    // add connecting town
    public void addConnection(Town town) {
        connections.add(town);
    }

    // is character at this town?
    public boolean hasCharacter(String name) {
        return characters.contains(name);
    }

    // can this town reach dest?
    public boolean canReachTown(Town dest) {
        boolean path = false;
        ArrayList<String> visited = new ArrayList<String>();
        visited.add(this.getTownName());
        int i = 0;
        // modified dfs
        while (i < connections.size() && !path) {
            if (connections.get(i).canReachTown(dest, visited)) {
                path = true;
            }
            i++;
        }

        return path;
    }

    // check if character list is empty
    public boolean isCharacterEmpty() {
        return this.characters.isEmpty();
    }

    // dfs helper function
    public boolean canReachTown(Town dest, ArrayList<String> visited) {
        boolean path = false;
        ArrayList<String> visited2 = new ArrayList<String>(visited);
        visited2.add(this.getTownName());
        if (this.isCharacterEmpty()) {
            if (this.getTownName().equals(dest.getTownName())) {
                path = true;
            } else {
                int i = 0;
                while (!path && i < connections.size()) {
                    if (!visited2.contains(connections.get(i).getTownName())
                            && connections.get(i).canReachTown(dest, visited2)) {
                        path = true;
                    }
                    i++;
                }
            }

        }

        return path;
    }
}
