import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize, map} from 'rxjs/operators';

import {ITaskSolved, TaskSolved} from '../task-solved.model';
import {TaskSolvedService} from '../service/task-solved.service';
import {AlertError} from 'app/shared/alert/alert-error.model';
import {EventManager, EventWithContent} from 'app/core/util/event-manager.service';
import {DataUtils, FileLoadError} from 'app/core/util/data-util.service';
import {IUser} from 'app/entities/user/user.model';
import {UserService} from 'app/entities/user/user.service';
import {ITask} from 'app/entities/task/task.model';
import {TaskService} from 'app/entities/task/service/task.service';

@Component({
  selector: 'jhi-task-solved-update',
  templateUrl: './task-solved-update.component.html',
})
export class TaskSolvedUpdateComponent implements OnInit {
  isSaving = false;
  usersSharedCollection: IUser[] = [];
  tasksSharedCollection: ITask[] = [];
  taskId?: number;
  paramSub?: any;

  editForm = this.fb.group({
    id: [],
    pointGrade: [],
    content: [],
    deadline: [],
    sendDay: [],
    answer: [],
    attachment: [],
    attachmentContentType: [],
    user: [],
    task: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected taskSolvedService: TaskSolvedService,
    protected userService: UserService,
    protected taskService: TaskService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({taskSolved}) => {
      this.updateForm(taskSolved);

      this.loadRelationshipsOptions();
      this.paramSub = this.activatedRoute.params.subscribe(
        params => this.taskId = parseInt(params['taskId'], 10));
    });
  }
  ngOnDestroy(): void {
    this.paramSub.unsubscribe();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('myTeacherApp.error', {
          ...err,
          key: 'error.file.' + err.key
        })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const taskSolved = this.createFromForm();
    if (taskSolved.id !== undefined) {
      this.subscribeToSaveResponse(this.taskSolvedService.update(taskSolved));
    } else {
      this.subscribeToSaveResponse(this.taskSolvedService.create(taskSolved));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackTaskById(index: number, item: ITask): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskSolved>>): void {
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

  protected updateForm(taskSolved: ITaskSolved): void {
    this.editForm.patchValue({
      id: taskSolved.id,
      pointGrade: taskSolved.pointGrade,
      content: taskSolved.content,
      deadline: taskSolved.deadline,
      sendDay: taskSolved.sendDay,
      answer: taskSolved.answer,
      attachment: taskSolved.attachment,
      attachmentContentType: taskSolved.attachmentContentType,
      user: taskSolved.user,
      task: taskSolved.task,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, taskSolved.user);
    this.tasksSharedCollection = this.taskService.addTaskToCollectionIfMissing(this.tasksSharedCollection, taskSolved.task);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.taskService
      .query()
      .pipe(map((res: HttpResponse<ITask[]>) => res.body ?? []))
      .pipe(map((tasks: ITask[]) => this.taskService.addTaskToCollectionIfMissing(tasks, this.editForm.get('task')!.value)))
      .subscribe((tasks: ITask[]) => (this.tasksSharedCollection = tasks));
  }

  protected createFromForm(): ITaskSolved {
    return {
      ...new TaskSolved(),
      id: this.editForm.get(['id'])!.value,
      pointGrade: this.editForm.get(['pointGrade'])!.value,
      content: this.editForm.get(['content'])!.value,
      deadline: this.editForm.get(['deadline'])!.value,
      sendDay: this.editForm.get(['sendDay'])!.value,
      answer: this.editForm.get(['answer'])!.value,
      attachmentContentType: this.editForm.get(['attachmentContentType'])!.value,
      attachment: this.editForm.get(['attachment'])!.value,
      user: this.editForm.get(['user'])!.value,
      // task: this.editForm.get(['task'])!.value,
      task: this.getTaskById(this.taskId),
    };
  }

  private getTaskById(taskId: number | undefined): ITask | undefined {
    return this.tasksSharedCollection.find(task => task.id === taskId);
  }
}
