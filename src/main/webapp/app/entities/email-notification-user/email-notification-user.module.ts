import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmailNotificationUserComponent } from './list/email-notification-user.component';
import { EmailNotificationUserDetailComponent } from './detail/email-notification-user-detail.component';
import { EmailNotificationUserUpdateComponent } from './update/email-notification-user-update.component';
import { EmailNotificationUserDeleteDialogComponent } from './delete/email-notification-user-delete-dialog.component';
import { EmailNotificationUserRoutingModule } from './route/email-notification-user-routing.module';

@NgModule({
  imports: [SharedModule, EmailNotificationUserRoutingModule],
  declarations: [
    EmailNotificationUserComponent,
    EmailNotificationUserDetailComponent,
    EmailNotificationUserUpdateComponent,
    EmailNotificationUserDeleteDialogComponent,
  ],
  entryComponents: [EmailNotificationUserDeleteDialogComponent],
})
export class EmailNotificationUserModule {}
