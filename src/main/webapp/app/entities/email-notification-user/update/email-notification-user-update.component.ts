import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmailNotificationUser, EmailNotificationUser } from '../email-notification-user.model';
import { EmailNotificationUserService } from '../service/email-notification-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmailNotification } from 'app/entities/email-notification/email-notification.model';
import { EmailNotificationService } from 'app/entities/email-notification/service/email-notification.service';

@Component({
  selector: 'jhi-email-notification-user-update',
  templateUrl: './email-notification-user-update.component.html',
})
export class EmailNotificationUserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  emailNotificationsSharedCollection: IEmailNotification[] = [];

  editForm = this.fb.group({
    id: [],
    user: [],
    emailNotification: [],
  });

  constructor(
    protected emailNotificationUserService: EmailNotificationUserService,
    protected userService: UserService,
    protected emailNotificationService: EmailNotificationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailNotificationUser }) => {
      this.updateForm(emailNotificationUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const emailNotificationUser = this.createFromForm();
    if (emailNotificationUser.id !== undefined) {
      this.subscribeToSaveResponse(this.emailNotificationUserService.update(emailNotificationUser));
    } else {
      this.subscribeToSaveResponse(this.emailNotificationUserService.create(emailNotificationUser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackEmailNotificationById(index: number, item: IEmailNotification): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmailNotificationUser>>): void {
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

  protected updateForm(emailNotificationUser: IEmailNotificationUser): void {
    this.editForm.patchValue({
      id: emailNotificationUser.id,
      user: emailNotificationUser.user,
      emailNotification: emailNotificationUser.emailNotification,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, emailNotificationUser.user);
    this.emailNotificationsSharedCollection = this.emailNotificationService.addEmailNotificationToCollectionIfMissing(
      this.emailNotificationsSharedCollection,
      emailNotificationUser.emailNotification
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.emailNotificationService
      .query()
      .pipe(map((res: HttpResponse<IEmailNotification[]>) => res.body ?? []))
      .pipe(
        map((emailNotifications: IEmailNotification[]) =>
          this.emailNotificationService.addEmailNotificationToCollectionIfMissing(
            emailNotifications,
            this.editForm.get('emailNotification')!.value
          )
        )
      )
      .subscribe((emailNotifications: IEmailNotification[]) => (this.emailNotificationsSharedCollection = emailNotifications));
  }

  protected createFromForm(): IEmailNotificationUser {
    return {
      ...new EmailNotificationUser(),
      id: this.editForm.get(['id'])!.value,
      user: this.editForm.get(['user'])!.value,
      emailNotification: this.editForm.get(['emailNotification'])!.value,
    };
  }
}
