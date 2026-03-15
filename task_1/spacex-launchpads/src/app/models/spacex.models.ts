export interface Launch {
  id: string;
  name: string;
  flight_number: number;
  date_utc: string;
}

export interface Launchpad {
  id: string;
  name: string;
  full_name?: string;
  region: string;
  locality: string;
  status: string;
  wikipedia: string;
  details: string;
  images: {
    large: string[];
  };
  launches: Launch[];
}

export interface PaginatedResponse<T> {
  docs: T[];
  totalDocs: number;
  limit: number;
  totalPages: number;
  page: number;
  pagingCounter: number;
  hasPrevPage: boolean;
  hasNextPage: boolean;
  prevPage: number | null;
  nextPage: number | null;
}
