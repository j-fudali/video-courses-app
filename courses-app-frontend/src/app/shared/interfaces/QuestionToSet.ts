export interface QuestionToSet {
  text: string;
  answers: {
    text: string;
    isCorrect: string;
  }[];
}
