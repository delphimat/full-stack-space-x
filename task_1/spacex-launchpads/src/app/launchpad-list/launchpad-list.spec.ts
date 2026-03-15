import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { LaunchpadList } from './launchpad-list';
import { SpacexService } from '../services/spacex.service';
import {of, throwError} from 'rxjs';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { PaginatedResponse, Launchpad } from '../models/spacex.models';
import { MatSnackBarModule } from '@angular/material/snack-bar';

describe('LaunchpadListComponent', () => {
  let component: LaunchpadList;
  let fixture: ComponentFixture<LaunchpadList>;
  let spacexServiceSpy: jasmine.SpyObj<SpacexService>;

  const mockLaunchpad: Launchpad = {
    id: '1',
    name: 'Ariane flight VA256',
    region: 'Guyana',
    locality: 'French base',
    status: 'retired',
    wikipedia: 'https://en.wikipedia.org/wiki/Ariane_flight_VA256',
    details: 'It was 2021\'s final Ariane flight, its most valuable payload to date, and the 256th Ariane mission..',
    images: { large: ['test-image.jpg'] },
    launches: [
      { id: 'l1', name: 'Ariane 5 Test', flight_number: 1, date_utc: '2021-12-25T12:20:00.000Z' }
    ]
  };

  const mockResponse: PaginatedResponse<Launchpad> = {
    docs: [mockLaunchpad],
    totalDocs: 1,
    limit: 5,
    totalPages: 1,
    page: 1,
    pagingCounter: 1,
    hasPrevPage: false,
    hasNextPage: false,
    prevPage: null,
    nextPage: null
  };

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('SpacexService', ['getLaunchpads']);

    await TestBed.configureTestingModule({
      imports: [
        LaunchpadList,
        MatSnackBarModule
      ],
      providers: [
        { provide: SpacexService, useValue: spy },
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations()
      ]
    })
      .compileComponents();

    spacexServiceSpy = TestBed.inject(SpacexService) as jasmine.SpyObj<SpacexService>;
    spacexServiceSpy.getLaunchpads.and.returnValue(of(mockResponse));
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LaunchpadList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load launchpads on init', () => {
    expect(spacexServiceSpy.getLaunchpads).toHaveBeenCalledWith(1, 5, '');
    expect(component.launchpads.length).toBe(1);
    expect(component.launchpads[0].name).toBe('Ariane flight VA256');
    expect(component.isLoading).toBeFalse();
  });

  it('should handle pagination changes', () => {
    component.onPageChange({ pageIndex: 1, pageSize: 10, length: 1 });
    expect(spacexServiceSpy.getLaunchpads).toHaveBeenCalledWith(2, 10, '');
  });

  it('should debounce search input and trigger load', fakeAsync(() => {
    spacexServiceSpy.getLaunchpads.calls.reset();

    component.onSearchChange('cali');
    tick(200);
    expect(spacexServiceSpy.getLaunchpads).not.toHaveBeenCalled();

    tick(200); // 400ms total
    expect(spacexServiceSpy.getLaunchpads).toHaveBeenCalledWith(1, 5, 'cali');
    expect(component.currentPage).toBe(0);
  }));

  it('should handle API errors gracefully', () => {
    spacexServiceSpy.getLaunchpads.and.returnValue(throwError(() => new Error('API Error')));
    component.loadLaunchpads();

    expect(component.isLoading).toBeFalse();
    expect(component.errorMessage).toBe('Failed to load launchpads. Please try again later.');
  });

  it('should generate correct Wikipedia URL', () => {
    const padWithFullName: any = { full_name: 'Ariana Space' };
    expect(component.getWikipediaUrl(padWithFullName)).toBe('https://en.wikipedia.org/w/index.php?search=Ariana_Space');

    const padWithNameFallback: any = { name: 'flight VA256' };
    expect(component.getWikipediaUrl(padWithNameFallback)).toBe('https://en.wikipedia.org/w/index.php?search=flight_VA256');
  });

});
