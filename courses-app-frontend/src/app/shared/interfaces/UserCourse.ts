import { Category } from './Category';

export interface UserCourse {
  idcourse: number;
  name: string;
  category: Category;
  completed: boolean;
}
