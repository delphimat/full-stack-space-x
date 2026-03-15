import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { LaunchpadList } from './launchpad-list';
import { SpacexService } from '../services/spacex.service';
import { of } from 'rxjs';
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
});
