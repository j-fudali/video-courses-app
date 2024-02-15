import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-shopping-cart-button',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  template: `
    @if(isMini){
    <button
      (click)="addToShoppingCart()"
      [disabled]="disabled"
      mat-mini-fab
      [color]="color"
    >
      @if(isAvailable){
      <mat-icon>shopping_cart</mat-icon>

      }@else{
      <mat-icon>add_shopping_cart</mat-icon>
      }
    </button>

    }@else {
    <button
      (click)="addToShoppingCart()"
      [disabled]="disabled"
      mat-fab
      [color]="color"
    >
      @if(isAvailable){
      <mat-icon>shopping_cart</mat-icon>

      }@else{
      <mat-icon>add_shopping_cart</mat-icon>
      }
    </button>

    }
  `,
  styleUrl: './shopping-cart-button.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ShoppingCartButtonComponent {
  @Input({ required: true }) disabled: boolean;
  @Input({ required: true }) isAvailable: boolean;
  @Input() isMini: boolean = true;
  @Input() color: 'primary' | 'accent' = 'primary';
  @Output() onAddToShoppingCart = new EventEmitter<void>();
  addToShoppingCart() {
    this.onAddToShoppingCart.emit();
  }
}
