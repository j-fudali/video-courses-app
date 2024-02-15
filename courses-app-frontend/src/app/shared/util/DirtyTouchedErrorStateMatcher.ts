import { AbstractControl, FormGroupDirective, NgForm } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';

export class DirtyTouchedErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(
    control: AbstractControl<any, any> | null,
    form: FormGroupDirective | NgForm | null
  ): boolean {
    return form && form.hasError('notSame')
      ? !!(
          form &&
          form.hasError('notSame') &&
          control &&
          (control.touched || control.dirty)
        )
      : !!(control && control.invalid && (control.touched || control.dirty));
  }
}
