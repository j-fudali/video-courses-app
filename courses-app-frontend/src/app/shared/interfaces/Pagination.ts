export interface Pagination<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
