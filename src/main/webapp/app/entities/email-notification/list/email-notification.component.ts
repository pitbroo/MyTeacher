import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmailNotification } from '../email-notification.model';
import { EmailNotificationService } from '../service/email-notification.service';
import { EmailNotificationDeleteDialogComponent } from '../delete/email-notification-delete-dialog.component';

@Component({
  selector: 'jhi-email-notification',
  templateUrl: './email-notification.component.html',
})
export class EmailNotificationComponent implements OnInit {
  emailNotifications?: IEmailNotification[];
  isLoading = false;

  constructor(protected emailNotificationService: EmailNotificationService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.emailNotificationService.query().subscribe(
      (res: HttpResponse<IEmailNotification[]>) => {
        this.isLoading = false;
        this.emailNotifications = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEmailNotification): number {
    return item.id!;
  }

  delete(emailNotification: IEmailNotification): void {
    const modalRef = this.modalService.open(EmailNotificationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.emailNotification = emailNotification;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
