import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class SidenavService {
  sidenavOpened = signal(false);

  toogleSidenav() {
    this.sidenavOpened.update(() => !this.sidenavOpened());
  }
  openSidenav() {
    this.sidenavOpened.set(true);
  }
  closeSidenav() {
    this.sidenavOpened.set(false);
  }
}
