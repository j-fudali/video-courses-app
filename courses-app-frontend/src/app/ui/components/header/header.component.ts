import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
  Signal,
} from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Course } from '../../../shared/interfaces/Course';
import { RouterModule } from '@angular/router';
import { Link } from '../../../shared/interfaces/Link';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    MatMenuModule,
    MatListModule,
    MatIconModule,
    MatToolbarModule,
    MatBadgeModule,
    RouterModule,
    MatButtonModule,
  ],
  template: `
    <mat-toolbar
      [style.fontSize]="isGtSm ? '12px' : '16px'"
      class="mat-elevation-z4"
      color="primary"
    >
      <div>
        @if (!isGtSm && isLoggedIn()) {
        <button (click)="toggleSidenav()" mat-icon-button>
          <mat-icon>menu</mat-icon>
        </button>
        }
        <a
          routerLink="/home"
          mat-button
          [style.fontSize]="isGtSm ? '24px' : '16px'"
          >{{ title }}</a
        >
      </div>
      <div>
        @if (isLoggedIn()) { @if(isGtSm){ @for (link of links; track link.name)
        { @if((link.isAdmin && isAdmin()) || !link.isAdmin){
        <a [routerLink]="[link.path]" routerLinkActive="active" mat-button>{{
          link.name
        }}</a>

        } } }
        <button
          mat-icon-button
          [matMenuTriggerData]="{ courses: cart }"
          [matMenuTriggerFor]="shoppingCart"
        >
          <mat-icon
            [matBadgeHidden]="cart().length == 0"
            matBadgeColor="accent"
            [matBadge]="cart().length"
            aria-hidden="false"
            >shopping_cart</mat-icon
          >
        </button>
        <mat-menu #shoppingCart xPosition="before">
          <ng-template matMenuContent let-courses="courses">
            <mat-nav-list>
              @for (course of courses(); track course.idcourse) {
              <mat-list-item [routerLink]="['/courses', course.idcourse]">
                <a matListItemTitle>{{ course.name }}</a>
                <button
                  matListItemMeta
                  mat-icon-button
                  (click)="removeFromCart(course.idcourse, $event)"
                >
                  <mat-icon>remove</mat-icon>
                </button>
              </mat-list-item>
              }@empty {
              <mat-list-item>Cart is empty</mat-list-item>
              }
            </mat-nav-list>
            @if(courses().length> 0){
            <a routerLink="/purchase" mat-menu-item>
              <mat-icon>money</mat-icon>
              <span>Proceed</span>
            </a>
            }
          </ng-template>
        </mat-menu>

        <button mat-raised-button (click)="logout()">Logout</button>
        } @else {
        <a routerLink="/login" mat-stroked-button>Login</a>
        <a color="accent" routerLink="/sign-up" mat-raised-button>Sign-up</a>
        }
      </div>
    </mat-toolbar>
  `,
  styleUrl: './header.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HeaderComponent {
  @Input({ required: true }) isGtSm: boolean | null;
  @Input({ required: true }) cart: Signal<Course[]>;
  @Input({ required: true }) isLoggedIn: Signal<boolean>;
  @Input({ required: true }) title: string;
  @Input({ required: true }) links: Link[];
  @Input({ required: true }) isAdmin: Signal<boolean>;

  @Output() onToggleSidenav = new EventEmitter<void>();
  @Output() onRemoveFromCart = new EventEmitter<number>();
  @Output() onLogout = new EventEmitter<void>();
  toggleSidenav() {
    this.onToggleSidenav.emit();
  }
  removeFromCart(courseId: number, e: Event) {
    e.stopPropagation();
    this.onRemoveFromCart.emit(courseId);
  }
  logout() {
    this.onLogout.emit();
  }
}
