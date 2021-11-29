import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEmailNotification, EmailNotification } from '../email-notification.model';
import { EmailNotificationService } from '../service/email-notification.service';

@Component({
  selector: 'jhi-email-notification-update',
  templateUrl: './email-notification-update.component.html',
})
export class EmailNotificationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    content: [],
    time: [],
    teacher: [],
  });

  constructor(
    protected emailNotificationService: EmailNotificationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailNotification }) => {
      this.updateForm(emailNotification);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const emailNotification = this.createFromForm();
    if (emailNotification.id !== undefined) {
      this.subscribeToSaveResponse(this.emailNotificationService.update(emailNotification));
    } else {
      this.subscribeToSaveResponse(this.emailNotificationService.create(emailNotification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmailNotification>>): void {
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

  protected updateForm(emailNotification: IEmailNotification): void {
    this.editForm.patchValue({
      id: emailNotification.id,
      content: emailNotification.content,
      time: emailNotification.time,
      teacher: emailNotification.teacher,
    });
  }

  protected createFromForm(): IEmailNotification {
    return {
      ...new EmailNotification(),
      id: this.editForm.get(['id'])!.value,
      content: this.editForm.get(['content'])!.value,
      time: this.editForm.get(['time'])!.value,
      teacher: this.editForm.get(['teacher'])!.value,
    };
  }
}
