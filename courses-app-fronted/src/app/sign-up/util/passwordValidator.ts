import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function passwordsMatchValidator(): ValidationErrors | null {
  return (rePassword: AbstractControl) => {
    const passwordVal = rePassword.parent?.get('password')?.value;
    const rePasswordVal = rePassword.value;
    return passwordVal === rePasswordVal ? null : { notSame: true };
  };
}
