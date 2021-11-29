import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILesson, Lesson } from '../lesson.model';
import { LessonService } from '../service/lesson.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

@Component({
  selector: 'jhi-lesson-update',
  templateUrl: './lesson-update.component.html',
})
export class LessonUpdateComponent implements OnInit {
  isSaving = false;

  coursesSharedCollection: ICourse[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    dateStart: [],
    dateEnd: [],
    classroomOrAddres: [],
    course: [],
  });

  constructor(
    protected lessonService: LessonService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lesson }) => {
      this.updateForm(lesson);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lesson = this.createFromForm();
    if (lesson.id !== undefined) {
      this.subscribeToSaveResponse(this.lessonService.update(lesson));
    } else {
      this.subscribeToSaveResponse(this.lessonService.create(lesson));
    }
  }

  trackCourseById(index: number, item: ICourse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILesson>>): void {
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

  protected updateForm(lesson: ILesson): void {
    this.editForm.patchValue({
      id: lesson.id,
      status: lesson.status,
      dateStart: lesson.dateStart,
      dateEnd: lesson.dateEnd,
      classroomOrAddres: lesson.classroomOrAddres,
      course: lesson.course,
    });

    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(this.coursesSharedCollection, lesson.course);
  }

  protected loadRelationshipsOptions(): void {
    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing(courses, this.editForm.get('course')!.value)))
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }

  protected createFromForm(): ILesson {
    return {
      ...new Lesson(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      dateStart: this.editForm.get(['dateStart'])!.value,
      dateEnd: this.editForm.get(['dateEnd'])!.value,
      classroomOrAddres: this.editForm.get(['classroomOrAddres'])!.value,
      course: this.editForm.get(['course'])!.value,
    };
  }
}
