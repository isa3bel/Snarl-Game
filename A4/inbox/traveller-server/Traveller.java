import java.util.ArrayList;

class Traveller {

    // list of towns in the world
    ArrayList<Town> townNetwork = new ArrayList<Town>();

    // townConnections in format "TownB:TownA TownC:TownA,TownD"
    public Traveller(String townConnections) {
        // remove white space from input
        townConnections = townConnections.replaceAll("\\s+", "");
        // seperate by Town name
        String[] differentTowns = townConnections.split(";");
        for (String townConnec : differentTowns) {
            String[] townName = townConnec.split(":");

            // create town
            Town newTown = new Town(townName[0]);

            // if it has connections
            if (townName.length > 1) {
                // split connection list
                String[] connections = townName[1].split(",");
                for (String connec : connections) {
                    // if connected town exists
                    if (getTown(connec) != null) {
                        Town matchTown = getTown(connec);
                        newTown.addConnection(matchTown);
                        matchTown.addConnection(newTown);
                        this.townNetwork.add(newTown);
                    } else { // if connected town does not exist
                        Town matchTown = new Town(connec);
                        newTown.addConnection(matchTown);
                        matchTown.addConnection(newTown);
                        this.townNetwork.add(matchTown);
                        this.townNetwork.add(newTown);
                    }
                }
            }
        }
    }

    public Town getTown(String townName) {
        if (townNetwork != null) {
            for (Town town : townNetwork) {
                if (town.getTownName().equals(townName)) {
                    return town;
                }
            }
        }

        return null;
    }

    // places the given character in the given town
    public void placeCharacter(String characterName, String townName) {
        if (getTown(townName) != null) {
            Town matchTown = getTown(townName);
            matchTown.addCharacter(characterName);
        }
    }

    // determines if the given character can reach the given town without running
    // into another character
    public boolean canReachTownAlone(String characterName, String townName) {
        for (Town town : townNetwork) {
            if (town.hasCharacter(characterName) && getTown(townName) != null) {
                Town matchTown = getTown(townName);
                return town.canReachTown(matchTown);
            }
        }
        return false;
    }
}