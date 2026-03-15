import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SpacexService } from '../services/spacex.service';
import { Launch, Launchpad, PaginatedResponse } from '../models/spacex.models';
import { Subject, takeUntil, debounceTime, distinctUntilChanged } from 'rxjs';

// Angular Material Imports
import { MatCardModule } from '@angular/material/card';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-launchpad-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatPaginatorModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatExpansionModule,
    MatButtonModule,
    MatSnackBarModule,
    FormsModule
  ],
  templateUrl: './launchpad-list.html',
  styleUrls: ['./launchpad-list.scss']
})
export class LaunchpadList implements OnInit, OnDestroy {
  private spacexService = inject(SpacexService);
  private snackBar = inject(MatSnackBar);
  private destroy$ = new Subject<void>();
  private searchSubject = new Subject<string>();

  launchpads: Launchpad[] = [];
  isLoading = false;
  errorMessage = '';

  // Pagination params
  totalDocs = 0;
  pageSize = 5;
  currentPage = 0;
  pageSizeOptions = [2, 5, 10, 25];

  searchTerm = '';

  ngOnInit(): void {
    // Setup search debounce
    this.searchSubject.pipe(
      debounceTime(400),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(term => {
      this.searchTerm = term;
      this.currentPage = 0;
      this.loadLaunchpads();
    });

    this.loadLaunchpads();
  }

  loadLaunchpads(): void {
    this.isLoading = true;
    this.errorMessage = '';

    const apiPage = this.currentPage + 1;

    this.spacexService.getLaunchpads(apiPage, this.pageSize, this.searchTerm)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: PaginatedResponse<Launchpad>) => {
          this.launchpads = response.docs;
          this.totalDocs = response.totalDocs;
          this.isLoading = false;
        },
        error: (err) => {
          this.errorMessage = 'Failed to load launchpads. Please try again later.';
          this.isLoading = false;
          console.error('Error fetching launchpads:', err);
          this.snackBar.open(this.errorMessage, 'Retry', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'bottom',
          }).onAction().subscribe(() => {
            this.loadLaunchpads();
          });
        }
      });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadLaunchpads();
  }

  onSearchChange(term: string): void {
    this.searchSubject.next(term);
  }

  trackLaunchpad(index: number, item: Launchpad): string {
    return item.id;
  }

  trackLaunch(index: number, item: Launch): string {
    return item.id;
  }

  getWikipediaUrl(pad: Launchpad): string {
    const nameToUse = pad.full_name || pad.name;
    const formattedName = nameToUse.replace(/ /g, '_');
    return `https://en.wikipedia.org/w/index.php?search=${formattedName}`;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
