import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPaymentUser, PaymentUser } from '../payment-user.model';

import { PaymentUserService } from './payment-user.service';

describe('Service Tests', () => {
  describe('PaymentUser Service', () => {
    let service: PaymentUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentUser;
    let expectedResult: IPaymentUser | IPaymentUser[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PaymentUserService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PaymentUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PaymentUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PaymentUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PaymentUser', () => {
        const patchObject = Object.assign({}, new PaymentUser());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PaymentUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PaymentUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPaymentUserToCollectionIfMissing', () => {
        it('should add a PaymentUser to an empty array', () => {
          const paymentUser: IPaymentUser = { id: 123 };
          expectedResult = service.addPaymentUserToCollectionIfMissing([], paymentUser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paymentUser);
        });

        it('should not add a PaymentUser to an array that contains it', () => {
          const paymentUser: IPaymentUser = { id: 123 };
          const paymentUserCollection: IPaymentUser[] = [
            {
              ...paymentUser,
            },
            { id: 456 },
          ];
          expectedResult = service.addPaymentUserToCollectionIfMissing(paymentUserCollection, paymentUser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PaymentUser to an array that doesn't contain it", () => {
          const paymentUser: IPaymentUser = { id: 123 };
          const paymentUserCollection: IPaymentUser[] = [{ id: 456 }];
          expectedResult = service.addPaymentUserToCollectionIfMissing(paymentUserCollection, paymentUser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paymentUser);
        });

        it('should add only unique PaymentUser to an array', () => {
          const paymentUserArray: IPaymentUser[] = [{ id: 123 }, { id: 456 }, { id: 84464 }];
          const paymentUserCollection: IPaymentUser[] = [{ id: 123 }];
          expectedResult = service.addPaymentUserToCollectionIfMissing(paymentUserCollection, ...paymentUserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const paymentUser: IPaymentUser = { id: 123 };
          const paymentUser2: IPaymentUser = { id: 456 };
          expectedResult = service.addPaymentUserToCollectionIfMissing([], paymentUser, paymentUser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paymentUser);
          expect(expectedResult).toContain(paymentUser2);
        });

        it('should accept null and undefined values', () => {
          const paymentUser: IPaymentUser = { id: 123 };
          expectedResult = service.addPaymentUserToCollectionIfMissing([], null, paymentUser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paymentUser);
        });

        it('should return initial array if no PaymentUser is added', () => {
          const paymentUserCollection: IPaymentUser[] = [{ id: 123 }];
          expectedResult = service.addPaymentUserToCollectionIfMissing(paymentUserCollection, undefined, null);
          expect(expectedResult).toEqual(paymentUserCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
