import { Injectable, signal } from '@angular/core';
import { Course } from '../interfaces/Course';

@Injectable({
  providedIn: 'root',
})
export class ShoppingCartService {
  private readonly _coursesInCart = signal<Course[]>([]);
  readonly coursesInCart = this._coursesInCart.asReadonly();
  private getStoredCourses(): Course[] {
    const storedCourses = localStorage.getItem('storedCourses');
    return storedCourses ? JSON.parse(storedCourses) : [];
  }
  loadStoredCourses() {
    this._coursesInCart.set(this.getStoredCourses());
  }
  clearCart() {
    this._coursesInCart.set([]);
    localStorage.removeItem('storedCourses');
  }
  storeCourses(courses: Course[]) {
    localStorage.setItem('storedCourses', JSON.stringify(courses));
  }
  addToCart(course: Course) {
    this._coursesInCart.update((v) => [...v, course]);
    this.storeCourses(this.coursesInCart());
  }
  removeFromCart(courseId: number) {
    this._coursesInCart.update((v) =>
      v.filter((course) => course.idcourse != courseId)
    );
    this.storeCourses(this.coursesInCart());
  }
}
