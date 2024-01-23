import { AbstractControl, FormArray, ValidationErrors } from '@angular/forms';

export function minimumOneAnswerCorrect(
  answers: AbstractControl
): ValidationErrors | null {
  const answersArr = answers as FormArray;
  return answersArr.controls.filter((g) => g.get('isCorrect')?.value === true)
    .length > 0
    ? null
    : { notValid: true };
}
