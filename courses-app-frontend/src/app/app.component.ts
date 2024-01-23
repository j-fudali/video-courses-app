import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AuthService } from './shared/data-access/auth.service';
import { ShoppingCartService } from './shared/data-access/shopping-cart.service';
import { Link } from './shared/interfaces/Link';
import { HeaderComponent } from './ui/components/header/header.component';
import { map, tap } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { UserRole } from './shared/util/roles.enum';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    MatListModule,
    RouterModule,
    HeaderComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  private breakpointObserver = inject(BreakpointObserver);
  private authService = inject(AuthService);
  private shoppinCartService = inject(ShoppingCartService);
  isAdmin = this.authService.$isAdmin;
  cart = this.shoppinCartService.coursesInCart;
  isLoggedIn = this.authService.$isLoggedIn;
  sidenavOpened = false;
  title: string = 'Video Courses';
  isGtSm$ = this.breakpointObserver
    .observe([Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge])
    .pipe(
      map((v) => v.matches),
      tap((m) => (m ? this.closeSidenav() : null))
    );
  links: Link[] = [
    {
      name: 'My courses',
      path: '/me/courses',
      isAdmin: false,
      isActive: false,
    },
    {
      name: 'Create course',
      path: '/editor',
      isAdmin: true,
      isActive: false,
    },
  ];
  ngOnInit(): void {
    this.authService.checkIsLoggedIn();
    this.authService.checkIsAdmin();
    this.shoppinCartService.loadStoredCourses();
  }

  toggleSidenav() {
    this.sidenavOpened = !this.sidenavOpened;
  }
  closeSidenav() {
    this.sidenavOpened = false;
  }
  removeFromCart(courseId: number) {
    this.shoppinCartService.removeFromCart(courseId);
  }

  logout() {
    this.shoppinCartService.clearCart();
    this.authService.logout();
  }
}
