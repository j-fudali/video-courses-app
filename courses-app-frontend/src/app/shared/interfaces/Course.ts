import { Category } from './Category';
import { Creator } from './Creator';
export interface Course {
  idcourse: number;
  name: string;
  description: string;
  creator: Creator;
  cost: number;
  category: Category;
  isBought?: boolean;
}
