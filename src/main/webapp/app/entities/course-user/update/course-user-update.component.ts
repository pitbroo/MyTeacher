import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICourseUser, CourseUser } from '../course-user.model';
import { CourseUserService } from '../service/course-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

@Component({
  selector: 'jhi-course-user-update',
  templateUrl: './course-user-update.component.html',
})
export class CourseUserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  coursesSharedCollection: ICourse[] = [];

  editForm = this.fb.group({
    id: [],
    user: [],
    course: [],
  });

  constructor(
    protected courseUserService: CourseUserService,
    protected userService: UserService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseUser }) => {
      this.updateForm(courseUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseUser = this.createFromForm();
    if (courseUser.id !== undefined) {
      this.subscribeToSaveResponse(this.courseUserService.update(courseUser));
    } else {
      this.subscribeToSaveResponse(this.courseUserService.create(courseUser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackCourseById(index: number, item: ICourse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(courseUser: ICourseUser): void {
    this.editForm.patchValue({
      id: courseUser.id,
      user: courseUser.user,
      course: courseUser.course,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, courseUser.user);
    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(this.coursesSharedCollection, courseUser.course);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing(courses, this.editForm.get('course')!.value)))
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }

  protected createFromForm(): ICourseUser {
    return {
      ...new CourseUser(),
      id: this.editForm.get(['id'])!.value,
      user: this.editForm.get(['user'])!.value,
      course: this.editForm.get(['course'])!.value,
    };
  }
}
