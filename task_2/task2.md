# Task 2: Development of a SpaceX Dragon Rockets Repository 

## Instruction
Provide the implementation of the SpaceX Dragon Rockets repository as a simple library written in Java.

## Specifications
* Keep it simple. Stick to the requirements and implement the simplest solution that works while considering edge cases.
* Use an in-memory store (collections) to store information.
* It should be a library, NOT a REST API, Web Service, or Microservice.
* No frameworks required.
* Focus on quality, use Test-Driven Development (TDD), pay attention to OO design, clean code, and adherence to SOLID principles.
* Share your solution via a remote repository (GitHub, GitLab, etc.) - commit history is important!.
* Add a `README.md` file with assumptions, specifications, and notes about your approach.

## Requirements
* Add a new rocket (initial status: On ground).
* Assign a rocket to a mission (a rocket can only be assigned to one mission).
* Change rocket status.
* Add a new mission (initial status: Scheduled).
* Assign rockets to a mission (a mission can have multiple rockets).
* Change mission status.
* Get a summary of missions by number of rockets assigned.
* Missions with the same number of rockets should be ordered in descending alphabetical order.

## Statuses

**Rocket:**
* On ground (initial) 
* In space
* In repair (implies mission status = Pending)

**Mission:**
* Scheduled (initial)
* Pending (at least one rocket in repair)
* In Progress (at least one rocket assigned, none in repair)
* Ended (final stage, no more rocket assignments)

## Example Summary
Having the following data in the system:
* Mars - Scheduled - Dragons: 0 
* Luna1 - Pending - Dragons: 2
    * Dragon 1 - On ground 
    * Dragon 2 - On ground
* Double Landing - Ended - Dragons: 0 
* Transit - In progress - Dragons: 3 
    * Red Dragon - On ground
    * Dragon XL - In space 
    * Falcon Heavy - In space
* Luna2 - Scheduled - Dragons: 0
* Vertical Landing - Ended - Dragons: 0

The summary of the data should look like:
* Transit - In progress - Dragons: 3
    * Red Dragon - On ground
    * Dragon XL - In space
    * Falcon Heavy - In space
* Luna1 - Pending - Dragons: 2
    * Dragon 1 - On ground
    * Dragon 2 - On ground
* Vertical Landing - Ended - Dragons: 0
* Mars - Scheduled - Dragons: 0
* Luna2 - Scheduled - Dragons: 0
* Double Landing - Ended - Dragons: 0