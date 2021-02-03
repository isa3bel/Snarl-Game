// an example run of Traveller
public class traveller_server {
    public static void main(String[] args) {
        Traveller traveller = new Traveller("TownB: TownA; TownC: TownA, TownD; TownX: TownA, TownD; TownQ: TownX");

        traveller.placeCharacter("Sam", "TownA");
        traveller.placeCharacter("Tom", "TownC");

        // TownC -> TownA -> TownB but Sam is in A
        if (traveller.canReachTownAlone("Tom", "TownB")) {
            System.out.println("Test Failed: Tom can go");
        } else {
            System.out.println("Test Passed: Tom can NOT go"); // this is expected
        }

        // TownC -> TownD -> TownX
        if (traveller.canReachTownAlone("Tom", "TownX")) {
            System.out.println("Test Passed: Tom can go"); // this is expected
        } else {
            System.out.println("Test Failed:Tom can NOT go");
        }

        // TownC -> TownD -> TownX -> TownQ
        if (traveller.canReachTownAlone("Tom", "TownQ")) {
            System.out.println("Test Passed: Tom can go"); // this is expected
        } else {
            System.out.println("Test Failed:Tom can NOT go");
        }
    }
}