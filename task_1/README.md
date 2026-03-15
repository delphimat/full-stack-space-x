# SpaceX Launchpads

A presentation-ready Angular web application that displays information about SpaceX launchpads using the official [SpaceX API](https://github.com/r-spacex/SpaceX-API).

## Features

- **Launchpad Overview:** View detailed information about SpaceX launchpads, including name, region, status, and description.
- **Launch History:** Expandable list of recent launches for each launchpad, detailing mission names, flight numbers, and dates.
- **Server-Side Pagination:** Efficiently browse through launchpads with customizable page sizes (defaulting to 5).
- **Server-Side Search:** Real-time debounced filtering by launchpad name or region.
- **Responsive Design:** A polished grid layout utilizing Angular Material components that adapts to different screen sizes.

## Tech Stack

- Angular 19
- Angular Material (UI Components)
- SCSS
- RxJS (Debouncing and Data Streams)
- Karma/Jasmine (Testing)