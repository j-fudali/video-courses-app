import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, ViewChild, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule, MatListOption } from '@angular/material/list';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { RouterModule } from '@angular/router';
import { ShoppingCartService } from '../../shared/data-access/shopping-cart.service';
import { PurchaseService } from '../data-access/purchase.service';
import { Observable, catchError, forkJoin, map, of, tap } from 'rxjs';
import { SelectionModel } from '@angular/cdk/collections';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
@Component({
  selector: 'app-purchase',
  standalone: true,
  imports: [
    CommonModule,
    MatStepperModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    RouterModule,
    MatProgressSpinnerModule,
  ],
  template: `
    @if(isLsMd$ | async; as bp){
    <mat-stepper
      linear
      animationDuration="0"
      #stepper
      [style.width]="bp.matches ? '100%' : '60%'"
      [orientation]="bp.matches ? 'vertical' : 'horizontal'"
    >
      <mat-step
        #chooseCourses
        label="Select courses"
        [completed]="
          selectedCourses.selectedOptions.selected.length > 0 ||
          stepper.selectedIndex === 2
        "
        [editable]="!(stepper.selectedIndex > 1)"
      >
        <div>
          <mat-selection-list #selectedCourses>
            @for(course of cart(); track course.idcourse){
            <mat-list-option [value]="course.idcourse">{{
              course.name
            }}</mat-list-option>
            } @empty {
            <mat-list-item class="no-item">No course in cart</mat-list-item>
            }
          </mat-selection-list>
          @if(cart().length > 0){
          <button
            mat-button
            matStepperNext
            [disabled]="!chooseCourses.completed"
          >
            Next
          </button>

          }
        </div>
      </mat-step>
      <mat-step
        #confirmation
        [completed]="purchaseStatus$"
        [editable]="!(stepper.selectedIndex > 1)"
        label="Confirm purchase"
      >
        @if(purchaseStatus$ === undefined){
        <div>
          <h3>Are you sure you want to buy those courses?</h3>
          <div>
            <button mat-button type="button" color="warn" matStepperPrevious>
              No
            </button>
            <button
              mat-button
              type="button"
              color="primary"
              (click)="buy(selectedCourses.selectedOptions)"
            >
              Yes
            </button>
          </div>
        </div>
        } @else {
        <mat-spinner></mat-spinner>
        }
      </mat-step>
      <mat-step label="Completion">
        <div>
          @if(purchaseStatus$ | async; as purchaseStatus){ @if(purchaseStatus
          === true){
          <h3>You bought new courses!</h3>

          }@else {

          <h3>Some courses you already bought</h3>
          }
          <a
            mat-dialog-close
            mat-raised-button
            [routerLink]="['/me/courses']"
            [queryParams]="{ type: 'owned' }"
            >My courses</a
          >

          }
        </div>
      </mat-step>
    </mat-stepper>
    }
  `,
  styleUrl: './purchase.component.scss',
})
export class PurchaseComponent {
  @ViewChild('stepper') stepper: MatStepper;
  private shoppingCartService = inject(ShoppingCartService);
  private purchaseService = inject(PurchaseService);
  private breakpointObs = inject(BreakpointObserver);
  isLsMd$ = this.breakpointObs.observe([Breakpoints.XSmall, Breakpoints.Small]);
  cart = this.shoppingCartService.coursesInCart;
  purchaseStatus$: Observable<boolean | string> | undefined = undefined;
  buy(options: SelectionModel<MatListOption>) {
    const coursesIds = options.selected.map((o) => o.value);
    this.purchaseStatus$ = forkJoin(
      coursesIds.map((cid) => this.purchaseService.buyCourse(cid))
    ).pipe(
      map(() => true),
      catchError((err) => of(err.error.message)),
      tap(() => {
        this.stepper.next();
        coursesIds.forEach((id) => this.shoppingCartService.removeFromCart(id));
      })
    );
  }
}
