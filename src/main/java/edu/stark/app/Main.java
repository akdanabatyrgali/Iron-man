package edu.stark.app;

import edu.stark.core.ActionResult;
import edu.stark.facade.StarkSuitFacade;
import edu.stark.missions.MissionScenario;
import edu.stark.suits.SuitType;
import edu.stark.weapons.WeaponType;
import java.util.Scanner;

public class Main {
    private static final StarkSuitFacade facade = new StarkSuitFacade();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==================================");
        System.out.println(" STARK INDUSTRIES | SUIT CONTROL ");
        System.out.println("==================================");
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Your choice: ");
            int choice = readInt(scanner);
            switch (choice) {
                case 1 -> chooseSuit(scanner);
                case 2 -> show(facade.activate());
                case 3 -> chooseMission(scanner);
                case 4 -> chooseWeapon(scanner);
                case 5 -> show(facade.attack());
                case 6 -> show(facade.defend());
                case 7 -> show(facade.fly());
                case 8 -> show(facade.recharge());
                case 9 -> show(facade.repair());
                case 10 -> System.out.println("\n" + facade.status());
                case 11 -> show(facade.retreat());
                case 12 -> System.out.println("\n" + facade.missionHistory());
                case 0 -> { running = false; System.out.println("System shutting down. Goodbye!"); }
                default -> System.out.println("Choose one of the listed numbers.");
            }
        }
    }
    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println(" 1. Select suit      2. Activate suit");
        System.out.println(" 3. Start mission    4. Select weapon");
        System.out.println(" 5. Attack           6. Defend");
        System.out.println(" 7. Fly              8. Recharge");
        System.out.println(" 9. Repair          10. Suit status");
        System.out.println("11. Retreat         12. Mission history");
        System.out.println(" 0. Exit");
    }
    private static void chooseSuit(Scanner scanner) {
        System.out.println("Choose suit: 1. Mark III  2. Mark V  3. Mark L");
        int choice = readInt(scanner);
        SuitType type = switch (choice) {
            case 1 -> SuitType.MARK_III;
            case 2 -> SuitType.MARK_V;
            case 3 -> SuitType.MARK_L;
            default -> null;
        };
        show(facade.selectSuit(type));
    }
    private static void chooseMission(Scanner scanner) {
        System.out.println("Choose mission: 1. Monaco  2. Stark Expo  3. Titan Rescue");
        show(facade.startMission(MissionScenario.fromNumber(readInt(scanner))));
    }
    private static void chooseWeapon(Scanner scanner) {
        System.out.println("Choose weapon: 1. Repulsor  2. Missile  3. Nanotech Blade  4. Unibeam");
        int choice = readInt(scanner);
        WeaponType type = switch (choice) {
            case 1 -> WeaponType.REPULSOR;
            case 2 -> WeaponType.MISSILE;
            case 3 -> WeaponType.NANO_BLADE;
            case 4 -> WeaponType.UNIBEAM;
            default -> null;
        };
        show(facade.selectWeapon(type));
    }
    private static int readInt(Scanner scanner) {
        String text = scanner.nextLine().trim();
        try { return Integer.parseInt(text); }
        catch (NumberFormatException ex) { return -1; }
    }
    private static void show(ActionResult result) {
        System.out.println("\n" + (result.success() ? "[OK] " : "[ERROR] ") + result.message());
    }
}
