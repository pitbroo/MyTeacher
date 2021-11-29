import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentUser } from '../payment-user.model';
import { PaymentUserService } from '../service/payment-user.service';

@Component({
  templateUrl: './payment-user-delete-dialog.component.html',
})
export class PaymentUserDeleteDialogComponent {
  paymentUser?: IPaymentUser;

  constructor(protected paymentUserService: PaymentUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
