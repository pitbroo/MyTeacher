import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmailNotificationUser } from '../email-notification-user.model';
import { EmailNotificationUserService } from '../service/email-notification-user.service';

@Component({
  templateUrl: './email-notification-user-delete-dialog.component.html',
})
export class EmailNotificationUserDeleteDialogComponent {
  emailNotificationUser?: IEmailNotificationUser;

  constructor(protected emailNotificationUserService: EmailNotificationUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emailNotificationUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
