# SpaceX Launchpads

A presentation-ready Angular web application that displays information about SpaceX launchpads using the official [SpaceX API](https://github.com/r-spacex/SpaceX-API).

## Features

- **Launchpad Overview:** View detailed information about SpaceX launchpads, including name, region, status, and description.
- **Launch History:** Expandable list of recent launches for each launchpad, detailing mission names, flight numbers, and dates.
- **Server-Side Pagination:** Efficiently browse through launchpads with customizable page sizes (defaulting to 5).
- **Server-Side Search:** Real-time debounced filtering by launchpad name or region.
- **Responsive Design:** A polished grid layout utilizing Angular Material components that adapts to different screen sizes.

## Tech Stack

- Angular 20
- Angular Material (UI Components)
- SCSS
- RxJS (Debouncing and Data Streams)
- Karma/Jasmine (Testing)

## Prerequisites

Before you begin, ensure you have the following installed:
- [Node.js](https://nodejs.org/) (v18 or higher recommended)
- [npm](https://www.npmjs.com/) (comes with Node.js)
## Setup Instructions

1. **Clone the repository (if applicable) and navigate to the project directory:**
   ```bash
   cd spacex-launchpads
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

## Running the Application

To start the development server:

```bash
npm start
```

Open your browser and navigate to `http://localhost:4200/`.

The application will automatically reload if you change any of the source files.

## Running Tests

The application includes a comprehensive test suite for services and components. To run the unit tests via Karma:

```bash
npm run test
```
or
```bash
npx ng test --watch=false
```
