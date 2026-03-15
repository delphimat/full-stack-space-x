import { Component } from '@angular/core';
import { LaunchpadList } from './launchpad-list/launchpad-list';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [LaunchpadList],
  template: `<app-launchpad-list></app-launchpad-list>`,
})
export class App {
  protected title = 'spacex-launchpads';
}
