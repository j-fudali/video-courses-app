import {
  AbstractControl,
  FormArray,
  FormGroup,
  ValidatorFn,
} from '@angular/forms';

export function requriedAnswersSeleceted(numberToSelect: number) {
  return (control: AbstractControl) =>
    (control as FormArray).controls.filter(
      (c) => (c as FormGroup).get('checked')?.value
    ).length >= numberToSelect
      ? null
      : { valid: false };
}
