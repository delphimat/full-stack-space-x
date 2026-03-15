# SpaceX Dragon Rockets Library

A pure Java library to manage SpaceX Dragon rockets and their missions. This library enforces business rules and state transitions using an in-memory data store, without the use of heavy frameworks, adhering to Clean Architecture and SOLID principles.

## Features

- **Rockets Management**: Create rockets with default "On Ground" status. Change statuses manually to "In Space" or "In Repair".
- **Mission Management**: Create missions with an initial "Scheduled" status.
- **Assignment Logic**: A rocket can only be assigned to one active mission at a time.
- **Automatic Mission State Evaluation**:
    - `SCHEDULED`: Initial state or if no rockets are assigned.
    - `PENDING`: If at least one assigned rocket is "In Repair".
    - `IN_PROGRESS`: If at least one rocket is assigned and no rockets are "In Repair".
    - `ENDED`: Manual terminal state. Once a mission is ended, no new rockets can be assigned, but its existing rockets are freed up and can be assigned to new active missions.
- **Summary Reporting**: Generate a summary of all missions, sorted by the number of assigned rockets in descending order. In case of ties, sorted in descending alphabetical order by mission name.

## Tech Stack

- **Java 11** (or higher)
- **Maven** (for build and dependency management)
- **JUnit 5** & **AssertJ** (for Test-Driven Development and assertions)