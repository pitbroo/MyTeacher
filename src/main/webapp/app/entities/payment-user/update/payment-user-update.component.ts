import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPaymentUser, PaymentUser } from '../payment-user.model';
import { PaymentUserService } from '../service/payment-user.service';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-payment-user-update',
  templateUrl: './payment-user-update.component.html',
})
export class PaymentUserUpdateComponent implements OnInit {
  isSaving = false;

  paymentsSharedCollection: IPayment[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    payment: [],
    user: [],
  });

  constructor(
    protected paymentUserService: PaymentUserService,
    protected paymentService: PaymentService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentUser }) => {
      this.updateForm(paymentUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentUser = this.createFromForm();
    if (paymentUser.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentUserService.update(paymentUser));
    } else {
      this.subscribeToSaveResponse(this.paymentUserService.create(paymentUser));
    }
  }

  trackPaymentById(index: number, item: IPayment): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(paymentUser: IPaymentUser): void {
    this.editForm.patchValue({
      id: paymentUser.id,
      payment: paymentUser.payment,
      user: paymentUser.user,
    });

    this.paymentsSharedCollection = this.paymentService.addPaymentToCollectionIfMissing(this.paymentsSharedCollection, paymentUser.payment);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, paymentUser.user);
  }

  protected loadRelationshipsOptions(): void {
    this.paymentService
      .query()
      .pipe(map((res: HttpResponse<IPayment[]>) => res.body ?? []))
      .pipe(
        map((payments: IPayment[]) => this.paymentService.addPaymentToCollectionIfMissing(payments, this.editForm.get('payment')!.value))
      )
      .subscribe((payments: IPayment[]) => (this.paymentsSharedCollection = payments));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPaymentUser {
    return {
      ...new PaymentUser(),
      id: this.editForm.get(['id'])!.value,
      payment: this.editForm.get(['payment'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
