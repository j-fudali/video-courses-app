import { Question } from './Question';

export interface Quiz {
  title: string;
  questions: Question[];
  isPassed: boolean;
}
