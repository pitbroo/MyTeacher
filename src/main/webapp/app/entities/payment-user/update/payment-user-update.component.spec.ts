jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentUserService } from '../service/payment-user.service';
import { IPaymentUser, PaymentUser } from '../payment-user.model';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { PaymentUserUpdateComponent } from './payment-user-update.component';

describe('Component Tests', () => {
  describe('PaymentUser Management Update Component', () => {
    let comp: PaymentUserUpdateComponent;
    let fixture: ComponentFixture<PaymentUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentUserService: PaymentUserService;
    let paymentService: PaymentService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PaymentUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentUserService = TestBed.inject(PaymentUserService);
      paymentService = TestBed.inject(PaymentService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Payment query and add missing value', () => {
        const paymentUser: IPaymentUser = { id: 456 };
        const payment: IPayment = { id: 49940 };
        paymentUser.payment = payment;

        const paymentCollection: IPayment[] = [{ id: 83594 }];
        jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
        const additionalPayments = [payment];
        const expectedCollection: IPayment[] = [...additionalPayments, ...paymentCollection];
        jest.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ paymentUser });
        comp.ngOnInit();

        expect(paymentService.query).toHaveBeenCalled();
        expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(paymentCollection, ...additionalPayments);
        expect(comp.paymentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const paymentUser: IPaymentUser = { id: 456 };
        const user: IUser = { id: 44651 };
        paymentUser.user = user;

        const userCollection: IUser[] = [{ id: 99383 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ paymentUser });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const paymentUser: IPaymentUser = { id: 456 };
        const payment: IPayment = { id: 60396 };
        paymentUser.payment = payment;
        const user: IUser = { id: 57454 };
        paymentUser.user = user;

        activatedRoute.data = of({ paymentUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paymentUser));
        expect(comp.paymentsSharedCollection).toContain(payment);
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentUser>>();
        const paymentUser = { id: 123 };
        jest.spyOn(paymentUserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(paymentUserService.update).toHaveBeenCalledWith(paymentUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentUser>>();
        const paymentUser = new PaymentUser();
        jest.spyOn(paymentUserService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentUser }));
        saveSubject.complete();

        // THEN
        expect(paymentUserService.create).toHaveBeenCalledWith(paymentUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentUser>>();
        const paymentUser = { id: 123 };
        jest.spyOn(paymentUserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(paymentUserService.update).toHaveBeenCalledWith(paymentUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPaymentById', () => {
        it('Should return tracked Payment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
