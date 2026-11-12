Iron Man Suit Control System

A Java web application inspired by Marvel's Iron Man that demonstrates six GoF design patterns. Users select a suit, choose a weapon, start missions, fight enemies and manage energy and armor. Completed missions are saved in an H2 database.

Team Members
Akdana Batyrgali: Factory Method, Observer, State
Azhar: Strategy, Command, Facade

Design Patterns
Factory Method: creates the suits (Mark III, Mark V, Mark L).
Observer: warns when energy or armor gets low.
State: controls the suit's states (STANDBY, ACTIVE, COMBAT, DAMAGED).
Strategy: switches weapons at runtime (Repulsor, Missile, Nanotech Blade, Unibeam).
Command: wraps each action (attack, defend, fly, recharge, repair, retreat) in its own object.
Facade: connects all systems behind one simple interface.
