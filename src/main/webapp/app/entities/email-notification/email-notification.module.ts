import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmailNotificationComponent } from './list/email-notification.component';
import { EmailNotificationDetailComponent } from './detail/email-notification-detail.component';
import { EmailNotificationUpdateComponent } from './update/email-notification-update.component';
import { EmailNotificationDeleteDialogComponent } from './delete/email-notification-delete-dialog.component';
import { EmailNotificationRoutingModule } from './route/email-notification-routing.module';

@NgModule({
  imports: [SharedModule, EmailNotificationRoutingModule],
  declarations: [
    EmailNotificationComponent,
    EmailNotificationDetailComponent,
    EmailNotificationUpdateComponent,
    EmailNotificationDeleteDialogComponent,
  ],
  entryComponents: [EmailNotificationDeleteDialogComponent],
})
export class EmailNotificationModule {}
