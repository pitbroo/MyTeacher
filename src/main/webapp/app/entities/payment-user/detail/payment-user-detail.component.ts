import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentUser } from '../payment-user.model';

@Component({
  selector: 'jhi-payment-user-detail',
  templateUrl: './payment-user-detail.component.html',
})
export class PaymentUserDetailComponent implements OnInit {
  paymentUser: IPaymentUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentUser }) => {
      this.paymentUser = paymentUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
