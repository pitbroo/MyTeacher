import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEmailNotification, EmailNotification } from '../email-notification.model';

import { EmailNotificationService } from './email-notification.service';

describe('Service Tests', () => {
  describe('EmailNotification Service', () => {
    let service: EmailNotificationService;
    let httpMock: HttpTestingController;
    let elemDefault: IEmailNotification;
    let expectedResult: IEmailNotification | IEmailNotification[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EmailNotificationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        content: 'AAAAAAA',
        time: currentDate,
        teacher: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            time: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a EmailNotification', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            time: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            time: currentDate,
          },
          returnedFromService
        );

        service.create(new EmailNotification()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EmailNotification', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            content: 'BBBBBB',
            time: currentDate.format(DATE_FORMAT),
            teacher: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            time: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a EmailNotification', () => {
        const patchObject = Object.assign(
          {
            content: 'BBBBBB',
            time: currentDate.format(DATE_FORMAT),
          },
          new EmailNotification()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            time: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EmailNotification', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            content: 'BBBBBB',
            time: currentDate.format(DATE_FORMAT),
            teacher: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            time: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a EmailNotification', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEmailNotificationToCollectionIfMissing', () => {
        it('should add a EmailNotification to an empty array', () => {
          const emailNotification: IEmailNotification = { id: 123 };
          expectedResult = service.addEmailNotificationToCollectionIfMissing([], emailNotification);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(emailNotification);
        });

        it('should not add a EmailNotification to an array that contains it', () => {
          const emailNotification: IEmailNotification = { id: 123 };
          const emailNotificationCollection: IEmailNotification[] = [
            {
              ...emailNotification,
            },
            { id: 456 },
          ];
          expectedResult = service.addEmailNotificationToCollectionIfMissing(emailNotificationCollection, emailNotification);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EmailNotification to an array that doesn't contain it", () => {
          const emailNotification: IEmailNotification = { id: 123 };
          const emailNotificationCollection: IEmailNotification[] = [{ id: 456 }];
          expectedResult = service.addEmailNotificationToCollectionIfMissing(emailNotificationCollection, emailNotification);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(emailNotification);
        });

        it('should add only unique EmailNotification to an array', () => {
          const emailNotificationArray: IEmailNotification[] = [{ id: 123 }, { id: 456 }, { id: 94200 }];
          const emailNotificationCollection: IEmailNotification[] = [{ id: 123 }];
          expectedResult = service.addEmailNotificationToCollectionIfMissing(emailNotificationCollection, ...emailNotificationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const emailNotification: IEmailNotification = { id: 123 };
          const emailNotification2: IEmailNotification = { id: 456 };
          expectedResult = service.addEmailNotificationToCollectionIfMissing([], emailNotification, emailNotification2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(emailNotification);
          expect(expectedResult).toContain(emailNotification2);
        });

        it('should accept null and undefined values', () => {
          const emailNotification: IEmailNotification = { id: 123 };
          expectedResult = service.addEmailNotificationToCollectionIfMissing([], null, emailNotification, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(emailNotification);
        });

        it('should return initial array if no EmailNotification is added', () => {
          const emailNotificationCollection: IEmailNotification[] = [{ id: 123 }];
          expectedResult = service.addEmailNotificationToCollectionIfMissing(emailNotificationCollection, undefined, null);
          expect(expectedResult).toEqual(emailNotificationCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
