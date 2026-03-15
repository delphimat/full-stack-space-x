# Task 1: Development of a SpaceX Launchpad Information Application 

## Scenario
Develop a small web application using Angular that displays information about SpaceX launchpadsx.

## Specifications
* Utilize the SpaceX API to fetch data about launchpads.
* Display the launchpad name, region, link to Wikipedia, and a list of launches from each launchpad.
* Implement pagination with configurable records per page (default: 5 launchpads per page).
* Include a filter feature to search by launchpad name and region.
* Use of Angular Material is recommended.
* Be creative; demonstrate your proficiency as a front-end developer.
* Commit the project to a GitHub repository, complete with comprehensive setup and execution instructions.
* Write unit and integration tests for the application to validate functionality.
* API Reference: [SpaceX API Docs](https://github.com/r-spacex/SpaceX-API/tree/master/docs#rspacex-api-docs).

## Requirements

| Requirement | Status | Notes                                                                                                                |
| :--- | :---: |:---------------------------------------------------------------------------------------------------------------------|
| **SpaceX API integration** | ✅ Pass | `POST /v4/launchpads/query`.                                                                                         |
| **Display required data** | ✅ Pass | Launchpad name, region, Wikipedia link, and populated list of launches are all displayed.                            |
| **Pagination with configurable records** | ✅ Pass | Server-side pagination is implemented well; MatPaginator correctly tracks records and defaults to 5.                 |
| **Filter by name and region** | ✅ Pass | Server-side filtering utilizes `$or` query matching both `name` and `region` from mongodb api.                       |
| **Angular Material used** | ✅ Pass | Cards, paginator, input fields, spinners, accordions, buttons, and snackbar.                                         |
| **Creative/Proficiency** | ✅ Pass | RxJS debouncing for search input is an excellent practice to prevent excessive API calls.                            |
| **Setup & execution instructions** | ✅ Pass | Clear prerequisites, clone, install, serve, and test commands provided in the `README.md`.                           |
| **Unit and integration tests** | ✅ Pass | Component and app tests are included and configured with mock data (`jasmine`/`karma`).                              |
