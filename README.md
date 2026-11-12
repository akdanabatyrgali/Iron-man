Iron Man Suit Control System

A full-stack Java application inspired by Marvel's Iron Man, built to demonstrate six GoF design patterns. The user selects a suit, activates it, chooses a weapon, starts a mission and fights an enemy while managing the suit's energy and armor. Completed missions are saved in an H2 database.

The game logic is written entirely in Java. The browser interface only calls the REST API and displays the JSON it returns.

Team Members
Member	Design patterns
Akdana Batyrgali	Factory Method, Observer, State
Azhar	Strategy, Command, Facade
Technologies
Java 17
Spring Boot 3.5 (Spring Web, Spring Data JPA)
H2 Database (file-based, no installation required)
HTML, CSS, JavaScript (Fetch API)
Maven
JUnit 5
IntelliJ IDEA
How to Run
Web version (main)
bash
mvn spring-boot:run

Or run edu.stark.StarkApplication from IntelliJ IDEA.

Then open http://localhost:8082 in your browser.

H2 database console: http://localhost:8082/h2-console (JDBC URL: jdbc:h2:file:./data/stark-history, user: sa, empty password)
Database file location: data/stark-history.mv.db
Console version

Run edu.stark.app.Main. It provides a text menu with the same game logic. Mission history in the console version is kept in memory only and is not saved to the database.

Tests
bash
mvn test
Design Patterns
Pattern	Package	Purpose
Factory Method	suits	SuitCreator declares buildSuit(). MarkIIICreator, MarkVCreator and MarkLCreator create the concrete suits.
State	states	The suit's behavior depends on its state: STANDBY, ACTIVE, COMBAT or DAMAGED. BaseSuitState denies every action by default, and each state allows only its own actions.
Observer	observers	IronManSuit notifies its subscribers whenever energy, armor or state changes. WarningObserver issues a one-time warning when energy drops to 20 or below, or armor drops to 30 or below.
Strategy	weapons	Each weapon implements WeaponStrategy with its own energy cost and damage formula. The weapon can be switched at runtime through WeaponCatalog.
Command	commands	Every action (Activate, Attack, Defend, Fly, Recharge, Repair, Retreat) is a separate Command object executed by CommandInvoker.
Facade	facade	StarkSuitFacade hides all subsystems behind simple methods. Both the console menu and the web service use only the facade.
Suit state transitions
STANDBY --activate--> ACTIVE --start mission--> COMBAT
   ^                    ^                          |
   |                    +------ mission ends ------+
   |                                               | armor = 0
   +----------- repair ------- DAMAGED <-----------+
Game Data
Suits
Suit	Energy	Armor	Assistant	Weapons	Defense
Mark III	100	120	J.A.R.V.I.S.	Repulsor, Missile, Unibeam	Blocks 50%
Mark V	90	75	J.A.R.V.I.S.	Repulsor	Blocks 50%
Mark L	160	150	F.R.I.D.A.Y.	Repulsor, Nanotech Blade, Unibeam	Nanotech shield, blocks 75%
Weapons
Weapon	Energy cost	Damage
Repulsor	12	20
Missile	24	30 (38 against armored enemies)
Nanotech Blade	18	27 (33 against armored enemies)
Unibeam	34	42
Missions
Mission	Enemy	HP	Enemy damage	Armored
Monaco Emergency	Whiplash	80	18	No
Stark Expo Defense	Hammer Drone	110	21	Yes
Titan Rescue	Alien Vanguard	150	26	Yes
Rules
Attack uses the weapon's energy. If the enemy survives, it counterattacks and reduces the suit's armor.
Defend costs 5 energy and reduces the incoming damage by the suit's blocking percentage.
Fly costs 10 energy.
Recharge and Repair are not allowed during combat.
The mission is a success when the enemy's HP reaches 0. It fails when the suit's armor reaches 0 (the suit becomes DAMAGED) or when the player retreats.
The suit cannot be changed during a mission.
REST API
Method	Endpoint	Description
GET	/api/catalog	Lists of suits, weapons and missions
GET	/api/dashboard	Current suit status and mission history
GET	/api/history	Mission history from the database
POST	/api/suits	Select a suit, e.g. {"value": "MARK_L"}
POST	/api/weapons	Select a weapon, e.g. {"value": "UNIBEAM"}
POST	/api/missions	Start a mission, e.g. {"value": "MONACO"}
POST	/api/actions/{name}	activate, attack, defend, fly, recharge, repair, retreat
Project Structure
src/main/java/edu/stark/
├── StarkApplication.java   # Spring Boot entry point (web version)
├── app/                    # Console version (Main)
├── commands/               # Command pattern
├── core/                   # ActionResult, GameSnapshot, output formatter
├── facade/                 # Facade pattern
├── missions/               # Missions, enemies, combat logic
├── observers/              # Observer pattern
├── persistence/            # JPA entity and repository (H2)
├── service/                # StarkGameService: shared game session and saving results
├── states/                 # State pattern
├── suits/                  # Factory Method pattern and suit classes
├── weapons/                # Strategy pattern
└── web/                    # REST controller and response records
src/main/resources/
├── static/                 # index.html, styles.css, app.js
└── application.properties  # Port 8082, H2 settings
src/test/java/edu/stark/tests/
├── IronManPatternTest.java    # Tests for each design pattern
└── IronManEdgeCasesTest.java  # Edge cases and invalid actions
Notes

This is a local coursework demo. The H2 console is enabled for development only, and the application is not intended to be deployed as a public service.
