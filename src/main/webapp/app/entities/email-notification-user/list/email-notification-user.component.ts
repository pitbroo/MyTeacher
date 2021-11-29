import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmailNotificationUser } from '../email-notification-user.model';
import { EmailNotificationUserService } from '../service/email-notification-user.service';
import { EmailNotificationUserDeleteDialogComponent } from '../delete/email-notification-user-delete-dialog.component';

@Component({
  selector: 'jhi-email-notification-user',
  templateUrl: './email-notification-user.component.html',
})
export class EmailNotificationUserComponent implements OnInit {
  emailNotificationUsers?: IEmailNotificationUser[];
  isLoading = false;

  constructor(protected emailNotificationUserService: EmailNotificationUserService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.emailNotificationUserService.query().subscribe(
      (res: HttpResponse<IEmailNotificationUser[]>) => {
        this.isLoading = false;
        this.emailNotificationUsers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEmailNotificationUser): number {
    return item.id!;
  }

  delete(emailNotificationUser: IEmailNotificationUser): void {
    const modalRef = this.modalService.open(EmailNotificationUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.emailNotificationUser = emailNotificationUser;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
