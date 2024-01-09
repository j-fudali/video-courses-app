import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from './shared/data-access/auth.service';
interface Link {
  name: string;
  path: string;
  isAdmin: boolean;
  isActive: boolean;
}
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    RouterModule,
    MatIconModule,
    MatButtonModule,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  private breakpointObserver = inject(BreakpointObserver);
  private destroyRef = inject(DestroyRef);
  private authService = inject(AuthService);
  isLoggedIn = this.authService.$isLoggedIn;
  sidenavOpened = false;
  title: string = 'Video Courses';
  isGtSm: boolean = false;
  links: Link[] = [
    {
      name: 'My courses',
      path: '/me/courses',
      isAdmin: false,
      isActive: false,
    },
  ];
  ngOnInit(): void {
    this.authService.checkIsLoggedIn();
    this.breakpointObserver
      .observe([Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge])
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((v) => {
        if (v.matches) {
          this.closeSidenav();
        } else {
        }
        this.isGtSm = v.matches;
      });
  }

  toggleSidenav() {
    this.sidenavOpened = !this.sidenavOpened;
  }
  closeSidenav() {
    this.sidenavOpened = false;
  }

  logout() {
    this.authService.logout();
  }
}
