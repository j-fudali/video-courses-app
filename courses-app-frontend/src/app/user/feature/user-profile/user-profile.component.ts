import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    CommonModule,
  ],
  template: `<p>user-profile works!</p>`,
  styleUrl: './user-profile.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserProfileComponent { }
