import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentUserComponent } from './list/payment-user.component';
import { PaymentUserDetailComponent } from './detail/payment-user-detail.component';
import { PaymentUserUpdateComponent } from './update/payment-user-update.component';
import { PaymentUserDeleteDialogComponent } from './delete/payment-user-delete-dialog.component';
import { PaymentUserRoutingModule } from './route/payment-user-routing.module';

@NgModule({
  imports: [SharedModule, PaymentUserRoutingModule],
  declarations: [PaymentUserComponent, PaymentUserDetailComponent, PaymentUserUpdateComponent, PaymentUserDeleteDialogComponent],
  entryComponents: [PaymentUserDeleteDialogComponent],
})
export class PaymentUserModule {}
