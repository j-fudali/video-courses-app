import { Answer } from './Answer';

export interface Question {
  idquestion: number;
  text: string;
  answers: Answer[];
}
