import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Category } from '../../../shared/interfaces/Category';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-course-details-form',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatButtonModule,
  ],
  template: `
    <form [formGroup]="courseDetailsGroup">
      <mat-form-field>
        <mat-label>Course name</mat-label>
        <input type="text" matInput formControlName="name" />
        @if(name && name.hasError('required')){
        <mat-error>Field is required</mat-error>
        } @if(name && name.hasError('maxlength')){
        <mat-error>Max. 45 characters</mat-error>
        }
      </mat-form-field>
      <mat-form-field>
        <mat-label>Category</mat-label>
        <mat-select formControlName="categoryId">
          @for (category of categories; track category.idcategory) {
          <mat-option [value]="category.idcategory">{{
            category.name
          }}</mat-option>
          } @empty {
          <mat-option>No categories</mat-option>
          }
        </mat-select>
        @if(category && category.hasError('required')){
        <mat-error>Field is required</mat-error>
        }
      </mat-form-field>
      <mat-form-field>
        <mat-label>Cost</mat-label>
        <input type="number" matInput formControlName="cost" step="0.01" />
        @if(cost && cost.hasError('required')){
        <mat-error>Field is required</mat-error>
        } @if(cost && cost.hasError('min')){
        <mat-error>Minimal cost must be 0.01</mat-error>
        }
      </mat-form-field>
      <mat-form-field class="description">
        <mat-label>Description</mat-label>
        <textarea matInput formControlName="description"></textarea>
        <mat-hint>
          <span
            [style.color]="
              description && description.value.length > 500 ? 'red' : '#000'
            "
            >{{ description ? description.value.length : 0 }}</span
          ><span>/500</span></mat-hint
        >
        @if(description && description.hasError('required')){
        <mat-error>Field is required</mat-error>
        } @if(description && description.hasError('maxlength')){
        <mat-error>Max. 500 characters</mat-error>
        }
      </mat-form-field>
    </form>
  `,
  styleUrl: './course-details-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CourseDetailsFormComponent {
  @Input({ required: true }) courseDetailsGroup: FormGroup;
  @Input({ required: true }) categories: Category[] | null;
  get name() {
    return this.courseDetailsGroup.get('name');
  }
  get cost() {
    return this.courseDetailsGroup.get('cost');
  }
  get category() {
    return this.courseDetailsGroup.get('category');
  }
  get description() {
    return this.courseDetailsGroup.get('description');
  }
}
