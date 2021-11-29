import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentUser } from '../payment-user.model';
import { PaymentUserService } from '../service/payment-user.service';
import { PaymentUserDeleteDialogComponent } from '../delete/payment-user-delete-dialog.component';

@Component({
  selector: 'jhi-payment-user',
  templateUrl: './payment-user.component.html',
})
export class PaymentUserComponent implements OnInit {
  paymentUsers?: IPaymentUser[];
  isLoading = false;

  constructor(protected paymentUserService: PaymentUserService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.paymentUserService.query().subscribe(
      (res: HttpResponse<IPaymentUser[]>) => {
        this.isLoading = false;
        this.paymentUsers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPaymentUser): number {
    return item.id!;
  }

  delete(paymentUser: IPaymentUser): void {
    const modalRef = this.modalService.open(PaymentUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paymentUser = paymentUser;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
