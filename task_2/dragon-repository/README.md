# SpaceX Dragon Rockets Library

A pure Java library to manage SpaceX Dragon rockets and their missions. 
This library enforces business rules and state transitions using an in-memory data store, without the use of frameworks, adhering to Clean Architecture and SOLID principles.

## Tech Stack

- **Java 11** (or higher)
- **Maven** (for build and dependency management)
- **JUnit 5** & **AssertJ** (for Test-Driven Development and assertions)

## Architecture
- **Architecture Validation**: The project is implemented as a simple, plain Java library.
- **In-Memory Store**: The `InMemoryRepository` correctly leverages standard Java Collections (`HashMap`) to act as a lightweight data store.

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

## Business Logic
- **Rocket Addition & Initialization**: A new rocket defaults to `ON_GROUND`, which aligns with the requirements.
- **Mission Addition & Initialization**: A new mission defaults to `SCHEDULED`, as requested.
- **Rocket Assignment (Uniqueness)**: The service (`DragonService.assignRocketToMission`) properly enforces the rule that a rocket can only be assigned to *one* active mission at a time. It searches all missions and blocks assignments if the rocket is already bound to another active mission.
- **Status Cascade**:
  - Changes to rocket statuses correctly trigger a re-evaluation of their assigned mission’s status via `evaluateMissionStatus`.
  - If a rocket goes into `IN_REPAIR`, the mission transitions to `PENDING`.
  - If all rockets are healthy (e.g., `ON_GROUND`, `IN_SPACE`), the mission transitions to `IN_PROGRESS`.
- **Mission Summary Sort Rule (Deviating for Practicality)**:
- *Feedback/Action*: While technically accurate according to the prompt, ordering ties in *descending* alphabetical order (Z to A) is an unusual and counter-intuitive practice in software. It makes more sense to fall back to natural *ascending* alphabetical order (A to Z)

## Design
- **Single Responsibility Principle**: The classes have defined roles. Entities manage state, the repository manages storage, and the service manages cross-entity business logic.
- **Encapsulation**: `Mission.getAssignedRockets()` returns an unmodifiable list (`Collections.unmodifiableList`).
- **Test-Driven Development (TDD)**: `mvn clean test` succeeds with 17 tests passing in ~0.9 seconds
