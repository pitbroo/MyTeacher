import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITimeShit, TimeShit } from '../time-shit.model';
import { TimeShitService } from '../service/time-shit.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-time-shit-update',
  templateUrl: './time-shit-update.component.html',
})
export class TimeShitUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    present: [],
    date: [],
    user: [],
  });

  constructor(
    protected timeShitService: TimeShitService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ timeShit }) => {
      this.updateForm(timeShit);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const timeShit = this.createFromForm();
    if (timeShit.id !== undefined) {
      this.subscribeToSaveResponse(this.timeShitService.update(timeShit));
    } else {
      this.subscribeToSaveResponse(this.timeShitService.create(timeShit));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITimeShit>>): void {
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

  protected updateForm(timeShit: ITimeShit): void {
    this.editForm.patchValue({
      id: timeShit.id,
      present: timeShit.present,
      date: timeShit.date,
      user: timeShit.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, timeShit.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ITimeShit {
    return {
      ...new TimeShit(),
      id: this.editForm.get(['id'])!.value,
      present: this.editForm.get(['present'])!.value,
      date: this.editForm.get(['date'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
