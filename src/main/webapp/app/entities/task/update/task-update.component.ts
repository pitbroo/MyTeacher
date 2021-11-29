import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITask, Task } from '../task.model';
import { TaskService } from '../service/task.service';
import { ILesson } from 'app/entities/lesson/lesson.model';
import { LessonService } from 'app/entities/lesson/service/lesson.service';

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html',
})
export class TaskUpdateComponent implements OnInit {
  isSaving = false;

  lessonsSharedCollection: ILesson[] = [];

  editForm = this.fb.group({
    id: [],
    pointGrade: [],
    content: [],
    deadline: [],
    lesson: [],
  });

  constructor(
    protected taskService: TaskService,
    protected lessonService: LessonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      this.updateForm(task);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const task = this.createFromForm();
    if (task.id !== undefined) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  trackLessonById(index: number, item: ILesson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>): void {
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

  protected updateForm(task: ITask): void {
    this.editForm.patchValue({
      id: task.id,
      pointGrade: task.pointGrade,
      content: task.content,
      deadline: task.deadline,
      lesson: task.lesson,
    });

    this.lessonsSharedCollection = this.lessonService.addLessonToCollectionIfMissing(this.lessonsSharedCollection, task.lesson);
  }

  protected loadRelationshipsOptions(): void {
    this.lessonService
      .query()
      .pipe(map((res: HttpResponse<ILesson[]>) => res.body ?? []))
      .pipe(map((lessons: ILesson[]) => this.lessonService.addLessonToCollectionIfMissing(lessons, this.editForm.get('lesson')!.value)))
      .subscribe((lessons: ILesson[]) => (this.lessonsSharedCollection = lessons));
  }

  protected createFromForm(): ITask {
    return {
      ...new Task(),
      id: this.editForm.get(['id'])!.value,
      pointGrade: this.editForm.get(['pointGrade'])!.value,
      content: this.editForm.get(['content'])!.value,
      deadline: this.editForm.get(['deadline'])!.value,
      lesson: this.editForm.get(['lesson'])!.value,
    };
  }
}
