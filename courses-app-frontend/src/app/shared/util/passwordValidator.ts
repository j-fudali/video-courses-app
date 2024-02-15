import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const confirmPasswordValidator = (
  fieldOne: string,
  fieldTwo: string
): ValidatorFn => {
  return (group: AbstractControl) => {
    const passwordVal = group.get(fieldOne)?.value;
    const rePasswordVal = group.get(fieldTwo)?.value;
    return passwordVal === rePasswordVal ? null : { notSame: true };
  };
};
