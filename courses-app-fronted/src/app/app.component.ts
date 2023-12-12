import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { SidenavService } from './shared/data-access/sidenav.service';
import { MatListModule } from '@angular/material/list';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
interface Link {
  name: string;
  path: string;
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
  private sidenavService = inject(SidenavService);
  private breakpointObserver = inject(BreakpointObserver);
  private destroyRef = inject(DestroyRef);

  title: string = 'Video Courses App';
  sidenavOpened = this.sidenavService.sidenavOpened;
  isGtSm: boolean = false;
  links: Link[] = [
    { name: 'Home', path: '/home', isActive: true },
    { name: 'Courses', path: '/courses', isActive: false },
    { name: 'Login', path: '/login', isActive: false },
    { name: 'Sign-up', path: '/sign-up', isActive: false },
  ];

  toggleSidenav() {
    this.sidenavService.toogleSidenav();
  }
  closeSidenav() {
    this.sidenavService.closeSidenav();
  }
  ngOnInit(): void {
    this.breakpointObserver
      .observe([Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge])
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((v) => {
        if (!v.matches) this.sidenavService.closeSidenav();
        this.isGtSm = v.matches;
      });
  }
}
