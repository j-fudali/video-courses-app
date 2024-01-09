import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatMenuModule } from '@angular/material/menu';
import { MatListModule } from '@angular/material/list';
import { Category } from '../../interfaces/Category';
import { SearchFilters } from '../../interfaces/SearchFilters';
import { MatChipsModule } from '@angular/material/chips';
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
    MatMenuModule,
    MatListModule,
    ReactiveFormsModule,
    MatChipsModule,
  ],
  templateUrl: './search-courses.component.html',
  styleUrl: './search-courses.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SearchCoursesComponent implements OnInit {
  @Input() initialSearchValue: string;
  @Input() initialCategoryValue: string;
  @Input({ required: true }) categories: Category[] | null;
  @Input() showCategoryChip = true;
  @Output() searchEmit = new EventEmitter<SearchFilters>();
  @Output() showAllEmit = new EventEmitter<void>();
  searchName = new FormControl('');
  category = new FormControl<string[]>([]);

  ngOnInit(): void {
    this.searchName.setValue(this.initialSearchValue);
    this.category.setValue([this.initialCategoryValue]);
  }
  cancelCategory() {
    this.category.reset();
  }
  cancelSearchName() {
    this.searchName.reset();
  }
  showAll() {
    this.searchName.setValue('');
    this.cancelCategory();
    this.showAllEmit.emit();
  }
  search() {
    const name = this.searchName.value;
    const category = this.category.value;
    if (name && name != '') {
      if (category && category.length > 0) {
        this.searchEmit.emit({ name, category: category[0] });
      } else {
        this.searchEmit.emit({ name });
      }
    } else {
      if (category && category.length > 0)
        this.searchEmit.emit({ category: category[0] });
    }
  }
}
