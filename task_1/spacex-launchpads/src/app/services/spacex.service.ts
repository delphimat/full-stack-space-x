import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Launchpad, PaginatedResponse } from '../models/spacex.models';
import { environment } from '../../environments/environment';

interface MongoQueryPayload {
  query: Record<string, any>;
  options: {
    page: number;
    limit: number;
    populate?: Array<{
      path: string;
      select?: Record<string, number>;
      options?: {
        sort?: Record<string, number>;
      };
    }>;
  };
}

@Injectable({
  providedIn: 'root'
})
export class SpacexService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  /**
   * Fetches launchpads using the query endpoint.
   * This allows for server-side pagination, filtering, and populating the launches array.
   *
   * @param page The page number (1-indexed).
   * @param limit The number of items per page.
   * @param filterTerm An optional search term to filter by name or region.
   */
  getLaunchpads(page: number = 1, limit: number = 5, filterTerm: string = ''): Observable<PaginatedResponse<Launchpad>> {
    const queryPayload: MongoQueryPayload = {
      query: {},
      options: {
        page,
        limit,
        populate: [
          {
            path: 'launches',
            select: {
              name: 1,
              flight_number: 1,
              date_utc: 1,
              id: 1
        },
        options: {
          sort: { date_utc: -1 }
            }
          }
        ]
      }
    };

    if (filterTerm) {
      const escapedTerm = this.escapeRegExp(filterTerm);
      // Use an $or operator to search in both 'name' and 'region'
      queryPayload.query = {
        $or: [
          { name: { $regex: escapedTerm, $options: 'i' } },
          { region: { $regex: escapedTerm, $options: 'i' } }
        ]
      };
    }

    return this.http.post<PaginatedResponse<Launchpad>>(`${this.apiUrl}/launchpads/query`, queryPayload);
  }

  /**
   * Escapes special characters for use in a regular expression.
   */
  private escapeRegExp(text: string): string {
    return text.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
  }
}
