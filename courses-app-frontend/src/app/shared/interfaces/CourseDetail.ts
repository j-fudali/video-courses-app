import { Category } from './Category';
import { Creator } from './Creator';
import { Lesson } from './Lesson';

export interface CourseDetail {
  name: string;
  description: string;
  creator: Creator;
  cost: number;
  category: Category;
  isBought?: boolean;
  lessons: Lesson[];
}
