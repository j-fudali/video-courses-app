import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
@Component({
  selector: 'app-search-courses',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    MatDividerModule,
  ],
  template: `
    <mat-form-field appearance="outline" color="primary">
      <input matInput [(ngModel)]="searchText" type="text" />
      <button mat-icon-button matSuffix>
        <mat-icon>search</mat-icon>
      </button>
      <button matPrefix mat-icon-button>
        <mat-icon>filter_list</mat-icon>
      </button>
    </mat-form-field>
  `,
  styleUrl: './search-courses.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SearchCoursesComponent {
  searchText: string = '';
}
