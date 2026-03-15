import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SpacexService } from './spacex.service';

describe('SpacexService', () => {
  let service: SpacexService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SpacexService]
    });
    service = TestBed.inject(SpacexService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get launchpads with correct default pagination payload', () => {
    service.getLaunchpads().subscribe();

    const req = httpMock.expectOne('https://api.spacexdata.com/v4/launchpads/query');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      query: {},
      options: {
        page: 1,
        limit: 5,
        populate: [
          {
            path: 'launches',
            select: {
              name: 1,
              flight_number: 1,
              date_utc: 1,
              id: 1
            }
          }
        ]
      }
    });
    req.flush({ docs: [], totalDocs: 0 });
  });

  it('should include filter term in query when provided', () => {
    service.getLaunchpads(2, 10, 'california').subscribe();

    const req = httpMock.expectOne('https://api.spacexdata.com/v4/launchpads/query');
    expect(req.request.body.query).toEqual({
      $or: [
        { name: { $regex: 'california', $options: 'i' } },
        { region: { $regex: 'california', $options: 'i' } }
      ]
    });
    expect(req.request.body.options.page).toBe(2);
    expect(req.request.body.options.limit).toBe(10);
    req.flush({ docs: [], totalDocs: 0 });
  });

  it('should escape special regex characters in the filter term', () => {
    const specialChars = '.*+?^${}()|[\]\\';
    const escapedChars = '\\.\\*\\+\\?\\^\\$\\{\\}\\(\\)\\|\\[\\]\\\\';

    service.getLaunchpads(1, 5, specialChars).subscribe();

    const req = httpMock.expectOne('https://api.spacexdata.com/v4/launchpads/query');
    expect(req.request.body.query.$or[0].name.$regex).toBe(escapedChars);
    expect(req.request.body.query.$or[1].region.$regex).toBe(escapedChars);
    req.flush({ docs: [], totalDocs: 0 });
  });
});
