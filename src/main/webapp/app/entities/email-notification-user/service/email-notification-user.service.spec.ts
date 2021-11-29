import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmailNotificationUser, EmailNotificationUser } from '../email-notification-user.model';

import { EmailNotificationUserService } from './email-notification-user.service';

describe('Service Tests', () => {
  describe('EmailNotificationUser Service', () => {
    let service: EmailNotificationUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IEmailNotificationUser;
    let expectedResult: IEmailNotificationUser | IEmailNotificationUser[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EmailNotificationUserService);
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

      it('should create a EmailNotificationUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EmailNotificationUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EmailNotificationUser', () => {
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

      it('should partial update a EmailNotificationUser', () => {
        const patchObject = Object.assign({}, new EmailNotificationUser());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EmailNotificationUser', () => {
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

      it('should delete a EmailNotificationUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEmailNotificationUserToCollectionIfMissing', () => {
        it('should add a EmailNotificationUser to an empty array', () => {
          const emailNotificationUser: IEmailNotificationUser = { id: 123 };
          expectedResult = service.addEmailNotificationUserToCollectionIfMissing([], emailNotificationUser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(emailNotificationUser);
        });

        it('should not add a EmailNotificationUser to an array that contains it', () => {
          const emailNotificationUser: IEmailNotificationUser = { id: 123 };
          const emailNotificationUserCollection: IEmailNotificationUser[] = [
            {
              ...emailNotificationUser,
            },
            { id: 456 },
          ];
          expectedResult = service.addEmailNotificationUserToCollectionIfMissing(emailNotificationUserCollection, emailNotificationUser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EmailNotificationUser to an array that doesn't contain it", () => {
          const emailNotificationUser: IEmailNotificationUser = { id: 123 };
          const emailNotificationUserCollection: IEmailNotificationUser[] = [{ id: 456 }];
          expectedResult = service.addEmailNotificationUserToCollectionIfMissing(emailNotificationUserCollection, emailNotificationUser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(emailNotificationUser);
        });

        it('should add only unique EmailNotificationUser to an array', () => {
          const emailNotificationUserArray: IEmailNotificationUser[] = [{ id: 123 }, { id: 456 }, { id: 85048 }];
          const emailNotificationUserCollection: IEmailNotificationUser[] = [{ id: 123 }];
          expectedResult = service.addEmailNotificationUserToCollectionIfMissing(
            emailNotificationUserCollection,
            ...emailNotificationUserArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const emailNotificationUser: IEmailNotificationUser = { id: 123 };
          const emailNotificationUser2: IEmailNotificationUser = { id: 456 };
          expectedResult = service.addEmailNotificationUserToCollectionIfMissing([], emailNotificationUser, emailNotificationUser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(emailNotificationUser);
          expect(expectedResult).toContain(emailNotificationUser2);
        });

        it('should accept null and undefined values', () => {
          const emailNotificationUser: IEmailNotificationUser = { id: 123 };
          expectedResult = service.addEmailNotificationUserToCollectionIfMissing([], null, emailNotificationUser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(emailNotificationUser);
        });

        it('should return initial array if no EmailNotificationUser is added', () => {
          const emailNotificationUserCollection: IEmailNotificationUser[] = [{ id: 123 }];
          expectedResult = service.addEmailNotificationUserToCollectionIfMissing(emailNotificationUserCollection, undefined, null);
          expect(expectedResult).toEqual(emailNotificationUserCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
