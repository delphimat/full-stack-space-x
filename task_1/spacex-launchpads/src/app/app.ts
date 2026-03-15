import { Component } from '@angular/core';
import { LaunchpadList } from './launchpad-list/launchpad-list';

@Component({
  selector: 'app-root',
  imports: [LaunchpadList],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'spacex-launchpads';
}
