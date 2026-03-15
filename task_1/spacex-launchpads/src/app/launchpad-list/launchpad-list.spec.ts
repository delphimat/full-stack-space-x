import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LaunchpadList } from './launchpad-list';

describe('LaunchpadList', () => {
  let component: LaunchpadList;
  let fixture: ComponentFixture<LaunchpadList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LaunchpadList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LaunchpadList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
