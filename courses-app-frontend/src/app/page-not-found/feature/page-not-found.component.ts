import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-page-not-found',
  standalone: true,
  imports: [CommonModule, MatButtonModule, RouterModule],
  template: `
    <h2>Page not found</h2>
    <a mat-stroked-button routerLink="/">Main page</a>
  `,
  styles: `
    :host {
      min-height: 90vh;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }
    h2{
      font-size: 36px;
    }
  `,
})
export class PageNotFoundComponent {}
