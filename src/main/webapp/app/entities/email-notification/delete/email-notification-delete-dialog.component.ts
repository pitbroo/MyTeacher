import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmailNotification } from '../email-notification.model';
import { EmailNotificationService } from '../service/email-notification.service';

@Component({
  templateUrl: './email-notification-delete-dialog.component.html',
})
export class EmailNotificationDeleteDialogComponent {
  emailNotification?: IEmailNotification;

  constructor(protected emailNotificationService: EmailNotificationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emailNotificationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
